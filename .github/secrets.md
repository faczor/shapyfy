# GitHub Secrets Configuration Guide

This document describes all secrets required for CI/CD workflows in this repository.

## Table of Contents
- [Environment-Specific Secrets](#environment-specific-secrets)
- [Shared/Public Information](#sharedpublic-information)
- [Setup Instructions](#setup-instructions)

---

## Environment-Specific Secrets

These secrets are **environment-dependent** and must be configured separately for each environment (development, production).

### Server Connection Secrets

| Secret Name | Description | Environment | Example Value | Required |
|------------|-------------|-------------|---------------|----------|
| `SERVER_HOST` | IP address or hostname of deployment server | Per-env | `adrian247.mikrus.xyz` | ✅ Yes |
| `SERVER_PORT` | SSH port for server access | Per-env | `10247` | ✅ Yes |
| `SERVER_USER` | SSH username for deployment | Per-env | `root` | ✅ Yes |
| `SERVER_SSH_KEY` | Private SSH key for authentication | Per-env | `-----BEGIN OPENSSH PRIVATE KEY-----\n...` | ✅ Yes |

**Notes:**
- Development and production should use **different servers**
- Each environment should have its **own dedicated SSH key** for security
- SSH keys must include header/footer lines (`-----BEGIN` and `-----END`)

### Database Secrets

| Secret Name | Description | Environment | Example Value | Required |
|------------|-------------|-------------|---------------|----------|
| `DB_USERNAME` | PostgreSQL database username | Per-env | `dev_user` / `prod_user` | ✅ Yes |
| `DB_PASSWORD` | PostgreSQL database password | Per-env | `strong_password_123` | ✅ Yes |

**Notes:**
- Use **different credentials** for dev and production
- Production passwords should be stronger and rotated regularly
- Database schema is configured in docker-compose.yml: `shapyfy` schema in `db_adrian247` database

### Monitoring Secrets

| Secret Name | Description | Environment | Example Value | Required |
|------------|-------------|-------------|---------------|----------|
| `SENTRY_DSN` | Sentry error tracking DSN | Per-env | `https://...@sentry.io/123456` | ⚠️ Optional |

**Notes:**
- Recommended to use **separate Sentry projects** for dev and production
- Can be left empty if not using Sentry
- Set `SENTRY_ENVIRONMENT` automatically by workflow (development/production)

### Optional Secrets

| Secret Name | Description | Environment | Example Value | Required |
|------------|-------------|-------------|---------------|----------|
| `DOMAIN` | Production domain name (for URL display) | Production only | `api.shapyfy.com` | ❌ No |

---

## Shared/Public Information

These values are **NOT secrets** and can be safely shared or committed to the repository:

### Repository Information

| Variable | Value | Location |
|----------|-------|----------|
| GitHub Repository | `faczor/shapyfy` | Used in workflows |
| Container Registry | `ghcr.io` | GitHub Container Registry |
| Image Name | `ghcr.io/faczor/shapyfy` | Built by Jib |
| Base Image | `eclipse-temurin:21-jre-alpine` | Defined in build.gradle |

### Database Configuration (Public)

| Variable | Value | Notes |
|----------|-------|-------|
| Database Host | `psql01.mikr.us` | External PostgreSQL server |
| Database Port | `5432` | Standard PostgreSQL port |
| Database Name | `db_adrian247` | Shared database |
| Database Schema | `shapyfy` | Application schema |

**Full connection string:**
```
jdbc:postgresql://psql01.mikr.us:5432/db_adrian247?currentSchema=shapyfy
```

### Application Configuration (Public)

| Variable | Value | Environment |
|----------|-------|-------------|
| Spring Profile | `dev` | Development |
| Spring Profile | `production` | Production |
| Sentry Environment | `development` | Development |
| Sentry Environment | `production` | Production |
| Application Port | `8080` | Both environments |

---

## Setup Instructions

### 1. Create GitHub Environments

Go to: `https://github.com/faczor/shapyfy/settings/environments`

#### Dev Environment
```
Name: dev
Deployment branches: kotlin-main
Protection rules: None (auto-deploy)
```

#### Prod Environment
```
Name: prod
Deployment branches: main
Protection rules:
  ✅ Required reviewers (add yourself)
  ⏱️ Wait timer: 0 minutes
```

### 2. Generate SSH Keys

**For Development:**
```bash
# Generate key
ssh-keygen -t ed25519 -C "github-actions-dev" -f ~/.ssh/shapyfy-dev

# Copy to development server
ssh-copy-id -i ~/.ssh/shapyfy-dev.pub -p 10247 root@adrian247.mikrus.xyz

# Get private key for GitHub secret
cat ~/.ssh/shapyfy-dev
```

**For Production:**
```bash
# Generate key
ssh-keygen -t ed25519 -C "github-actions-prod" -f ~/.ssh/shapyfy-prod

# Copy to production server (when available)
ssh-copy-id -i ~/.ssh/shapyfy-prod.pub -p PROD_PORT root@PROD_SERVER

# Get private key for GitHub secret
cat ~/.ssh/shapyfy-prod
```

### 3. Add Secrets to GitHub

#### Dev Environment Secrets

Navigate to: `Settings` → `Environments` → `dev` → `Add secret`

```
SERVER_HOST     = adrian247.mikrus.xyz
SERVER_PORT     = 10247
SERVER_USER     = root
SERVER_SSH_KEY  = (output from: cat ~/.ssh/shapyfy-dev)
DB_USERNAME     = your_dev_db_username
DB_PASSWORD     = your_dev_db_password
SENTRY_DSN      = (optional - your dev Sentry DSN or leave empty)
```

#### Prod Environment Secrets

Navigate to: `Settings` → `Environments` → `prod` → `Add secret`

```
SERVER_HOST     = production-server.mikrus.xyz
SERVER_PORT     = (production SSH port)
SERVER_USER     = root
SERVER_SSH_KEY  = (output from: cat ~/.ssh/shapyfy-prod)
DB_USERNAME     = your_prod_db_username
DB_PASSWORD     = your_prod_db_password
SENTRY_DSN      = (optional - your prod Sentry DSN)
DOMAIN          = (optional - api.shapyfy.com)
```

### 4. Make GitHub Package Public (Recommended)

After your first deployment creates the package:

1. Go to: `https://github.com/faczor/shapyfy/packages`
2. Click on `shapyfy` package
3. Click `Package settings`
4. Scroll to "Danger Zone"
5. Click `Change visibility` → `Public`

**Why?** Public packages don't require authentication to pull, simplifying server configuration.

**Alternative:** Keep private and configure server authentication (see DEPLOYMENT-DOCKER.md)

---

## Security Best Practices

### ✅ DO:
- Use **different SSH keys** for dev and production
- Use **different database credentials** for each environment
- Use **strong passwords** for production (20+ characters)
- **Rotate credentials** regularly (every 90 days)
- Use **separate Sentry projects** for dev and production
- Set **600 permissions** on .env files on servers (`chmod 600 .env`)
- Enable **required reviewers** for production deployments

### ❌ DON'T:
- Don't commit secrets to the repository
- Don't share production credentials in Slack/email
- Don't use the same database user for dev and production
- Don't disable SSH key passphrase for production keys
- Don't reuse SSH keys across projects

---

## Troubleshooting

### "Permission denied (publickey)" Error
- Verify `SERVER_SSH_KEY` contains the **full private key** including header/footer
- Ensure public key is in server's `~/.ssh/authorized_keys`
- Check `SERVER_PORT` matches your SSH configuration

### "Database connection refused" Error
- Verify `DB_USERNAME` and `DB_PASSWORD` are correct
- Check database exists and schema `shapyfy` is created
- Ensure server can reach `psql01.mikr.us:5432` (firewall rules)

### "Container won't start" Error
- SSH to server and check logs: `docker compose logs`
- Verify all environment secrets are set correctly
- Check `.env` file exists: `cat /opt/shapyfy/.env`

### "Image pull failed" Error
- If package is private, make it public (see step 4 above)
- Or configure server authentication to GHCR
- Verify image was pushed: check GitHub Packages

---

## Verification Checklist

Before your first deployment, verify:

- [ ] Both environments created in GitHub (dev, prod)
- [ ] Dev secrets added (7 secrets)
- [ ] Prod secrets added (7-8 secrets)
- [ ] SSH keys generated and copied to servers
- [ ] Docker installed on servers
- [ ] `/opt/shapyfy` directory created on servers
- [ ] `docker-compose.yml` copied to servers
- [ ] GitHub package will be made public after first push

---

## Quick Reference

**Test workflow:** Runs on every push/PR to `main` or `kotlin-main`
```yaml
Workflow: .github/workflows/test.yml
Secrets: None required
```

**Deploy workflow:** Deploys based on branch
```yaml
Workflow: .github/workflows/deploy.yml
Trigger: Push to kotlin-main → deploys to development
Trigger: Push to main → deploys to production (with approval)
Secrets: All environment-specific secrets
```

**Container Registry:**
```
Public URL: https://github.com/faczor/shapyfy/pkgs/container/shapyfy
Image: ghcr.io/faczor/shapyfy:latest
Tags: latest, <commit-sha>
```

---

## Support

For more detailed deployment instructions, see:
- [DEPLOYMENT-DOCKER.md](../DEPLOYMENT-DOCKER.md) - Docker deployment guide
- [DEPLOYMENT-CICD.md](../DEPLOYMENT-CICD.md) - CI/CD setup guide
- [ENVIRONMENTS-SETUP.md](../ENVIRONMENTS-SETUP.md) - Multi-environment guide
- [TWO-SERVER-SETUP.md](../TWO-SERVER-SETUP.md) - Two-server architecture
