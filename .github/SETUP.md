# GitHub Environments Setup - Step by Step Guide

This guide walks you through setting up GitHub Environments and Secrets for automated CI/CD deployment.

## Overview

You will create **two environments**:
- **development**: Auto-deploys when you push to `kotlin-main` branch
- **production**: Deploys when you push to `main` branch (requires manual approval)

---

## Prerequisites

Before starting, ensure you have:
- ✅ Access to your Mikrus servers
- ✅ Database credentials (username and password)
- ✅ SSH access to servers
- ✅ Admin access to GitHub repository: `faczor/shapyfy`

---

## Part 1: Generate SSH Keys

### Step 1.1: Create Development SSH Key

On your **local machine**, run:

```bash
# Generate SSH key for development
ssh-keygen -t ed25519 -C "github-actions-dev" -f ~/.ssh/shapyfy-dev

# When prompted:
# - Enter file: (press Enter to use default location shown)
# - Enter passphrase: (press Enter for no passphrase - required for automation)
# - Confirm passphrase: (press Enter)

# Copy public key to development server
ssh-copy-id -i ~/.ssh/shapyfy-dev.pub -p 10247 root@adrian247.mikrus.xyz

# When prompted, enter your server password
```

**Expected output:**
```
Number of key(s) added: 1

Now try logging into the machine with: "ssh -p 10247 'root@adrian247.mikrus.xyz'"
```

### Step 1.2: Test SSH Key

Verify the key works:

```bash
ssh -i ~/.ssh/shapyfy-dev.pub -p 10247 root@adrian247.mikrus.xyz
# Should log in WITHOUT asking for password
exit
```

### Step 1.3: Get Private Key Content

```bash
# Display the private key (you'll paste this into GitHub)
cat ~/.ssh/shapyfy-dev
```

**IMPORTANT:**
- Copy the ENTIRE output, including `-----BEGIN OPENSSH PRIVATE KEY-----` and `-----END OPENSSH PRIVATE KEY-----`
- Keep this window open or save to a temporary file - you'll need it for GitHub secrets

### Step 1.4: Production SSH Key (Optional - Set up later)

When you have your production server, repeat the above for production:

```bash
ssh-keygen -t ed25519 -C "github-actions-prod" -f ~/.ssh/shapyfy-prod
ssh-copy-id -i ~/.ssh/shapyfy-prod.pub -p PROD_PORT root@PROD_SERVER
cat ~/.ssh/shapyfy-prod
```

---

## Part 2: Create GitHub Environments

### Step 2.1: Navigate to Environments

1. Go to: https://github.com/faczor/shapyfy
2. Click **Settings** (top menu bar)
3. In left sidebar, scroll down and click **Environments**

### Step 2.2: Create Development Environment

1. Click **New environment** button
2. Enter name: `development`
3. Click **Configure environment**

**Configure deployment protection:**
- **Deployment branches**: Click dropdown → Select **"Selected branches"**
- Click **"Add deployment branch rule"**
- Enter branch name: `kotlin-main`
- Click **"Add rule"**

**Protection rules:**
- Leave **all checkboxes unchecked** (no approval needed for dev)

