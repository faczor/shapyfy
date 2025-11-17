# Docker Deployment Guide (Using Jib)

This guide covers deploying Shapyfy Core to Mikrus Ubuntu VPS using Docker and Jib for containerization.

## Why Jib?

Jib offers several advantages over traditional Dockerfile builds:
- **No Docker daemon required** - builds directly from Gradle
- **Faster builds** - optimized layer caching (dependencies separate from code)
- **Reproducible builds** - consistent image creation
- **Better performance** - parallel layer uploads
- **Optimized for Java** - automatically configures JVM for containers

## Prerequisites

- Mikrus VPS with Ubuntu installed
- SSH access to your server
- Docker installed on the server (NOT required locally!)

## Part 1: Server Setup

### 1. Connect to Your Server

```bash
ssh root@your-mikrus-server-ip -p YOUR_SSH_PORT
```

### 2. Install Docker on Ubuntu Server

```bash
# Update package index
apt update

# Install prerequisites
apt install -y ca-certificates curl gnupg lsb-release

# Add Docker's official GPG key
mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Set up the repository
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker Engine
apt update
apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Verify installation
docker --version
docker compose version

# Enable Docker to start on boot
systemctl enable docker
systemctl start docker
```

### 3. Create Application Directory

```bash
mkdir -p /opt/shapyfy
cd /opt/shapyfy
```

### 4. Create Environment File

Create `/opt/shapyfy/.env`:

```bash
cat > /opt/shapyfy/.env << 'EOF'
# Database configuration
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

# Sentry configuration
SENTRY_DSN=your_sentry_dsn

# Application version (optional)
APP_VERSION=0.0.1-SNAPSHOT
EOF
```

**IMPORTANT**: Update with your actual credentials.

Set secure permissions:
```bash
chmod 600 /opt/shapyfy/.env
```

## Part 2: Building and Deploying with Jib

### Option A: Build to Docker Daemon (Local Development)

From your development machine:

```bash
# Build and load image into local Docker daemon
./gradlew jibDockerBuild

# Verify the image
docker images | grep shapyfy

# Save image to tar file
docker save shapyfy/core:latest | gzip > shapyfy-core.tar.gz

# Transfer to server
scp -P YOUR_SSH_PORT shapyfy-core.tar.gz root@your-mikrus-server-ip:/opt/shapyfy/

# On the server, load the image
ssh root@your-mikrus-server-ip -p YOUR_SSH_PORT
cd /opt/shapyfy
docker load < shapyfy-core.tar.gz
```

### Option B: Build to Docker Registry (Recommended for Production)

#### Using Docker Hub:

```bash
# Login to Docker Hub (one-time setup)
docker login

# Build and push to Docker Hub
./gradlew jib -Djib.to.image=your-dockerhub-username/shapyfy-core:latest

# On server, pull the image
docker pull your-dockerhub-username/shapyfy-core:latest
```

#### Using GitHub Container Registry:

```bash
# Create GitHub Personal Access Token with 'write:packages' permission
# Login to GitHub Container Registry
echo $GITHUB_TOKEN | docker login ghcr.io -u YOUR_GITHUB_USERNAME --password-stdin

# Build and push
./gradlew jib -Djib.to.image=ghcr.io/YOUR_GITHUB_USERNAME/shapyfy-core:latest

# On server, pull the image
docker pull ghcr.io/YOUR_GITHUB_USERNAME/shapyfy-core:latest
```

### Option C: Build Directly to Tar (No Registry)

```bash
# Build to tar file
./gradlew jibBuildTar

# The tar will be in build/jib-image.tar
# Transfer to server
scp -P YOUR_SSH_PORT build/jib-image.tar root@your-mikrus-server-ip:/opt/shapyfy/

# On server, load the image
docker load --input /opt/shapyfy/jib-image.tar
```

## Part 3: Running with Docker Compose

### 1. Create docker-compose.yml on Server

```bash
cat > /opt/shapyfy/docker-compose.yml << 'EOF'
version: '3.8'

services:
  shapyfy-core:
    image: shapyfy/core:latest  # Update if using registry
    container_name: shapyfy-core
    restart: unless-stopped
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=production
      - SPRING_DATASOURCE_URL=jdbc:postgresql://psql01.mikr.us:5432/db_adrian247?currentSchema=shapyfy
      - SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
      - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
      - SENTRY_DSN=${SENTRY_DSN}
      - SENTRY_ENVIRONMENT=production
    env_file:
      - .env
    networks:
      - shapyfy-network
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 3s
      retries: 3
      start_period: 60s

networks:
  shapyfy-network:
    driver: bridge
EOF
```

