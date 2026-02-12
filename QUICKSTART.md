# Quick Start Guide

## Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose (for database)

## Steps to Run

### 1. Start the Database
```bash
docker-compose up -d
```

Wait 10-15 seconds for PostgreSQL to initialize.

### 2. Build the Application
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

### 4. Access the Dashboard
Open your browser to:
```
http://localhost:8080
```

## First-Time Setup

The application will automatically:
- Create database tables
- Initialize 50 sample waste bins
- Start the background simulation
- Serve the web interface

## Test the Features

1. **View Bins**: See all bins on the map with color-coded markers
2. **Check Stats**: Dashboard shows real-time statistics
3. **Generate Route**: Click "Generate Optimized Route" button
4. **Watch Updates**: Bins update every 30 seconds automatically

## API Testing

```bash
# Get all bins
curl http://localhost:8080/api/bins

# Get statistics
curl http://localhost:8080/api/bins/stats

# Generate route
curl -X POST http://localhost:8080/api/routes/optimize

# Get full bins
curl http://localhost:8080/api/bins/full
```

## Troubleshooting

### Database Connection Error
- Ensure PostgreSQL is running: `docker ps`
- Check credentials in `application.properties`
- Restart database: `docker-compose restart`

### Port 8080 Already in Use
- Change port in `application.properties`:
  ```properties
  server.port=8081
  ```

### No Bins on Map
- Wait 30 seconds for initialization
- Check browser console for errors
- Verify API is running: `curl http://localhost:8080/api/bins`

## Stop the Application

1. Stop Spring Boot: `Ctrl+C` in terminal
2. Stop database: `docker-compose down`

## Clean Restart

```bash
# Stop everything
docker-compose down -v

# Clean build
mvn clean

# Start fresh
docker-compose up -d
mvn spring-boot:run
```

## Configuration

### Change Depot Location
Edit `application.properties`:
```properties
app.depot.latitude=your_latitude
app.depot.longitude=your_longitude
```

### Change Update Interval
```properties
# Milliseconds (30000 = 30 seconds)
app.bin.simulation.interval=30000
```

### Database Credentials
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Next Steps

- Add more bins via API
- Customize bin locations
- Modify simulation parameters
- Extend with new features

## Support

For issues or questions, check:
- Full README.md for detailed documentation
- API endpoint descriptions
- Algorithm explanations
