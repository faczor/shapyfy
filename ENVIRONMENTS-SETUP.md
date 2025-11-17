# Multi-Environment Setup Guide

This guide explains how to set up separate **Development**, **Staging**, and **Production** environments using GitHub Environments.

## Overview

### Branch Strategy

| Branch | Environment | Auto-Deploy | Use Case |
|--------|-------------|-------------|----------|
| `kotlin-main` | Development | ✅ Yes | Active development, testing features |
| `staging` (optional) | Staging | ✅ Yes | Pre-production testing, QA |
| `main` | Production | ✅ Yes (with approval) | Live production environment |

### Environment Configuration

Each environment has:
- **Separate secrets** (different servers, databases, etc.)
- **Different Spring profiles** (`dev`, `staging`, `production`)
- **Different Sentry environments** (to separate errors)
- **Optional deployment approvals** (especially for production)

## Step 1: Create GitHub Environments

### 1. Go to Repository Settings

1. Navigate to your repository on GitHub
2. Click **Settings** (top menu)
3. In left sidebar, click **Environments**

### 2. Create Development Environment

1. Click **New environment**
2. Name: `development`
3. Click **Configure environment**
4. **Deployment branches**: Select "Selected branches" → Add `kotlin-main`
5. **Environment secrets**: We'll add these next
6. **Protection rules**: Leave unchecked (no approval needed for dev)
7. Click **Save protection rules**

### 3. Create Production Environment

1. Click **New environment**
2. Name: `production`
3. Click **Configure environment**
4. **Deployment branches**: Select "Selected branches" → Add `main`
5. **Protection rules** (recommended):
   - ✅ **Required reviewers**: Add yourself or team members (optional but recommended)
   - ✅ **Wait timer**: 0 minutes (or add delay for safety)
6. **Environment secrets**: We'll add these next
7. Click **Save protection rules**

### 4. Create Staging Environment (Optional)

If you want a staging environment between dev and production:

1. Click **New environment**
2. Name: `staging`
3. **Deployment branches**: Add `staging` branch
4. Configure as needed

## Step 2: Add Environment-Specific Secrets

For **EACH** environment, add the following secrets:

### Development Environment Secrets

Go to **Settings** → **Environments** → **development** → **Add secret**

| Secret Name | Description | Example Value |
|------------|-------------|---------------|
| `SERVER_HOST` | Dev server IP | `123.45.67.89` |
| `SERVER_PORT` | Dev SSH port | `12345` |
| `SERVER_USER` | SSH username | `root` |
| `SERVER_SSH_KEY` | SSH private key | (full key content) |
| `DB_USERNAME` | Dev database username | `dev_user` |
| `DB_PASSWORD` | Dev database password | `dev_password_123` |
| `SENTRY_DSN` | Sentry DSN (dev project) | `https://...@sentry.io/dev-project-id` |

### Production Environment Secrets

Go to **Settings** → **Environments** → **production** → **Add secret**

| Secret Name | Description | Example Value |
|------------|-------------|---------------|
| `SERVER_HOST` | Production server IP | `98.76.54.32` |
| `SERVER_PORT` | Production SSH port | `54321` |
| `SERVER_USER` | SSH username | `root` |
| `SERVER_SSH_KEY` | SSH private key (can be different) | (full key content) |
| `DB_USERNAME` | Production database username | `prod_user` |
| `DB_PASSWORD` | Production database password | `super_secure_prod_pass` |
| `SENTRY_DSN` | Sentry DSN (prod project) | `https://...@sentry.io/prod-project-id` |
| `DOMAIN` | Production domain (optional) | `api.shapyfy.com` |

## Step 3: Deployment Workflow

### How It Works

#### Development Deployment
```bash
# Push to kotlin-main branch
git push origin kotlin-main

# GitHub Actions automatically:
# 1. Builds Docker image
# 2. Pushes to GHCR
# 3. Deploys to DEVELOPMENT environment (no approval needed)
# 4. Uses development secrets
# 5. Sets SPRING_PROFILES_ACTIVE=dev
# 6. Sets SENTRY_ENVIRONMENT=development
```

