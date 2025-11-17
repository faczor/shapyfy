# Environment Configuration Guide

This project uses Spring Boot profiles to manage different environments. Configuration is split across multiple files for clarity and security.

## 🌍 Available Environments

| Profile | Use Case | Configuration File | Secrets Source |
|---------|----------|-------------------|----------------|
| **local** | Local development on your machine | `application-local.yaml` | `.env` file |
| **dev** | Dev server (Docker on Mikrus) | `application-dev.yaml` | Docker env vars |

## 📁 Configuration Files

### Base Configuration
**`application.yaml`** - Common settings shared across all environments:
- Application name
- Flyway migrations
- Security settings
- Actuator endpoints
- Server port

### Local Development
**`application-local.yaml`** - Local development settings:
- Database connection (from `.env`)
- SQL logging enabled
- Firebase test project
- Sentry disabled for local

### Dev Server
**`application-dev.yaml`** - Dev server settings:
- Database connection (from Docker env vars)
- SQL logging disabled
- Connection pooling optimized
- Sentry enabled with full tracing
- Debug logging for app code

## 🚀 Local Development Setup

### 1. Create `.env` File

Copy the example file and fill in your values:

```bash
cp .env.example .env
```

Edit `.env` with your actual credentials:

```env
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://psql01.mikr.us:5432/db_adrian247?currentSchema=shapyfy
SPRING_DATASOURCE_USERNAME=adrian247
SPRING_DATASOURCE_PASSWORD=your_password_here

# Optional: Firebase
FIREBASE_PROJECT_ID=shapyfy-dev

# Optional: Anthropic API
ANTHROPIC_API_KEY=your_key_here

# Optional: Sentry
SENTRY_DSN=your_sentry_dsn
```

**IMPORTANT:** `.env` is gitignored and should NEVER be committed!

### 2. Run Locally

**Using Gradle:**
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

**Using IntelliJ IDEA:**
1. Edit Run Configuration
2. Add VM options: `-Dspring.profiles.active=local`
3. Or set environment variable: `SPRING_PROFILES_ACTIVE=local`

**Using JAR:**
```bash
./gradlew bootJar
java -jar build/libs/core-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

### 3. Verify Configuration

Check the startup logs - you should see:
```
The following 1 profile is active: "local"
```

## 🐳 Dev Server (Docker)

The dev server runs in Docker and uses environment variables from:
1. GitHub Actions secrets (injected during deployment)
2. `.env` file on server (created by GitHub Actions)
3. `docker-compose.yml` environment section

**Profile:** `dev` (set automatically by deployment workflow)

### Environment Variables on Server

The deployment workflow creates `/opt/shapyfy/.env` with:
```env
DB_USERNAME=value_from_github_secret
DB_PASSWORD=value_from_github_secret
SENTRY_DSN=value_from_github_secret
SENTRY_ENVIRONMENT=development
GITHUB_REPOSITORY=faczor/shapyfy
SPRING_PROFILES_ACTIVE=dev
```

## 🔍 How It Works

### Local Development
```
1. Spring Boot starts with profile=local
2. Loads application.yaml (base config)
3. Loads application-local.yaml (overrides)
4. Reads .env file automatically
5. Replaces ${SPRING_DATASOURCE_USERNAME} with value from .env
```

### Dev Server
```
1. Docker Compose starts container
2. Loads /opt/shapyfy/.env file
3. Sets environment variables in container
4. Spring Boot starts with profile=dev (from env var)
5. Loads application.yaml (base config)
6. Loads application-dev.yaml (overrides)
7. Replaces ${SPRING_DATASOURCE_USERNAME} with value from env var
```

## 🔐 Security Best Practices

### ✅ DO:
- Use `.env` file for local development secrets
- Keep `.env` in `.gitignore`
- Use `.env.example` to document required variables
- Use GitHub Secrets for dev server
- Use different passwords for each environment

### ❌ DON'T:
- Commit `.env` file to git
- Hardcode secrets in `application.yaml`
- Share `.env` file via Slack/email
- Use the same password across environments

## 🧪 Testing Profiles

You can verify which configuration is loaded:

```bash
# Check active profile
curl http://localhost:8080/actuator/info

# Check health
curl http://localhost:8080/actuator/health
```

## 📊 Profile-Specific Logging

**Local (`local` profile):**
- Human-readable console output
- SQL queries visible
- Debug level for `com.shapyfy` package

**Dev Server (`dev` profile):**
- JSON console output (for log aggregation)
- SQL queries hidden
- Debug level for `com.shapyfy` package
- Info level for Spring

## 🔄 Switching Profiles

**During development:**
```bash
# Run with local profile
./gradlew bootRun --args='--spring.profiles.active=local'

# Run with dev profile (to test dev config locally)
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**In Docker:**
Set in `docker-compose.yml` or `.env`:
```yaml
environment:
  - SPRING_PROFILES_ACTIVE=dev
```

## 🐛 Troubleshooting

### "The server requested password-based authentication, but no password was provided"
- Check `.env` file exists and has `SPRING_DATASOURCE_PASSWORD`
- Verify profile is `local` (check startup logs)
- Ensure no spaces around `=` in `.env` file

### "Cannot find application.yaml"
- Ensure you're running from project root
- Check `src/main/resources/` contains yaml files

### "Profile 'local' not recognized"
- Verify `application-local.yaml` exists in `src/main/resources/`
- Check file name exactly matches (case-sensitive)

## 📚 References

- [Spring Boot Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html)
- [Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html)
- [.env Files in Spring Boot](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.files)

## 🎯 Quick Reference

| Task | Command |
|------|---------|
| Run locally | `./gradlew bootRun --args='--spring.profiles.active=local'` |
| Check active profile | Look for "The following 1 profile is active" in logs |
| Edit local config | Edit `application-local.yaml` |
| Edit dev config | Edit `application-dev.yaml` |
| Add secret to local | Add to `.env` file |
| Add secret to dev | Add to GitHub dev environment secrets |
