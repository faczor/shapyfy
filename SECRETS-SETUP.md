# GitHub Secrets Setup Guide

This guide shows you how to configure all secrets needed for CI/CD deployment.

## Required GitHub Secrets

Go to your GitHub repository → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

You need to add **7 secrets total**:

### Server/SSH Secrets (4)

| Secret Name | Description | Example Value |
|------------|-------------|---------------|
| `SERVER_HOST` | Your Mikrus server IP address | `123.45.67.89` |
| `SERVER_USER` | SSH username (usually root) | `root` |
| `SERVER_PORT` | Your Mikrus SSH port number | `12345` |
| `SERVER_SSH_KEY` | Your SSH private key (full content) | See below |

### Application Secrets (3)

| Secret Name | Description | Example Value |
|------------|-------------|---------------|
| `DB_USERNAME` | PostgreSQL database username | `db_adrian247` |
| `DB_PASSWORD` | PostgreSQL database password | `your_secure_password` |
| `SENTRY_DSN` | Sentry error tracking DSN | `https://abc123@o123.ingest.sentry.io/456` |

## Step-by-Step Setup

### 1. Server Secrets

#### Get Server Details

You should have received these from Mikrus:
- IP address (SERVER_HOST)
- SSH port (SERVER_PORT)
- Username is typically `root` (SERVER_USER)

#### Generate SSH Key for GitHub Actions

On your local machine:

```bash
# Generate a new SSH key specifically for GitHub Actions
ssh-keygen -t ed25519 -C "github-actions-deploy" -f ~/.ssh/github-actions-shapyfy

# When prompted for passphrase, leave empty (just press Enter)
```

This creates two files:
- `~/.ssh/github-actions-shapyfy` (private key) ← This goes to GitHub
- `~/.ssh/github-actions-shapyfy.pub` (public key) ← This goes to server

#### Copy Public Key to Server

```bash
# Copy the public key to your server
ssh-copy-id -i ~/.ssh/github-actions-shapyfy.pub -p YOUR_SSH_PORT root@YOUR_SERVER_IP

# Test the connection
ssh -i ~/.ssh/github-actions-shapyfy -p YOUR_SSH_PORT root@YOUR_SERVER_IP
```

#### Get Private Key for GitHub Secret

```bash
# Display the private key
cat ~/.ssh/github-actions-shapyfy
```

Copy the **ENTIRE output**, including:
- `-----BEGIN OPENSSH PRIVATE KEY-----`
- All the lines in between
- `-----END OPENSSH PRIVATE KEY-----`

This entire block goes into the `SERVER_SSH_KEY` secret.

### 2. Application Secrets

#### Database Credentials

You should have these from your PostgreSQL setup:
- `DB_USERNAME` - Your database username
- `DB_PASSWORD` - Your database password

These are the same credentials you use in your local `application.yaml`.

#### Sentry DSN