#### Production Deployment
```bash
# Merge to main branch
git checkout main
git merge kotlin-main
git push origin main

# GitHub Actions automatically:
# 1. Builds Docker image
# 2. Pushes to GHCR
# 3. Waits for approval (if configured)
# 4. Deploys to PRODUCTION environment
# 5. Uses production secrets
# 6. Sets SPRING_PROFILES_ACTIVE=production
# 7. Sets SENTRY_ENVIRONMENT=production
```

## Step 4: Create Spring Profile Configurations (Optional)

You can create environment-specific application properties:

### src/main/resources/application-dev.yaml

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 5  # Lower for dev

logging:
  level:
    com.shapyfy: DEBUG
    org.springframework.web: DEBUG

server:
  error:
    include-stacktrace: always
```

### src/main/resources/application-production.yaml

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20  # Higher for production

logging:
  level:
    com.shapyfy: INFO
    org.springframework.web: WARN

server:
  error:
    include-stacktrace: never
```

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     GitHub Repository                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  kotlin-main branch → development environment               │
│       ↓                                                      │
│   Build & Push Image                                        │
│       ↓                                                      │
│   Deploy to Dev Server (auto)                               │
│   - SPRING_PROFILES_ACTIVE=dev                              │
│   - SENTRY_ENVIRONMENT=development                          │
│   - Uses dev secrets                                        │
│                                                              │
│  ─────────────────────────────────────────────────          │
│                                                              │
│  main branch → production environment                       │
│       ↓                                                      │
│   Build & Push Image                                        │
│       ↓                                                      │
│   Wait for Approval (optional)                              │
│       ↓                                                      │
│   Deploy to Prod Server                                     │
│   - SPRING_PROFILES_ACTIVE=production                       │
│   - SENTRY_ENVIRONMENT=production                           │
│   - Uses production secrets                                 │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## Monitoring Deployments

### In GitHub

1. Go to **Actions** tab
2. Click on a workflow run
3. See separate jobs for each environment:
   - "Build and Push Docker Image" (runs for both)
   - "Deploy to Development" (only for kotlin-main)
   - "Deploy to Production" (only for main)

### Environment Dashboard

1. Go to repository main page
2. Right sidebar shows **Environments**
3. Click on environment to see:
   - Latest deployment
   - Deployment history
   - Active URL

### Deployment URLs

After setup, each environment shows in GitHub:
- **Development**: `http://DEV_SERVER_IP:8080`
- **Production**: `https://YOUR_DOMAIN` (if DOMAIN secret is set)

## Best Practices

### 1. Use Different Servers

✅ **Recommended**: Separate servers for dev and production
- Development: Smaller Mikrus VPS (Mikrus 2.1)
- Production: Larger Mikrus VPS (Mikrus 3.0+)

❌ **Not Recommended**: Same server with different ports
- Harder to isolate
- Resource conflicts
- Security concerns

### 2. Use Different Databases

✅ **Recommended**: Separate database schemas or databases
```yaml
# Development
SPRING_DATASOURCE_URL=jdbc:postgresql://psql01.mikr.us:5432/db_adrian247?currentSchema=shapyfy_dev

# Production
SPRING_DATASOURCE_URL=jdbc:postgresql://psql01.mikr.us:5432/db_adrian247?currentSchema=shapyfy_prod
```

### 3. Use Different Sentry Projects

Create separate Sentry projects:
- `shapyfy-dev` → Development DSN
- `shapyfy-prod` → Production DSN

Benefits:
- Separate error tracking
- Different alert rules
- Clear environment visibility

### 4. Require Approvals for Production

In production environment settings:
- Enable "Required reviewers"
- Add team members who can approve
- Prevents accidental deployments

### 5. Branch Protection

Protect your main branch:
1. **Settings** → **Branches** → **Add rule**
2. Branch name pattern: `main`
3. Enable:
   - ✅ Require pull request reviews
   - ✅ Require status checks to pass (tests)
   - ✅ Require branches to be up to date

## Development Workflow

### Day-to-Day Development

