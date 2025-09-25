# Docker Documentation for Student Management API

## Overview
This document provides comprehensive instructions for running the Student Management API using Docker. The setup includes both development and production configurations with proper health checks, logging, and security considerations.

## Prerequisites

### Required Software
- **Docker**: 20.10+ (Docker Desktop for Windows/Mac)
- **Docker Compose**: 2.0+ (included with Docker Desktop)
- **Git**: For cloning the repository

### Firebase Setup
1. Create a Firebase project at https://console.firebase.google.com/
2. Enable Firestore Database
3. Generate a service account key:
   - Go to Project Settings → Service Accounts
   - Click "Generate new private key"
   - Save the JSON file as `firebase-service-account.json`
4. Place the file in `src/main/resources/firebase-service-account.json`

## Docker Architecture

### Multi-Stage Build
The Dockerfile uses a multi-stage build approach:
- **Builder Stage**: Uses OpenJDK 21 JDK to compile the application
- **Runtime Stage**: Uses OpenJDK 21 JRE for a smaller final image

### Security Features
- ✅ Non-root user execution
- ✅ Read-only Firebase credentials mounting
- ✅ Resource limits and health checks
- ✅ Secure cookie settings in production

## Quick Start

### Development Environment

#### Option 1: Using Docker Scripts (Recommended)
```bash
# Windows
docker-run.bat

# Linux/Mac
chmod +x docker-run.sh
./docker-run.sh
```

#### Option 2: Manual Docker Compose
```bash
# Build and start the application
docker-compose up --build

# Run in background
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop the application
docker-compose down
```

### Production Environment
```bash
# Build and start production environment
docker-compose -f docker-compose.prod.yml up --build -d

# Scale the application (if needed)
docker-compose -f docker-compose.prod.yml up --scale student-management-api=2

# View logs
docker-compose -f docker-compose.prod.yml logs -f student-management-api

# Stop production environment
docker-compose -f docker-compose.prod.yml down
```

## Environment Configurations

### Development (docker-compose.yml)
- **Profile**: `docker`
- **Port**: 8080
- **Logging**: DEBUG level for application
- **Health Check**: Every 30 seconds
- **Auto-restart**: Unless stopped

### Production (docker-compose.prod.yml)  
- **Profile**: `prod`
- **Port**: 8080
- **Logging**: INFO level, file rotation
- **Resource Limits**: 1GB memory, 1 CPU
- **Health Check**: Every 30 seconds
- **Auto-restart**: Unless stopped
- **Nginx Proxy**: Optional reverse proxy

## Container Management

### Building the Image
```bash
# Build development image
docker build -t student-management-api:dev .

# Build production image with specific tag
docker build -t student-management-api:1.0 .
```

### Running Individual Container
```bash
# Run development container
docker run -d \
  --name student-management-api \
  -p 8080:8080 \
  -v $(pwd)/src/main/resources/firebase-service-account.json:/app/config/firebase-service-account.json:ro \
  -v $(pwd)/logs:/app/logs \
  -e SPRING_PROFILES_ACTIVE=docker \
  student-management-api:dev

# Run with custom JVM options
docker run -d \
  --name student-management-api \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  -v $(pwd)/src/main/resources/firebase-service-account.json:/app/config/firebase-service-account.json:ro \
  student-management-api:dev
```

## Health Checks and Monitoring

### Health Check Endpoints
```bash
# Application health
curl http://localhost:8080/actuator/health

# Application info
curl http://localhost:8080/actuator/info

# Application metrics (if enabled)
curl http://localhost:8080/actuator/metrics
```

### Container Health Status
```bash
# Check container health
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

# View health check logs
docker inspect --format='{{range .State.Health.Log}}{{.Output}}{{end}}' student-management-api
```

## Logging

### Log Locations
- **Container**: `/app/logs/application.log`
- **Host**: `./logs/application.log` (mounted volume)

### Log Management
```bash
# View real-time logs
docker-compose logs -f student-management-api

# View last 100 lines
docker-compose logs --tail=100 student-management-api

# Export logs
docker-compose logs --no-color student-management-api > app-logs.txt
```

## Network Configuration

### Default Network
- **Name**: `student-management-net`
- **Driver**: bridge
- **Isolation**: Container-to-container communication enabled

### Port Mapping
- **Application**: 8080:8080
- **Management** (prod): 8081:8081
- **Nginx** (prod): 80:80, 443:443

## Volume Management

### Development Volumes
```bash
# Application logs
./logs:/app/logs

# Firebase credentials (read-only)
./src/main/resources/firebase-service-account.json:/app/config/firebase-service-account.json:ro
```

### Production Volumes
```bash
# Named volumes for persistence
app-logs:/app/logs
app-data:/app/data
```