4. Click **"Add secret"** (we'll add these in Part 3)

### Step 2.3: Create Production Environment

1. Click **"New environment"** button again
2. Enter name: `production`
3. Click **Configure environment**

**Configure deployment protection:**
- **Deployment branches**: Click dropdown → Select **"Selected branches"**
- Click **"Add deployment branch rule"**
- Enter branch name: `main`
- Click **"Add rule"**

**Protection rules:**
- ✅ Check **"Required reviewers"**
- Click **"Add reviewers"**
- Select yourself (your GitHub username)
- **Wait timer**: 0 minutes (or set to 5 minutes for safety buffer)

4. Click **"Add secret"** (we'll add these next)

---

## Part 3: Add Environment Secrets

### Step 3.1: Add Development Secrets

Navigate to: `Settings` → `Environments` → `development`

Click **"Add secret"** and add each of these secrets one by one:

#### Secret 1: SERVER_HOST
- **Name:** `SERVER_HOST`
- **Value:** `adrian247.mikrus.xyz`
- Click **"Add secret"**

#### Secret 2: SERVER_PORT
- **Name:** `SERVER_PORT`
- **Value:** `10247`
- Click **"Add secret"**

#### Secret 3: SERVER_USER
- **Name:** `SERVER_USER`
- **Value:** `root`
- Click **"Add secret"**

#### Secret 4: SERVER_SSH_KEY
- **Name:** `SERVER_SSH_KEY`
- **Value:** (Paste the output from `cat ~/.ssh/shapyfy-dev`)
  ```
  -----BEGIN OPENSSH PRIVATE KEY-----
  b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZW
  ... (multiple lines) ...
  -----END OPENSSH PRIVATE KEY-----
  ```
- Click **"Add secret"**

#### Secret 5: DB_USERNAME
- **Name:** `DB_USERNAME`
- **Value:** `your_database_username`
- Click **"Add secret"**

#### Secret 6: DB_PASSWORD
- **Name:** `DB_PASSWORD`
- **Value:** `your_database_password`
- Click **"Add secret"**

#### Secret 7: SENTRY_DSN (Optional)
- **Name:** `SENTRY_DSN`
- **Value:** `your_sentry_dsn` (or leave empty if not using Sentry yet)
- Click **"Add secret"**

**✅ Development environment configured!**

---

### Step 3.2: Add Production Secrets (When Ready)

Navigate to: `Settings` → `Environments` → `production`

Repeat the same process as development, but with production values:

| Secret Name | Production Value |
|------------|------------------|
| `SERVER_HOST` | `production-server.mikrus.xyz` |
| `SERVER_PORT` | `PROD_SSH_PORT` |
| `SERVER_USER` | `root` |
| `SERVER_SSH_KEY` | (Output from `cat ~/.ssh/shapyfy-prod`) |
| `DB_USERNAME` | `prod_db_username` (different from dev!) |
| `DB_PASSWORD` | `prod_db_password` (different from dev!) |
| `SENTRY_DSN` | `production_sentry_dsn` (optional) |
| `DOMAIN` | `api.shapyfy.com` (optional) |

---

## Part 4: Verify Configuration

### Step 4.1: Check Environments

Go to: `Settings` → `Environments`

You should see:

```
📦 development
   Deployment branches: kotlin-main
   Secrets: 7 secrets
   Protection rules: None

🔒 production
   Deployment branches: main
   Secrets: 7-8 secrets
   Protection rules: Required reviewers (you)
```

### Step 4.2: View Configured Secrets

Click on each environment and verify all secrets are listed:
- SERVER_HOST
- SERVER_PORT
- SERVER_USER
- SERVER_SSH_KEY
- DB_USERNAME
- DB_PASSWORD
- SENTRY_DSN

**Note:** You can't view secret values after creation, only names.

---

## Part 5: Test Deployment

### Step 5.1: Trigger Development Deployment

Commit and push your changes to `kotlin-main`:

```bash
# Make sure you're on kotlin-main branch
git checkout kotlin-main

# Add workflow files
git add .github/workflows/test.yml .github/workflows/deploy.yml .github/secrets.md .github/SETUP.md

# Commit
git commit -m "Configure CI/CD workflows and environments"

# Push to trigger deployment
git push origin kotlin-main
```

### Step 5.2: Monitor Workflow

1. Go to: https://github.com/faczor/shapyfy/actions
2. You should see a new workflow run: **"Build and Deploy"**
3. Click on it to watch progress

**Expected workflow steps:**
1. ✅ Run Tests
2. ✅ Build and Push Docker Image
3. ✅ Deploy to Development

### Step 5.3: Check Deployment Summary

After the workflow completes, scroll to the bottom of the workflow run page to see the **Summary** with:
- Docker image details
- Deployment status
- Health check results

---

## Part 6: Make Package Public (Required)

After your first successful push, you need to make the Docker package public:

### Step 6.1: Navigate to Packages

1. Go to: https://github.com/faczor/shapyfy
2. In the right sidebar, click **"Packages"**
3. Click on **"shapyfy"** package

### Step 6.2: Change Visibility

1. Click **"Package settings"** (gear icon, top right)
2. Scroll down to **"Danger Zone"**
3. Click **"Change visibility"**
4. Select **"Public"**
5. Type the package name to confirm: `shapyfy`
6. Click **"I understand, change package visibility"**

**Why?** Public packages don't require authentication when pulling to your server.

---

## Part 7: Verify on Server

### Step 7.1: SSH to Development Server

```bash
ssh root@adrian247.mikrus.xyz -p 10247
```

### Step 7.2: Check Application

```bash
# Navigate to app directory
cd /opt/shapyfy

# Check if container is running
docker ps

# Should show:
# CONTAINER ID   IMAGE                         STATUS         PORTS
# xxxxx          ghcr.io/faczor/shapyfy:latest Up 2 minutes  0.0.0.0:8080->8080/tcp

# Check logs
docker compose logs --tail=50

# Check health
curl http://localhost:8080/actuator/health

# Should return:
# {"status":"UP"}
```

### Step 7.3: Test API Endpoint

```bash
# Test an API endpoint (if you have any)
curl -H "Accept-Language: en" http://localhost:8080/api/v1/exercises
```

---

## Part 8: Production Deployment (Later)

When you're ready to deploy to production:

### Step 8.1: Merge to Main

```bash
# Update main branch with kotlin-main changes
git checkout main
git merge kotlin-main
git push origin main
```

### Step 8.2: Approve Deployment

1. Go to: https://github.com/faczor/shapyfy/actions
2. You'll see the workflow **waiting for approval**
3. Click on the workflow run
4. Click **"Review pending deployments"**
5. Check **production** environment
6. Click **"Approve and deploy"**

The deployment will proceed after approval.

---

## Troubleshooting

### Issue: "Permission denied (publickey)"

**Cause:** SSH key not properly configured

**Fix:**
```bash
# Re-copy public key to server
ssh-copy-id -i ~/.ssh/shapyfy-dev.pub -p 10247 root@adrian247.mikrus.xyz

# Verify you can SSH without password
ssh -i ~/.ssh/shapyfy-dev -p 10247 root@adrian247.mikrus.xyz
```

### Issue: "No such file or directory: /opt/shapyfy"

**Cause:** Application directory not created on server

**Fix:**
```bash
# SSH to server
ssh root@adrian247.mikrus.xyz -p 10247

# Create directory
sudo mkdir -p /opt/shapyfy

# Copy docker-compose.yml
# From local machine:
scp -P 10247 docker-compose.yml root@adrian247.mikrus.xyz:/opt/shapyfy/
```

### Issue: "docker: command not found"

**Cause:** Docker not installed on server

**Fix:** See `DEPLOYMENT-DOCKER.md` for Docker installation steps

### Issue: Container starts but health check fails

**Cause:** Database connection or application error

**Fix:**
```bash
# Check logs
cd /opt/shapyfy
docker compose logs

# Check environment variables
docker compose exec shapyfy-core env | grep SPRING

# Verify database credentials in .env file
cat .env
```

### Issue: "Failed to pull image"

**Cause:** Package is private

**Fix:** Make package public (see Part 6)

---

## Success Checklist

After completing this guide, you should have:

- ✅ Two GitHub environments created (development, production)
- ✅ SSH keys generated and configured
- ✅ Development secrets configured (7 secrets)
- ✅ Production secrets configured (when ready)
- ✅ First deployment to development successful
- ✅ GitHub package made public
- ✅ Application running on development server
- ✅ Health check passing

---

## Next Steps

1. **Set up monitoring**: Configure Sentry error tracking
2. **Configure logging**: Set up centralized logging (see TWO-SERVER-SETUP.md)
3. **Production server**: Get second Mikrus VPS for production
4. **SSL/TLS**: Set up HTTPS with Let's Encrypt
5. **Custom domain**: Point your domain to production server
6. **Database backups**: Set up automated backups
7. **Monitoring**: Add uptime monitoring (UptimeRobot, etc.)

---

## Workflow Reference

### Development Workflow
```
1. Make changes locally
2. Commit to kotlin-main branch
3. Push to GitHub
   → Tests run automatically
   → Docker image built
   → Deploys to development server
4. Verify on dev server
```

### Production Workflow
```
1. Merge kotlin-main → main
2. Push to GitHub
   → Tests run automatically
   → Docker image built
   → Waits for manual approval
3. Approve deployment in GitHub Actions
   → Deploys to production server
4. Verify on production server
```

---

## Support

For more information, see:
- [secrets.md](.github/secrets.md) - Complete secrets reference
- [DEPLOYMENT-DOCKER.md](../DEPLOYMENT-DOCKER.md) - Docker deployment guide
- [DEPLOYMENT-CICD.md](../DEPLOYMENT-CICD.md) - CI/CD details

Need help? Check the troubleshooting section above or review the workflow logs in GitHub Actions.