```bash
# Work on feature
git checkout -b feature/new-feature
# ... make changes ...
git commit -m "Add new feature"

# Push to feature branch (no deployment)
git push origin feature/new-feature

# Create PR to kotlin-main
# After review, merge PR

# Auto-deploys to DEVELOPMENT
```

### Promoting to Production

```bash
# After testing in dev, merge to main
git checkout main
git pull origin main
git merge kotlin-main

# Review changes
git log

# Push to production
git push origin main

# Approve deployment in GitHub (if required)
# Auto-deploys to PRODUCTION
```

## Rollback Strategy

### Development Rollback

```bash
# SSH to dev server
ssh -p DEV_PORT root@DEV_SERVER

cd /opt/shapyfy

# Pull specific version by commit SHA
docker pull ghcr.io/YOUR_USERNAME/shapyfy-core:PREVIOUS_COMMIT_SHA

# Update .env to use specific tag
# Or manually tag as latest
docker tag ghcr.io/YOUR_USERNAME/shapyfy-core:PREVIOUS_SHA ghcr.io/YOUR_USERNAME/shapyfy-core:latest

# Restart
docker compose up -d --force-recreate
```

### Production Rollback

Same as dev, but:
1. May need approval in GitHub
2. Consider creating a rollback workflow
3. Document the rollback in Sentry/monitoring

## Troubleshooting

### Wrong Environment Deployed

**Issue**: Production secrets being used in dev

**Solution**:
1. Check branch matches environment configuration
2. Verify environment secrets are set correctly
3. Check workflow `if:` conditions

### Deployment Pending Forever

**Issue**: Waiting for approval that never comes

**Solution**:
1. Check **Environments** → Select environment → See pending deployments
2. Click **Review pending deployments**
3. Select environments to approve
4. Click **Approve and deploy**

### Secrets Not Working in Environment

**Issue**: Empty or wrong values in container

**Solution**:
1. Verify secrets are set in **Environment** secrets, not repository secrets
2. Check secret names match exactly (case-sensitive)
3. SSH to server and check `.env` file contents
4. Check docker compose logs

## Cost Optimization

### Using One Server for Both Environments

If you must use one server:

1. Use different ports:
   ```yaml
   # Development docker-compose.yml
   ports:
     - "8080:8080"

   # Production docker-compose.yml
   ports:
     - "8081:8080"
   ```

2. Use different directories:
   ```bash
   /opt/shapyfy-dev/
   /opt/shapyfy-prod/
   ```

3. Different container names:
   ```yaml
   container_name: shapyfy-core-dev
   container_name: shapyfy-core-prod
   ```

But this is **NOT recommended** for production.

## Advanced: Staging Environment

### Create Staging Branch

```bash
git checkout -b staging
git push origin staging
```

### Update Workflow

Add staging deployment job to `.github/workflows/deploy.yml`:

```yaml
deploy-staging:
  name: Deploy to Staging
  needs: build-and-push
  runs-on: ubuntu-latest
  if: github.ref == 'refs/heads/staging'
  environment:
    name: staging
    url: http://${{ secrets.SERVER_HOST }}:8080
  # ... similar to dev/prod
```

### Workflow with Staging

```
Feature Branch → kotlin-main (dev) → staging → main (production)
                     ↓                  ↓           ↓
                   Dev Server      Staging      Production
```

## Next Steps

1. ✅ Create GitHub environments
2. ✅ Add environment-specific secrets
3. ✅ Configure production approvals
4. ✅ Create Spring profile configs (optional)
5. ✅ Test deployment to dev
6. ✅ Test deployment to production
7. Set up monitoring per environment
8. Configure separate Sentry projects
9. Document environment URLs for team

## Summary

### You Now Have:

- ✅ **Separate environments** (dev, production)
- ✅ **Environment-specific secrets**
- ✅ **Automatic deployments** based on branch
- ✅ **Optional production approvals**
- ✅ **Spring profile support**
- ✅ **Sentry environment tracking**
- ✅ **Clean separation of concerns**

### Next Push:

```bash
# Push to kotlin-main → Deploys to DEV
git push origin kotlin-main

# Push to main → Deploys to PRODUCTION (with approval)
git push origin main
```

That's it! Professional multi-environment setup complete! 🚀
