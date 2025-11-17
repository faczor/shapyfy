# Logging Setup with Grafana + Loki

This guide explains how to set up and use the Grafana + Loki logging stack for the Shapyfy Core backend.

## Overview

The logging stack consists of:
- **Loki**: Log aggregation system (like Elasticsearch but lighter)
- **Promtail**: Log collector that ships logs from files to Loki
- **Grafana**: Web UI for querying and visualizing logs
- **Logback**: Java logging framework configured to output JSON logs

## Architecture

```
Spring Boot App → JSON Logs → File System → Promtail → Loki → Grafana
```

1. Your Spring Boot app writes JSON-formatted logs to `logs/shapyfy-core.log`
2. Promtail watches this file and ships logs to Loki
3. Loki stores and indexes the logs
4. You query and visualize logs in Grafana

## Quick Start

### 1. Start the Logging Stack

```bash
docker-compose -f docker-compose-logging.yml up -d
```

This starts:
- Grafana on http://localhost:3000 (admin/shapyfy123)
- Loki on http://localhost:3100
- Promtail (no UI, just ships logs)

### 2. Start Your Application

```bash
./gradlew bootRun
```

Your app will now write logs to:
- **Console**: Human-readable format (for development)
- **File**: JSON format at `logs/shapyfy-core.log` (for Loki)

### 3. Access Grafana

1. Open http://localhost:3000
2. Login: `admin` / `shapyfy123`
3. Go to **Explore** (compass icon in left sidebar)
4. Select **Loki** as data source
5. Start querying logs!

## Querying Logs in Grafana

### Basic Queries

**All logs from your app:**
```logql
{job="shapyfy-core"}
```

**Only ERROR level logs:**
```logql
{job="shapyfy-core"} | json | level="ERROR"
```

**Logs containing "exercise":**
```logql
{job="shapyfy-core"} |~ "exercise"
```

**Logs from specific logger:**
```logql
{job="shapyfy-core"} | json | logger=~"com.shapyfy.*"
```

**SQL query logs:**
```logql
{job="shapyfy-core"} | json | logger="org.springframework.jdbc.core"
```

### Advanced Queries

**Count errors over time:**
```logql
sum(count_over_time({job="shapyfy-core"} | json | level="ERROR" [5m]))
```

**Filter by exception type:**
```logql
{job="shapyfy-core"} | json | exception=~".*DuplicateException.*"
```

**Logs with trace ID (for request tracing):**
```logql
{job="shapyfy-core"} | json | trace_id="your-trace-id"
```

## Log Levels

The application uses these log levels (configured in `logback-spring.xml`):

- `com.shapyfy.*` → **DEBUG** (your application code)
- `org.springframework.jdbc.core` → **DEBUG** (SQL queries)
- `org.springframework.web` → **INFO**
- `org.springframework.security` → **INFO**
- Most libraries → **WARN**

To change log levels, edit `src/main/resources/logback-spring.xml`.

## Log Retention

- **Files**: 30 days, max 1GB total (rotates daily)
- **Loki**: 30 days (720 hours)
- Old logs are automatically deleted

## Adding Custom Log Fields

You can add custom fields to logs using MDC (Mapped Diagnostic Context):

```kotlin
import org.slf4j.MDC

// In your code
MDC.put("user_id", "12345")
MDC.put("request_id", UUID.randomUUID().toString())

logger.info("User performed action")

// Clean up
MDC.remove("user_id")
MDC.remove("request_id")
```

These fields will appear in JSON logs and can be queried in Grafana:
```logql
{job="shapyfy-core"} | json | user_id="12345"
```

## Useful Grafana Features

### 1. Live Tail
Click **Live** button in Explore view to stream logs in real-time (like `tail -f`).

### 2. Log Context
Click on any log line to see surrounding logs for context.

### 3. Field Extraction
Grafana automatically extracts JSON fields. Click any field to:
- Filter by that value
- Filter out that value
- Show only logs with this field

### 4. Create Dashboards
Save your queries as panels and create dashboards:
1. Run a query in Explore
2. Click "Add to dashboard"
3. Create visualizations (graphs, tables, etc.)

## Troubleshooting

### No logs in Grafana?

1. **Check if app is writing logs:**
   ```bash
   ls -lh logs/
   tail -f logs/shapyfy-core.log
   ```

2. **Check if Promtail is running:**
   ```bash
   docker-compose -f docker-compose-logging.yml ps
   docker logs shapyfy-promtail
   ```

3. **Check if Loki received logs:**
   ```bash
   curl http://localhost:3100/ready
   curl http://localhost:3100/metrics | grep loki_ingester_streams_created_total
   ```

### Logs are delayed?

Promtail scans files every few seconds. For instant feedback during development, watch the console output.

### Want to change Grafana password?

Edit `docker-compose-logging.yml`:
```yaml
environment:
  - GF_SECURITY_ADMIN_PASSWORD=your-new-password
```

Then restart:
```bash
docker-compose -f docker-compose-logging.yml restart grafana
```

## Production Deployment

For production, consider:

1. **Use remote Loki**: Deploy Loki separately (not in same docker-compose)
2. **Add authentication**: Enable Loki authentication
3. **Configure alerting**: Set up alerts for ERROR/FATAL logs
4. **Increase retention**: Adjust based on compliance needs
5. **Use environment variables**: Don't hardcode passwords
6. **Send logs directly**: Use Loki's HTTP API instead of file + Promtail

Example: Direct HTTP logging to Loki (no file needed):
```kotlin
// Add dependency: implementation 'com.github.loki4j:loki-logback-appender:1.4.2'
```

Then configure in `logback-spring.xml`:
```xml
<appender name="LOKI" class="com.github.loki4j.logback.Loki4jAppender">
    <http>
        <url>http://loki:3100/loki/api/v1/push</url>
    </http>
    <format>
        <label>
            <pattern>app=shapyfy-core,env=${SPRING_PROFILES_ACTIVE}</pattern>
        </label>
    </format>
</appender>
```

## Managing the Stack

**Start:**
```bash
docker-compose -f docker-compose-logging.yml up -d
```

**Stop:**
```bash
docker-compose -f docker-compose-logging.yml down
```

**View logs:**
```bash
docker-compose -f docker-compose-logging.yml logs -f
```

**Reset everything (deletes all logs!):**
```bash
docker-compose -f docker-compose-logging.yml down -v
```

## Configuration Files

- `docker-compose-logging.yml` - Docker compose stack
- `config/loki/loki-config.yaml` - Loki configuration
- `config/promtail/promtail-config.yaml` - Promtail configuration
- `config/grafana/provisioning/datasources/loki.yaml` - Grafana datasource
- `src/main/resources/logback-spring.xml` - Application logging config

## Next Steps

1. **Create dashboards**: Build dashboards for common queries
2. **Set up alerts**: Get notified of errors via Slack/email
3. **Add request tracing**: Implement distributed tracing with trace IDs
4. **Integrate with metrics**: Combine logs with Prometheus metrics
5. **Add log sampling**: Sample high-volume logs to reduce storage

## Resources

- [Loki Documentation](https://grafana.com/docs/loki/latest/)
- [LogQL Query Language](https://grafana.com/docs/loki/latest/logql/)
- [Grafana Explore](https://grafana.com/docs/grafana/latest/explore/)
- [Logstash Encoder](https://github.com/logfellow/logstash-logback-encoder)
