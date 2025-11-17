# CI/CD Deployment Setup

This guide explains how to set up automated deployments using GitHub Actions and Jib.

## Workflow Overview

1. **Push to main/kotlin-main branch** → Triggers GitHub Actions
2. **GitHub Actions** builds Docker image with Jib
3. **Jib** pushes image to GitHub Container Registry (ghcr.io)
4. **GitHub Actions** SSHs into Mikrus server
5. **Server** pulls new image and restarts container

## Prerequisites

- GitHub repository with your code
- Mikrus VPS with Docker installed
- SSH access to your server

## Part 1: Server Setup

### 1. Install Docker on Server

```bash
ssh root@your-mikrus-server -p YOUR_SSH_PORT

# Install Docker
apt update
apt install -y ca-certificates curl gnupg lsb-release

mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

apt update
apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

systemctl enable docker
systemctl start docker
```

### 2. Create Application Directory

```bash
mkdir -p /opt/shapyfy
cd /opt/shapyfy
```

### 3. Create Environment File

Create `/opt/shapyfy/.env` with your credentials:

```bash
cat > /opt/shapyfy/.env << 'EOF'
# Database credentials
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# Sentry
SENTRY_DSN=your_sentry_dsn

# GitHub repository (for docker-compose image path)
GITHUB_REPOSITORY=YOUR_GITHUB_USERNAME/shapyfy-core
EOF

chmod 600 /opt/shapyfy/.env
```

### 4. Copy docker-compose.yml to Server

```bash
# From your local machine
scp -P YOUR_SSH_PORT docker-compose.yml root@your-mikrus-server:/opt/shapyfy/
```

Or create it manually on the server - it's already configured to use GHCR.

## Part 2: GitHub Setup

### 1. Make GitHub Container Registry Public (or Configure Auth)

**Option A: Make Package Public (Easier)**
1. Go to your GitHub repository
2. Click on "Packages" (right sidebar after first push)
3. Click on your package
4. Click "Package settings"
5. Scroll to "Danger Zone"
6. Click "Change visibility" → Make public

**Option B: Use Authentication (More Secure)**
Keep package private and authenticate on server (already configured in workflow).

### 2. Add GitHub Secrets

Go to your repository → Settings → Secrets and variables → Actions → New repository secret:

Add these secrets:

| Secret Name | Value | Description |
|------------|-------|-------------|
| `SERVER_HOST` | Your Mikrus server IP | e.g., `123.45.67.89` |
| `SERVER_USER` | `root` | SSH username |
| `SERVER_PORT` | Your SSH port | e.g., `12345` |
| `SERVER_SSH_KEY` | Your private SSH key | Full content of `~/.ssh/id_rsa` |

### 3. Generate SSH Key (if you don't have one)

On your local machine:

```bash
# Generate new SSH key
ssh-keygen -t ed25519 -C "github-actions" -f ~/.ssh/github-actions

# Copy public key to server
ssh-copy-id -i ~/.ssh/github-actions.pub -p YOUR_SSH_PORT root@your-mikrus-server

# Copy private key content for GitHub secret
cat ~/.ssh/github-actions
```

Copy the ENTIRE output (including `-----BEGIN OPENSSH PRIVATE KEY-----` and `-----END OPENSSH PRIVATE KEY-----`) and paste into `SERVER_SSH_KEY` secret.

## Part 3: Workflow Files

Two workflows are already created:

### `.github/workflows/test.yml`
- Runs on every push and PR
- Executes all tests
- Uploads test results

### `.github/workflows/deploy.yml`
- Runs on push to main/kotlin-main branches
- Builds image with Jib
- Pushes to GitHub Container Registry
- SSHs to server and deploys

## Part 4: First Deployment

### 1. Initial Manual Setup on Server

For the first deployment, manually pull and start once:

```bash
ssh root@your-mikrus-server -p YOUR_SSH_PORT
cd /opt/shapyfy

# Login to GHCR (one-time, or use GitHub token)
docker login ghcr.io

# Pull and start (will fail until first CI/CD push)
# You'll do this after first GitHub Actions run
```

### 2. Trigger First Build

```bash
# From your local machine, commit and push
git add .
git commit -m "Add CI/CD configuration"
git push origin kotlin-main
```

### 3. Monitor Deployment

1. Go to your GitHub repository
2. Click "Actions" tab
3. Watch the workflow run
4. Check both "Build and Push Docker Image" and "Deploy to Server" jobs

### 4. Verify on Server

```bash
ssh root@your-mikrus-server -p YOUR_SSH_PORT

# Check if container is running
docker ps

# Check logs
cd /opt/shapyfy
docker compose logs -f

# Check health
curl http://localhost:8080/actuator/health
```

## Part 5: Day-to-Day Usage

### Deploying Changes

Simply push to main or kotlin-main branch:

```bash
git add .
git commit -m "Your changes"
git push origin kotlin-main
```

GitHub Actions will automatically:
1. Run tests
2. Build Docker image
3. Push to registry
4. Deploy to server
5. Restart container

### Manual Deployment Trigger

