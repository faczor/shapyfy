@.github/workflows/deploy.yml
@.github/workflows/test.yml
@.github/QUICK-START.md
@.github/secrets.md
@.github/SETUP.md
@docker-compose.yml

## Deployment Configuration

### Server: Mikrus (srv35.mikr.us)
Development server hosted on Mikrus VPS (adrian247.mikrus.xyz / srv35.mikr.us)

### Port Configuration
**External Port Used:** `40086` (srv35.mikr.us:40086)
**Internal Port:** `8080` (Spring Boot default)

**Port Mapping:** `40086:8080` (external:internal)
- Application runs on port 8080 inside the Docker container
- External traffic to port 40086 is routed to the container's port 8080
- Public API accessible at: http://srv35.mikr.us:40086

**Available Mikrus Ports:**
The following external ports are allocated by Mikrus and available for use:
- `20247` - SSH (srv35.mikr.us:20247) - Used for server access
- `30247` - Available (srv35.mikr.us:30247)
- `40086` - **IN USE** for Shapyfy Core API (srv35.mikr.us:40086)
- `40087` - Available (srv35.mikr.us:40087)
- `40088` - Available (srv35.mikr.us:40088)

**Why port 40086?**
- First available port in the 400XX range (40086, 40087, 40088)
- Ports 20247 and 30247 reserved for other services (SSH, etc.)
- Allows room for future services on 40087 and 40088

**Note:** The port mapping in docker-compose.yml must match one of the allocated Mikrus external ports. The internal port (8080) is defined in build.gradle's Jib configuration and should not be changed unless Spring Boot's server.port is also updated.

### Important: Manual docker-compose.yml Deployment

**CRITICAL:** The GitHub Actions workflow (.github/workflows/deploy.yml) does NOT automatically copy docker-compose.yml to the server. It only:
1. Builds and pushes the Docker image
2. Pulls the latest image on the server
3. Runs `docker compose up -d` with the EXISTING docker-compose.yml on the server

**If you modify docker-compose.yml, you MUST manually copy it to the server:**

```bash
# Copy updated docker-compose.yml to server
scp -P 10247 docker-compose.yml root@adrian247.mikrus.xyz:/opt/shapyfy/

# SSH to server and restart
ssh root@adrian247.mikrus.xyz -p 10247
cd /opt/shapyfy
docker compose down
docker compose up -d
docker ps  # Verify the changes
```

**Without this manual step, your docker-compose.yml changes will NOT be applied on the server, even after a successful GitHub Actions deployment.**

**TODO:** Consider updating the deploy.yml workflow to automatically copy docker-compose.yml during deployment.