1. Log in to [Sentry](https://sentry.io)
2. Go to your project
3. Go to **Settings** → **Projects** → Select your project
4. Go to **Client Keys (DSN)**
5. Copy the DSN URL (looks like `https://abc123@o123.ingest.sentry.io/456`)

## Adding Secrets to GitHub

### For Each Secret:

1. Go to your repository on GitHub
2. Click **Settings** (top menu)
3. In the left sidebar, click **Secrets and variables** → **Actions**
4. Click **New repository secret**
5. Enter the secret name (exactly as shown above, case-sensitive)
6. Paste the secret value
7. Click **Add secret**

### Visual Guide:

```
Your Repo → Settings → Secrets and variables → Actions → New repository secret

Name: SERVER_HOST
Value: 123.45.67.89
[Add secret]

Name: SERVER_PORT
Value: 12345
[Add secret]

Name: SERVER_USER
Value: root
[Add secret]

Name: SERVER_SSH_KEY
Value: -----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZW
... (all lines)
-----END OPENSSH PRIVATE KEY-----
[Add secret]

Name: DB_USERNAME
Value: db_adrian247
[Add secret]

Name: DB_PASSWORD
Value: your_password_here
[Add secret]

Name: SENTRY_DSN
Value: https://abc123@o123.ingest.sentry.io/456
[Add secret]
```

## Verification Checklist

After adding all secrets, verify you have all 7:

- [ ] `SERVER_HOST`
- [ ] `SERVER_PORT`
- [ ] `SERVER_USER`
- [ ] `SERVER_SSH_KEY`
- [ ] `DB_USERNAME`
- [ ] `DB_PASSWORD`
- [ ] `SENTRY_DSN`

## How Secrets Are Used

### In GitHub Actions Workflow:

```yaml
# SSH secrets are used to connect to your server
- uses: appleboy/ssh-action@v1.0.3
  with:
    host: ${{ secrets.SERVER_HOST }}
    username: ${{ secrets.SERVER_USER }}
    key: ${{ secrets.SERVER_SSH_KEY }}
    port: ${{ secrets.SERVER_PORT }}

# Application secrets are passed to the server
env:
  DB_USERNAME: ${{ secrets.DB_USERNAME }}
  DB_PASSWORD: ${{ secrets.DB_PASSWORD }}
  SENTRY_DSN: ${{ secrets.SENTRY_DSN }}
```

### On the Server:

The workflow creates a `.env` file on the server with these secrets:

```bash
# This happens automatically during deployment
cat > .env << EOF
DB_USERNAME=${DB_USERNAME}
DB_PASSWORD=${DB_PASSWORD}
SENTRY_DSN=${SENTRY_DSN}
GITHUB_REPOSITORY=${GITHUB_REPOSITORY}
EOF
```

### In Docker Container:

Docker Compose reads the `.env` file and passes them to your application:

```yaml
environment:
  - SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
  - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
  - SENTRY_DSN=${SENTRY_DSN}
env_file:
  - .env
```

## Security Best Practices

### ✅ DO:
- Use separate SSH key for GitHub Actions (not your personal key)
- Use strong, unique passwords for database
- Rotate secrets regularly (especially after team member changes)
- Review who has access to repository secrets
- Enable 2FA on your GitHub account

### ❌ DON'T:
- Never commit secrets to git
- Never share secrets via chat/email
- Never use the same password across services
- Never give SSH key a passphrase (GitHub Actions can't enter it)
- Never reuse your personal SSH key for automation

## Troubleshooting

### Secret Not Working

**Issue:** Workflow fails with "secret not found" or empty value

**Solution:**
1. Check secret name is exactly correct (case-sensitive)
2. Verify secret has a value (re-save if needed)
3. Make sure you're on the correct repository
4. Check you added it under "Actions" not "Codespaces" or "Dependabot"

### SSH Connection Fails

**Issue:** `Permission denied (publickey)`

**Solution:**
1. Verify public key is on server: `cat ~/.ssh/authorized_keys` on server
2. Check private key in GitHub secret is complete (including header/footer)
3. Verify SERVER_PORT is correct
4. Test SSH connection manually with the key

**Issue:** `Connection refused`

**Solution:**
1. Verify SERVER_HOST is correct
2. Verify SERVER_PORT is correct
3. Check if SSH service is running on server: `systemctl status ssh`
4. Check firewall allows the SSH port

### Database Connection Fails

**Issue:** Application can't connect to database

**Solution:**
1. Verify DB_USERNAME and DB_PASSWORD are correct
2. Check if database allows connections from your server IP
3. Test connection from server: `psql -h psql01.mikr.us -p 5432 -U $DB_USERNAME -d db_adrian247`
4. Check Docker logs: `docker compose logs`

### Sentry Not Receiving Events

**Issue:** Errors not appearing in Sentry

**Solution:**
1. Verify SENTRY_DSN is correct and complete
2. Check Sentry project is active
3. Test locally with same DSN
4. Check application logs for Sentry initialization errors

## Updating Secrets

To update a secret:

1. Go to repository → Settings → Secrets and variables → Actions
2. Click on the secret name
3. Click **Update secret**
4. Enter new value
5. Click **Update secret**

The new value will be used in the next workflow run.

## Environment-Specific Secrets (Advanced)

If you want separate staging and production environments:

### Staging Secrets:
- `STAGING_SERVER_HOST`
- `STAGING_DB_USERNAME`
- `STAGING_DB_PASSWORD`
- etc.

### Production Secrets:
- `PRODUCTION_SERVER_HOST`
- `PRODUCTION_DB_USERNAME`
- `PRODUCTION_DB_PASSWORD`
- etc.

Then create separate workflows that use the appropriate secrets.

## Next Steps

After setting up all secrets:

1. ✅ Verify all 7 secrets are added
2. ✅ Test SSH connection manually
3. ✅ Push code to trigger workflow
4. ✅ Monitor GitHub Actions run
5. ✅ Verify deployment on server

## Support

- GitHub Secrets docs: https://docs.github.com/en/actions/security-guides/encrypted-secrets
- SSH key setup: https://docs.github.com/en/authentication/connecting-to-github-with-ssh