You can manually trigger deployment from GitHub:
1. Go to Actions tab
2. Click "Build and Deploy" workflow
3. Click "Run workflow"
4. Select branch
5. Click "Run workflow"

### Monitoring Deployments

**GitHub Actions:**
- Repository → Actions tab
- View logs for each step
- See test results

**Server:**
```bash
# SSH to server
ssh root@your-mikrus-server -p YOUR_SSH_PORT

# View logs
cd /opt/shapyfy
docker compose logs -f

# Check status
docker compose ps

# Check health
curl http://localhost:8080/actuator/health
```

## Troubleshooting

### Workflow Fails at "Build and push with Jib"

**Error:** `unauthorized: unauthenticated`
- Check that GITHUB_TOKEN has packages:write permission
- This should work automatically - check repository settings

**Error:** `Permission denied`
- Verify Java 21 is set up correctly
- Check Gradle wrapper permissions: `git update-index --chmod=+x gradlew`

### Workflow Fails at "Deploy to Server"

**Error:** `Permission denied (publickey)`
- Verify `SERVER_SSH_KEY` secret contains the full private key
- Check that public key is in server's `~/.ssh/authorized_keys`
- Verify `SERVER_PORT` matches your Mikrus SSH port

**Error:** `docker: command not found`
- Docker not installed on server
- Run Part 1 server setup steps

**Error:** `docker compose pull` fails
- If package is private, ensure server can authenticate to GHCR
- Consider making package public (easier)
- Or configure proper authentication

### Container Won't Start on Server

```bash
# Check logs
docker compose logs

# Check environment variables
docker compose config

# Verify .env file
cat /opt/shapyfy/.env

# Check database connectivity
docker compose exec shapyfy-core sh -c "nc -zv psql01.mikr.us 5432"
```

### Image Not Found on Server

```bash
# Verify image was pushed
# Check GitHub Packages in your repository

# Login to GHCR on server
echo YOUR_GITHUB_TOKEN | docker login ghcr.io -u YOUR_USERNAME --password-stdin

# Try manual pull
docker pull ghcr.io/YOUR_USERNAME/shapyfy-core:latest
```

## Security Best Practices

1. **SSH Keys:**
   - Use separate SSH key for GitHub Actions
   - Restrict key permissions on server
   - Consider using deploy keys instead of root access

2. **Secrets:**
   - Never commit `.env` file
   - Rotate credentials regularly
   - Use GitHub encrypted secrets

3. **Container Registry:**
   - Keep packages private if possible
   - Use authentication tokens with minimal permissions
   - Enable vulnerability scanning

4. **Server:**
   - Configure firewall (UFW)
   - Keep Docker updated
   - Run containers as non-root (already configured in Jib)
   - Regular security updates

## Advanced Configuration

### Deploy to Multiple Environments

Create separate workflows for staging and production:

```yaml
# .github/workflows/deploy-staging.yml
on:
  push:
    branches:
      - develop

# .github/workflows/deploy-production.yml
on:
  push:
    branches:
      - main
```

Use different secrets for each environment:
- `STAGING_SERVER_HOST`, `PRODUCTION_SERVER_HOST`
- `STAGING_SSH_KEY`, `PRODUCTION_SSH_KEY`

### Rollback Strategy

```bash
# On server, rollback to previous image
ssh root@your-mikrus-server -p YOUR_SSH_PORT
cd /opt/shapyfy

# Pull specific version by commit SHA
docker pull ghcr.io/YOUR_USERNAME/shapyfy-core:COMMIT_SHA
docker tag ghcr.io/YOUR_USERNAME/shapyfy-core:COMMIT_SHA ghcr.io/YOUR_USERNAME/shapyfy-core:latest

# Restart
docker compose up -d --force-recreate
```

### Notifications

Add Slack/Discord notifications to workflow:

```yaml
- name: Notify Slack
  if: always()
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

## Monitoring

### Setup Health Check Monitoring

Consider adding external monitoring:
- UptimeRobot (free tier available)
- Healthchecks.io
- Better Stack (formerly Better Uptime)

### Log Aggregation

For production, consider:
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Grafana Loki
- Cloud logging services

## Cost Optimization

### GitHub Actions Minutes
- Free tier: 2,000 minutes/month for private repos
- Public repos: unlimited
- Optimize by caching dependencies (already configured)

### Container Registry Storage
- GitHub Packages: 500MB free for private repos
- Clean up old images regularly
- Use `docker image prune` in workflow

### Server Resources
- Mikrus 2.1 (1GB RAM) minimum recommended
- Monitor usage with `docker stats`
- Consider upgrade if needed

## Next Steps

1. Set up SSL certificates (Let's Encrypt)
2. Configure monitoring and alerting
3. Add database backup automation
4. Set up staging environment
5. Configure log rotation
6. Add performance monitoring (APM)

## Support

- GitHub Actions docs: https://docs.github.com/en/actions
- Jib docs: https://github.com/GoogleContainerTools/jib
- Docker Compose docs: https://docs.docker.com/compose/
- Mikrus support: https://mikr.us/