### 2. Start the Application

```bash
cd /opt/shapyfy

# Start in detached mode
docker compose up -d

# Check status
docker compose ps

# View logs
docker compose logs -f

# Check health
curl http://localhost:8080/actuator/health
```

## Part 4: Nginx Reverse Proxy (Optional but Recommended)

### 1. Install Nginx

```bash
apt install -y nginx
```

### 2. Configure Nginx

```bash
cat > /etc/nginx/sites-available/shapyfy << 'EOF'
server {
    listen 80;
    server_name your-domain.com;  # Or your server IP

    # Increase timeouts
    proxy_read_timeout 300s;
    proxy_connect_timeout 300s;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Health check endpoint
    location /actuator/health {
        proxy_pass http://localhost:8080/actuator/health;
        access_log off;
    }
}
EOF

# Enable site
ln -s /etc/nginx/sites-available/shapyfy /etc/nginx/sites-enabled/
rm -f /etc/nginx/sites-enabled/default

# Test and reload
nginx -t
systemctl reload nginx
```

## Part 5: Systemd Service for Docker Compose (Auto-restart)

Create `/etc/systemd/system/shapyfy-docker.service`:

```bash
cat > /etc/systemd/system/shapyfy-docker.service << 'EOF'
[Unit]
Description=Shapyfy Core Docker Container
Requires=docker.service
After=docker.service

[Service]
Type=oneshot
RemainAfterExit=yes
WorkingDirectory=/opt/shapyfy
ExecStart=/usr/bin/docker compose up -d
ExecStop=/usr/bin/docker compose down
TimeoutStartSec=0

[Install]
WantedBy=multi-user.target
EOF

# Enable and start
systemctl daemon-reload
systemctl enable shapyfy-docker
systemctl start shapyfy-docker
```

## Deployment Updates

### Quick Update Process

1. **Build new image locally:**
   ```bash
   # On your development machine
   ./gradlew jibDockerBuild
   docker save shapyfy/core:latest | gzip > shapyfy-core.tar.gz
   scp -P YOUR_SSH_PORT shapyfy-core.tar.gz root@your-server:/opt/shapyfy/
   ```

2. **Deploy on server:**
   ```bash
   # SSH to server
   cd /opt/shapyfy

   # Load new image
   docker load < shapyfy-core.tar.gz

   # Recreate container with new image
   docker compose up -d --force-recreate

   # View logs
   docker compose logs -f
   ```

### Using Registry (Recommended)

```bash
# On development machine - push to registry
./gradlew jib -Djib.to.image=your-username/shapyfy-core:latest

# On server - pull and restart
cd /opt/shapyfy
docker compose pull
docker compose up -d
docker compose logs -f
```

## Jib-Specific Commands

### Build Commands

```bash
# Build to Docker daemon (requires Docker installed)
./gradlew jibDockerBuild

# Build and push to registry
./gradlew jib

# Build to tar file
./gradlew jibBuildTar

# Custom image name
./gradlew jib -Djib.to.image=myregistry.com/shapyfy:v1.0.0

# Skip tests during build
./gradlew jib -x test
```

### Configuration Overrides

```bash
# Override JVM flags
./gradlew jib -Djib.container.jvmFlags=-Xmx1g,-Xms512m

# Override base image
./gradlew jib -Djib.from.image=eclipse-temurin:21-jre-jammy

# Set environment variables
./gradlew jib -Djib.container.environment=SPRING_PROFILES_ACTIVE=production
```

## Management Commands

### Docker Compose Commands

```bash
# Start services
docker compose up -d

# Stop services
docker compose down

# Restart services
docker compose restart

# View logs
docker compose logs -f

# View logs for specific time
docker compose logs --since 30m

# Check status
docker compose ps

# Execute command in running container
docker compose exec shapyfy-core sh

# View resource usage
docker stats
```

### Docker Commands

