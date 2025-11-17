# Two-Server Logging Setup Guide

This guide explains how to deploy Shapyfy Core across **two separate servers** for better isolation and scalability.

## Architecture

```
┌─────────────────────────────┐         ┌─────────────────────────────┐
│   SERVER 1 (Application)    │         │   SERVER 2 (Logging)        │
│                              │         │                              │
│  ┌────────────────────────┐ │         │  ┌────────────────────────┐ │
│  │  Spring Boot App       │ │         │  │  Loki                  │ │
│  │  (Port 8080)           │ │         │  │  (Port 3100)           │ │
│  └────────────────────────┘ │         │  └────────────────────────┘ │
│             │                │         │             ▲                │
│             ▼                │         │             │                │
│  ┌────────────────────────┐ │         │  ┌────────────────────────┐ │
│  │  Log Files (JSON)      │ │         │  │  Grafana               │ │
│  │  logs/*.log            │ │         │  │  (Port 3000)           │ │
│  └────────────────────────┘ │         │  └────────────────────────┘ │
│             │                │         │             ▲                │
│             ▼                │         │             │                │
│  ┌────────────────────────┐ │  HTTP   │             │                │
│  │  Promtail              │─┼─────────┼─────────────┘                │
│  │  (Ships logs)          │ │         │                              │
│  └────────────────────────┘ │         │  Access from your browser:   │
│                              │         │  http://server2-ip:3000      │
└─────────────────────────────┘         └─────────────────────────────┘
```

## Benefits of Two-Server Setup

✅ **Isolation**: Application and logging infrastructure are independent
✅ **Reliability**: If app crashes, logs remain accessible
✅ **Scalability**: Can add more application servers, all sending to one Loki
✅ **Performance**: No resource contention
✅ **Security**: Can restrict access to logging server
✅ **Production-Ready**: Industry standard architecture

## Prerequisites

- **Server 1**: Your application server (4GB RAM, running Kotlin backend)
- **Server 2**: Your logging server (4GB RAM, dedicated to Grafana + Loki)
- Network connectivity between servers
- Open ports: 3100 (Loki), 3000 (Grafana)

## Setup Instructions

### Step 1: Set Up LOGGING SERVER (Server 2)

#### 1.1 Copy Configuration Files

SSH into **Server 2** and create the directory structure:

```bash
mkdir -p ~/shapyfy-logging
cd ~/shapyfy-logging
mkdir -p config/loki config/grafana/provisioning/datasources
```

#### 1.2 Create Loki Config

Create `config/loki/loki-config.yaml`:

```yaml
auth_enabled: false

server:
  http_listen_port: 3100
  grpc_listen_port: 9096

common:
  instance_addr: 127.0.0.1
  path_prefix: /loki
  storage:
    filesystem:
      chunks_directory: /loki/chunks
      rules_directory: /loki/rules
  replication_factor: 1
  ring:
    kvstore:
      store: inmemory

schema_config:
  configs:
    - from: 2020-10-24
      store: boltdb-shipper
      object_store: filesystem
      schema: v11
      index:
        prefix: index_
        period: 24h

ruler:
  alertmanager_url: http://localhost:9093

limits_config:
  retention_period: 720h  # 30 days
  ingestion_rate_mb: 10
  ingestion_burst_size_mb: 20
  per_stream_rate_limit: 10MB
  per_stream_rate_limit_burst: 20MB

compactor:
  working_directory: /loki/compactor
  shared_store: filesystem
  compaction_interval: 10m
  retention_enabled: true
  retention_delete_delay: 2h
  retention_delete_worker_count: 150
```

#### 1.3 Create Grafana Datasource Config

Create `config/grafana/provisioning/datasources/loki.yaml`:

```yaml
apiVersion: 1

datasources:
  - name: Loki
    type: loki
    access: proxy
    url: http://loki:3100
    isDefault: true
    jsonData:
      maxLines: 1000
    editable: true
```

#### 1.4 Create Docker Compose File

Create `docker-compose.yml`:

```yaml
version: '3.8'

services:
  loki:
    image: grafana/loki:2.9.3
    container_name: shapyfy-loki
    ports:
      - "3100:3100"  # IMPORTANT: Exposed for remote Promtail
    command: -config.file=/etc/loki/local-config.yaml
    volumes:
      - ./config/loki/loki-config.yaml:/etc/loki/local-config.yaml
      - loki-data:/loki
    networks:
      - logging
    restart: unless-stopped

  grafana:
    image: grafana/grafana:10.2.3
    container_name: shapyfy-grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_USER=admin
      - GF_SECURITY_ADMIN_PASSWORD=shapyfy123
      - GF_USERS_ALLOW_SIGN_UP=false
    volumes:
      - grafana-data:/var/lib/grafana
      - ./config/grafana/provisioning:/etc/grafana/provisioning
    networks:
      - logging
    depends_on:
      - loki
    restart: unless-stopped

volumes:
  loki-data:
    driver: local
  grafana-data:
    driver: local

networks:
  logging:
    driver: bridge
```

#### 1.5 Start Logging Stack

```bash
docker-compose up -d
```

Verify it's running:
```bash
docker-compose ps
curl http://localhost:3100/ready  # Should return "ready"
```

---

### Step 2: Set Up APPLICATION SERVER (Server 1)

#### 2.1 Configure Promtail

On **Server 1**, create `config/promtail/promtail-remote-config.yaml`:

**IMPORTANT**: Replace `<LOGGING_SERVER_IP>` with Server 2's IP address!

```yaml
server:
  http_listen_port: 9080
  grpc_listen_port: 0

positions:
  filename: /tmp/positions.yaml

clients:
  - url: http://<LOGGING_SERVER_IP>:3100/loki/api/v1/push

scrape_configs:
  - job_name: shapyfy-core
    static_configs:
      - targets:
          - localhost
        labels:
          job: shapyfy-core
          environment: production
          server: app-server-1
          __path__: /var/log/shapyfy/*.log

    pipeline_stages:
      - json:
          expressions:
            timestamp: timestamp
            level: level
            thread: thread
            logger: logger
            message: message
            trace_id: trace_id
            span_id: span_id
            exception: exception
      - timestamp:
          source: timestamp
          format: RFC3339
      - labels:
          level:
          environment:
      - output:
          source: message
```

#### 2.2 Create Promtail Docker Compose

Create `docker-compose-promtail.yml`:

```yaml
version: '3.8'

services:
  promtail:
    image: grafana/promtail:2.9.3
    container_name: shapyfy-promtail
    volumes:
      - ./config/promtail/promtail-remote-config.yaml:/etc/promtail/config.yml
      - ./logs:/var/log/shapyfy
    command: -config.file=/etc/promtail/config.yml
    restart: unless-stopped
```

#### 2.3 Start Promtail

```bash
docker-compose -f docker-compose-promtail.yml up -d
```

Check logs to verify connection:
```bash
docker logs shapyfy-promtail
# Should show: "Starting Promtail" and connection to Loki
```

#### 2.4 Start Your Application

Your Spring Boot app should already have the Logback configuration from earlier setup.

```bash
./gradlew bootRun
# Or run your JAR in production
java -jar build/libs/core-0.0.1-SNAPSHOT.jar
```

The app writes logs to `logs/shapyfy-core.log`, and Promtail ships them to Server 2.

---

## Step 3: Access Grafana

From your browser (from anywhere):

1. Navigate to: `http://<SERVER_2_IP>:3000`
2. Login: `admin` / `shapyfy123`
3. Go to **Explore** (compass icon)
4. Query: `{job="shapyfy-core"}`

You should see logs from Server 1!

---

## Security Considerations

### Firewall Rules

On **Server 2** (Logging):
```bash
# Allow Grafana access (from your IP or all)
sudo ufw allow 3000/tcp

# Allow Loki from Server 1 ONLY
sudo ufw allow from <SERVER_1_IP> to any port 3100

# Enable firewall
sudo ufw enable
```

On **Server 1** (Application):
```bash
# Allow your application port
sudo ufw allow 8080/tcp

# No need to expose Promtail (internal only)
sudo ufw enable
```

### Secure Grafana

Change default password immediately:
1. Login to Grafana
2. Click profile icon → **Change Password**
3. Or set via environment variable in docker-compose

