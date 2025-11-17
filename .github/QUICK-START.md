# Quick Start - Dev Deployment

**5-minute setup guide for deploying Shapyfy to dev server**

---

## Prerequisites Checklist

Before you start, have these ready:

- [ ] GitHub repository: `faczor/shapyfy`
- [ ] Dev server: `adrian247.mikrus.xyz:10247`
- [ ] Database credentials (username and password)
- [ ] SSH access to server

---

## Step 1: Generate SSH Key (2 minutes)

```bash
# Generate key
ssh-keygen -t ed25519 -C "github-actions-dev" -f ~/.ssh/shapyfy-dev
# Press Enter 3 times (no passphrase)

# Get PUBLIC key for Mikrus panel
cat ~/.ssh/shapyfy-dev.pub
```

**Copy the output** (one line starting with `ssh-ed25519`) and paste it into:
- Mikrus panel → SSH key management

**Then get PRIVATE key for GitHub (save for Step 3):**

```bash
cat ~/.ssh/shapyfy-dev
```

Keep this output - you'll paste it into GitHub in Step 3.

---

## Step 2: Create GitHub Environment (2 minutes)

1. Go to: https://github.com/faczor/shapyfy/settings/environments
2. Click **"New environment"**
3. Name: `dev`
4. **Deployment branches**: Select "kotlin-main"
5. No protection rules needed
6. Click **"Configure environment"**

---

## Step 3: Add Secrets (1 minute)

In the `dev` environment, click **"Add secret"** for each:

**Server Connection:**
| Name | Value |
|------|-------|
| `SERVER_HOST` | `adrian247.mikrus.xyz` |
| `SERVER_PORT` | `10247` |
| `SERVER_USER` | `root` |
| `SERVER_SSH_KEY` | (paste from `cat ~/.ssh/shapyfy-dev`) |

**Database:**
| Name | Value |
|------|-------|
| `DB_SERVER` | `psql01.mikr.us:5432` |
| `DB_NAME` | `db_adrian247` |
| `DB_SCHEMA` | `shapyfy` |
| `DB_USERNAME` | your database username |
| `DB_PASSWORD` | your database password |

**Firebase & API:**
| Name | Value |
|------|-------|
| `FIREBASE_PROJECT_ID` | `shapyfy-dev` |
| `FIREBASE_JWK_SET_URI` | `https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com` |
| `ANTHROPIC_API_KEY` | your anthropic API key |

**Optional:**
| Name | Value |
|------|-------|
| `SENTRY_DSN` | (leave empty for now) |

---

## Step 4: Prepare Server (3 minutes)

```bash
# SSH to server
ssh root@adrian247.mikrus.xyz -p 10247

# Install Docker (if not installed)
curl -fsSL https://get.docker.com | sh

# Create app directory
mkdir -p /opt/shapyfy
cd /opt/shapyfy

# Exit server
exit

# Copy docker-compose.yml from local machine
scp -P 10247 docker-compose.yml root@adrian247.mikrus.xyz:/opt/shapyfy/
```

---

## Step 5: Deploy! (30 seconds)

```bash
# On your local machine
git checkout kotlin-main
git add .
git commit -m "Configure CI/CD for dev"
git push origin kotlin-main
```

Watch deployment: https://github.com/faczor/shapyfy/actions

---

## Step 6: Make Package Public (30 seconds)

After first successful deployment:

1. Go to: https://github.com/faczor/shapyfy/pkgs/container/shapyfy
2. Click **"Package settings"**
3. Scroll to "Danger Zone"
4. Click **"Change visibility"** → **"Public"**
5. Confirm by typing: `shapyfy`

---

## Verify Deployment

```bash
# SSH to server
ssh root@adrian247.mikrus.xyz -p 10247

# Check container
docker ps | grep shapyfy-core

# Check health
curl http://localhost:8080/actuator/health
# Should return: {"status":"UP"}
```

---

## Done! 🎉

**What you now have:**
- ✅ Automated testing on every push to kotlin-main
- ✅ Docker image built with Jib
- ✅ Auto-deployment to dev server
- ✅ Health checks after deployment

**Every time you push to `kotlin-main`:**
1. Tests run automatically
2. Docker image builds
3. Deploys to dev server
4. Runs health checks

---

## Quick Commands Reference

```bash
# View logs on server
ssh root@adrian247.mikrus.xyz -p 10247
cd /opt/shapyfy
docker compose logs -f

# Restart application
docker compose restart

# Check container status
docker compose ps

# View GitHub Actions
open https://github.com/faczor/shapyfy/actions
```

---

## Troubleshooting

**Build fails?**
- Check workflow logs: https://github.com/faczor/shapyfy/actions

**Deployment fails?**
- Verify all 14 secrets are set in GitHub `dev` environment
- Check SSH key is correct (including BEGIN/END lines)

**Container won't start?**
- SSH to server and check logs: `docker compose logs`
- Verify database credentials

**Image pull fails?**
- Make package public (see Step 6)

**Need detailed help?**
- See [SETUP.md](SETUP.md) for full step-by-step guide
- See [secrets.md](secrets.md) for secrets reference

---

**Ready to deploy?** Start with Step 1! 🚀