```bash
# List running containers
docker ps

# List all containers
docker ps -a

# List images
docker images

# Remove old images
docker image prune -a

# View container logs
docker logs shapyfy-core -f

# Execute shell in container
docker exec -it shapyfy-core sh

# Inspect container
docker inspect shapyfy-core

# View container resource usage
docker stats shapyfy-core
```

## Monitoring and Troubleshooting

### Health Checks

```bash
# Check application health
curl http://localhost:8080/actuator/health

# Check Docker container health
docker inspect --format='{{.State.Health.Status}}' shapyfy-core

# View health check logs
docker inspect --format='{{range .State.Health.Log}}{{.Output}}{{end}}' shapyfy-core
```

### Common Issues

#### Container Won't Start

```bash
# Check logs
docker compose logs

# Check if port is in use
netstat -tulpn | grep 8080

# Check container status
docker compose ps
```

#### Database Connection Issues

```bash
# Test database connectivity from container
docker compose exec shapyfy-core sh -c "nc -zv psql01.mikr.us 5432"

# Check environment variables
docker compose exec shapyfy-core env | grep SPRING
```

#### Memory Issues

```bash
# Check container memory usage
docker stats shapyfy-core

# View container resource limits
docker inspect shapyfy-core | grep -A 5 Memory

# Reduce JVM heap in docker-compose.yml
# Add to environment section:
# - JAVA_OPTS=-Xmx256m -Xms128m
```

#### Image Pull Issues

```bash
# Check registry credentials
docker login

# Manual pull
docker pull shapyfy/core:latest

# Check available space
df -h
```

## Backup and Restore

### Backup Container Data

```bash
# Export container
docker export shapyfy-core > shapyfy-backup.tar

# Save image
docker save shapyfy/core:latest > shapyfy-image.tar
```

### Restore from Backup

```bash
# Load image
docker load < shapyfy-image.tar

# Restart container
docker compose up -d
```

## Security Best Practices

1. **Run as non-root user** - Already configured in Jib (user 1000:1000)
2. **Limit resources** - Configure memory/CPU limits in docker-compose.yml
3. **Regular updates** - Keep base image updated
4. **Scan images** - Use Docker Scout or Trivy
5. **Network isolation** - Use Docker networks
6. **Secrets management** - Use `.env` file with proper permissions (600)

### Resource Limits Example

```yaml
services:
  shapyfy-core:
    # ... other config ...
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 768M
        reservations:
          cpus: '0.5'
          memory: 512M
```

## Advantages of Jib Over Dockerfile

1. **No Docker daemon required locally** - Build from CI/CD without Docker
2. **Faster builds** - Smart layer caching (dependencies vs. code)
3. **Reproducible** - Same inputs = same image hash
4. **Security** - Automatic distroless support, runs as non-root
5. **Integration** - Native Gradle/Maven integration
6. **Performance** - Parallel layer uploads
7. **Simplicity** - No Dockerfile to maintain

## Performance Tips

1. **Use specific tags** - Don't rely only on `latest`
2. **Clean old images** - `docker image prune -a --force`
3. **Monitor resources** - `docker stats`
4. **Use Alpine base** - Smaller image size (configured by default)
5. **Layer optimization** - Jib handles this automatically

## Mikrus-Specific Notes

- **Port Forwarding**: Remember Mikrus IPv4 port forwarding limitations
- **IPv6**: Consider using IPv6 for HTTP access
- **Resources**: Minimum Mikrus 2.1 (1GB RAM) recommended for Docker
- **Storage**: Monitor disk usage - Docker images consume space

## CI/CD Integration Example

```bash
# In GitHub Actions or similar
- name: Build and push with Jib
  run: |
    ./gradlew jib \
      -Djib.to.image=ghcr.io/your-username/shapyfy-core:${{ github.sha }} \
      -Djib.to.auth.username=${{ github.actor }} \
      -Djib.to.auth.password=${{ secrets.GITHUB_TOKEN }}
```

## Next Steps

1. Set up automated deployments (GitHub Actions)
2. Configure log aggregation (e.g., ELK stack)
3. Add monitoring (Prometheus + Grafana)
4. Set up SSL certificates (Let's Encrypt)
5. Configure automated backups

## Support

- Jib documentation: https://github.com/GoogleContainerTools/jib
- Docker documentation: https://docs.docker.com
- Mikrus support: https://mikr.us