## Security Configuration

### Container Security
- ✅ **Non-root execution**: Application runs as `appuser`
- ✅ **Read-only credentials**: Firebase key mounted as read-only
- ✅ **Resource limits**: Memory and CPU constraints
- ✅ **Health checks**: Automatic restart on failure

### Network Security
- ✅ **Isolated network**: Custom bridge network
- ✅ **Port restriction**: Only necessary ports exposed
- ✅ **Reverse proxy ready**: Nginx configuration included

## Troubleshooting

### Common Issues

#### 1. Docker Not Running
```bash
# Check Docker status
docker version

# Start Docker Desktop (Windows/Mac)
# Or start Docker service (Linux)
sudo systemctl start docker
```

#### 2. Firebase Credentials Missing
```bash
# Verify file exists
ls -la src/main/resources/firebase-service-account.json

# Check file permissions
chmod 644 src/main/resources/firebase-service-account.json
```

#### 3. Port Already in Use
```bash
# Check what's using port 8080
netstat -an | grep 8080

# Use different port
docker-compose -f docker-compose.yml -p 8081:8080 up
```

#### 4. Container Won't Start
```bash
# Check container logs
docker-compose logs student-management-api

# Check container status
docker ps -a

# Get detailed container info
docker inspect student-management-api
```

#### 5. Health Check Failing
```bash
# Check health endpoint manually
curl -f http://localhost:8080/actuator/health

# Check container health status
docker inspect --format='{{.State.Health.Status}}' student-management-api
```

### Debug Commands

```bash
# Enter running container
docker-compose exec student-management-api /bin/bash

# Check environment variables
docker-compose exec student-management-api env

# Check Java process
docker-compose exec student-management-api ps aux

# Check disk space
docker-compose exec student-management-api df -h

# Check memory usage
docker-compose exec student-management-api free -m
```

## Performance Optimization

### JVM Tuning
```bash
# Custom JVM options via environment variable
JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### Container Resources
```yaml
deploy:
  resources:
    limits:
      memory: 1G
      cpus: '1.0'
    reservations:
      memory: 256M
      cpus: '0.25'
```

## Maintenance

### Image Cleanup
```bash
# Remove unused images
docker image prune

# Remove all unused resources
docker system prune -a

# Remove specific image
docker rmi student-management-api:dev
```

### Container Updates
```bash
# Pull latest changes and rebuild
git pull
docker-compose up --build -d

# Rolling update (zero downtime)
docker-compose -f docker-compose.prod.yml up -d --no-deps student-management-api
```

## API Testing in Docker

### Using curl
```bash
# Create student
curl -X POST http://localhost:8080/api/v1/student \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mr",
    "name": "John Doe",
    "address": "123 Main St",
    "city": "New York",
    "course": "Computer Science"
  }'

# Get all students
curl http://localhost:8080/api/v1/students

# Get paginated students
curl "http://localhost:8080/api/v1/students/paginated?page=0&size=5"
```

### Swagger UI
Access the API documentation at:
- **Development**: http://localhost:8080/swagger-ui.html
- **Production**: http://localhost:8080/swagger-ui.html

## Production Deployment

### Using Docker Compose
```bash
# Start production environment
docker-compose -f docker-compose.prod.yml up -d

# Monitor logs
docker-compose -f docker-compose.prod.yml logs -f
```

### Using Docker Swarm (Advanced)
```bash
# Initialize swarm
docker swarm init

# Deploy stack
docker stack deploy -c docker-compose.prod.yml student-management

# Scale service
docker service scale student-management_student-management-api=3

# Check service status
docker stack services student-management
```

## Backup and Recovery

### Database Backup (Firebase)
Firebase Firestore handles automatic backups, but you can export data:
```bash
# Export Firestore data (requires Firebase CLI)
firebase firestore:export gs://your-bucket/backup-folder
```

### Application Logs
```bash
# Backup logs
tar -czf logs-backup-$(date +%Y%m%d).tar.gz logs/

# Rotate logs automatically via logrotate (Linux)
```

## Support and Maintenance

### Monitoring Recommendations
- **Application Metrics**: Enable Micrometer/Prometheus
- **Container Monitoring**: Use Docker stats or cAdvisor
- **Log Aggregation**: Consider ELK stack or similar
- **Alerting**: Set up alerts for health check failures

### Updates and Patches
- **Regular Updates**: Keep base images updated
- **Security Patches**: Monitor for Spring Boot security updates
- **Firebase SDK**: Keep Firebase SDK updated

## Conclusion

This Docker setup provides a production-ready containerized environment for the Student Management API with proper security, monitoring, and scalability considerations. The multi-environment configuration allows for smooth development-to-production workflows.