### Secure Loki (Optional but Recommended)

Add authentication to Loki by using a reverse proxy (nginx) with basic auth:

```nginx
# On Server 2, install nginx
sudo apt install nginx

# Create basic auth
sudo htpasswd -c /etc/nginx/.htpasswd loki

# Configure nginx to proxy Loki with auth
# Then update Promtail config to include credentials
```

---

## Testing the Setup

### 1. Generate Test Logs

On **Server 1**, trigger some application activity:

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/v1/exercises
```

### 2. Check Logs in Grafana

On **Server 2**, open Grafana and run:

```logql
{job="shapyfy-core"} | json | level="INFO"
```

You should see logs appearing in real-time!

### 3. Monitor Promtail

Check if Promtail is shipping logs:

```bash
# On Server 1
docker logs shapyfy-promtail --tail 50

# Should show lines like:
# level=info msg="Successfully sent batch" entries=10
```

---

## Monitoring & Maintenance

### View Metrics

Check Loki metrics (on Server 2):
```bash
curl http://localhost:3100/metrics | grep loki_ingester
```

Check Promtail metrics (on Server 1):
```bash
curl http://localhost:9080/metrics | grep promtail
```

### Check Disk Usage

Loki stores data in Docker volumes:
```bash
# On Server 2
docker volume ls
docker volume inspect shapyfy-logging_loki-data
```

### Backup Logs

```bash
# On Server 2, backup Loki data
docker run --rm -v shapyfy-logging_loki-data:/data -v $(pwd):/backup \
  ubuntu tar czf /backup/loki-backup-$(date +%Y%m%d).tar.gz -C /data .
```

---

## Troubleshooting

### No logs appearing in Grafana?

**Check 1: Is Promtail connecting to Loki?**
```bash
# On Server 1
docker logs shapyfy-promtail | grep -i error
```

**Check 2: Is Server 1 reaching Server 2?**
```bash
# On Server 1
curl http://<SERVER_2_IP>:3100/ready
# Should return: "ready"
```

**Check 3: Are log files being created?**
```bash
# On Server 1
ls -lh logs/
tail -f logs/shapyfy-core.log
```

**Check 4: Firewall blocking?**
```bash
# On Server 2
sudo ufw status
# Should show: 3100/tcp ALLOW from <Server 1 IP>
```

### Logs are delayed?

This is normal. Promtail batches logs before sending. Check `docker logs shapyfy-promtail` for batch send confirmations.

---

## Scaling to Multiple Application Servers

To add more app servers:

1. **Server 3** (Another app instance):
   - Install your application
   - Install Promtail with same config (change `server: app-server-2` label)
   - Point to same Loki on Server 2

2. **Query logs from all servers**:
   ```logql
   {job="shapyfy-core"}  # All servers
   {job="shapyfy-core", server="app-server-1"}  # Specific server
   ```

This is the beauty of centralized logging!

---

## Cost & Resource Usage

### Server 2 (Logging):
- **RAM**: ~400-500MB (Loki + Grafana)
- **Disk**: Grows with log volume (30-day retention ≈ 5-20GB depending on traffic)
- **CPU**: Minimal (~5-10%)

### Server 1 (Application):
- **RAM**: ~500-700MB (Kotlin app + Promtail)
- **Disk**: Minimal (logs are rotated)
- **CPU**: Depends on app load

**Total**: Both servers comfortably fit in 4GB RAM each.

---

## Next Steps

1. ✅ Set up alerting (Grafana Alerts)
2. ✅ Create dashboards for common queries
3. ✅ Add metrics with Prometheus (separate guide)
4. ✅ Implement log retention policies
5. ✅ Set up SSL/TLS for Grafana
6. ✅ Add more application servers

---

## Quick Reference

**Server 1 (App):**
- Application: Port 8080
- Promtail: Port 9080 (internal metrics)
- Logs directory: `./logs/`

**Server 2 (Logging):**
- Grafana: http://<server2-ip>:3000
- Loki API: http://<server2-ip>:3100

**Default Credentials:**
- Grafana: admin / shapyfy123 (CHANGE THIS!)
