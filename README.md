# 🌱 Smart City Waste Optimization Dashboard

A full-stack sustainability-focused web application that demonstrates real-time waste bin monitoring and intelligent route optimization using graph algorithms. This project showcases modern development practices including ESG (Environmental, Social, Governance) principles, RESTful API design, geospatial computing, and algorithmic problem-solving.

## 🎯 Project Overview

**Theme:** Sustainability (ESG), Full Stack Development, & Algorithms

**What Makes This Special:**
- ✅ **Social Good Impact:** Optimizes waste collection to reduce fuel consumption and carbon emissions
- ✅ **Full Stack Excellence:** Complete Spring Boot backend + Modern JavaScript frontend
- ✅ **Complex Algorithms:** Implements Nearest Neighbor TSP approximation for route optimization
- ✅ **Geospatial Computing:** Uses PostGIS and Haversine formula for distance calculations
- ✅ **Real-time Simulation:** Background scheduler simulates live bin fill levels

## 🏗️ Architecture

### Backend Stack
- **Framework:** Spring Boot 3.2.2 (Java 17)
- **Database:** PostgreSQL 15+ with PostGIS extension
- **ORM:** Spring Data JPA with Hibernate Spatial
- **API:** RESTful endpoints with JSON responses

### Frontend Stack
- **Core:** HTML5, CSS3, Vanilla JavaScript
- **Maps:** Leaflet.js for interactive mapping
- **Tiles:** OpenStreetMap
- **Design:** Responsive, mobile-friendly UI

### Algorithms
- **Route Optimization:** Nearest Neighbor Algorithm (Greedy TSP Approximation)
- **Distance Calculation:** Haversine Formula for great-circle distances
- **Time Complexity:** O(n²) for n bins

## ✨ Core Features

### 1. Real-time Dashboard
- Live statistics: Total bins, fill level distribution, averages
- Color-coded status indicators (Green/Yellow/Red)
- Auto-refresh every 30 seconds

### 2. Interactive Map
- Visual representation of all waste bins in the city
- Markers color-coded by fill level:
  - 🟢 Green: < 50% (Low)
  - 🟡 Yellow: 50-79% (Medium)
  - 🔴 Red: ≥ 80% (High/Full)
- Depot location marked in blue
- Click bins for detailed information

### 3. Route Optimization
- One-click route generation for full bins
- Visualized optimal path with directional arrows
- Distance and time estimates
- Round-trip calculation (Depot → Bins → Depot)

### 4. Background Simulation
- Automated fill level updates every 30 seconds
- Simulates waste accumulation (1-15% increase)
- Occasional emptying events (10% probability)
- Realistic city-scale operations

### 5. Filtering & Search
- Filter by fill level (All/Full only)
- Filter by waste type (General/Recyclable/Organic)
- Dynamic map updates

## 🚀 Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 15+
- PostGIS extension

### 1. Database Setup

#### Option A: Using Docker (Recommended)
```bash
# Start PostgreSQL with PostGIS
docker-compose up -d

# Verify it's running
docker ps
```

#### Option B: Manual Installation
```bash
# Install PostgreSQL and PostGIS
# On Ubuntu/Debian:
sudo apt-get install postgresql postgresql-contrib postgis

# On macOS:
brew install postgresql postgis

# Create database
psql -U postgres
CREATE DATABASE smart_waste_db;
\c smart_waste_db
CREATE EXTENSION postgis;
```

### 2. Configure Application

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smart_waste_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 3. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Access the Dashboard

Open your browser and navigate to:
```
http://localhost:8080
```

## 📡 API Endpoints

### Waste Bins

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/bins` | Get all bins |
| GET | `/api/bins/full` | Get full bins (≥80%) |
| GET | `/api/bins/{id}` | Get specific bin |
| GET | `/api/bins/stats` | Get dashboard statistics |
| POST | `/api/bins` | Create new bin |
| PUT | `/api/bins/{id}/fill-level?fillLevel={level}` | Update bin fill level |
| DELETE | `/api/bins/{id}` | Delete bin |

### Routes

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/routes/optimize` | Generate optimized route |
| GET | `/api/routes/recent` | Get recent routes |

### Example API Calls

```bash
# Get all bins
curl http://localhost:8080/api/bins

# Get full bins only
curl http://localhost:8080/api/bins/full

# Generate optimized route
curl -X POST http://localhost:8080/api/routes/optimize

# Get dashboard stats
curl http://localhost:8080/api/bins/stats
```

## 🧮 Algorithm Explanation

### Nearest Neighbor Algorithm (TSP Approximation)

**Problem:** Given n full waste bins, find the shortest route that visits all bins and returns to the depot.

**Solution:** Greedy approximation algorithm

**Steps:**
1. Start at depot location
2. Find the nearest unvisited bin
3. Move to that bin
4. Repeat until all bins are visited
5. Return to depot

**Time Complexity:** O(n²) where n = number of bins

**Space Complexity:** O(n) for storing the route

**Code Implementation:**
```java
private List<WasteBin> nearestNeighborTSP(List<WasteBin> bins, 
                                          double startLat, 
                                          double startLon) {
    List<WasteBin> route = new ArrayList<>();
    Set<Long> visited = new HashSet<>();
    
    double currentLat = startLat;
    double currentLon = startLon;
    
    while (visited.size() < bins.size()) {
        WasteBin nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (WasteBin bin : bins) {
            if (!visited.contains(bin.getId())) {
                double distance = calculateHaversineDistance(
                    currentLat, currentLon,
                    bin.getLatitude(), bin.getLongitude()
                );
                
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = bin;
                }
            }
        }
        
        if (nearest != null) {
            route.add(nearest);
            visited.add(nearest.getId());
            currentLat = nearest.getLatitude();
            currentLon = nearest.getLongitude();
        }
    }
    
    return route;
}
```

### Haversine Formula

Calculates the great-circle distance between two points on Earth.

**Formula:**
```
a = sin²(Δφ/2) + cos(φ1) × cos(φ2) × sin²(Δλ/2)
c = 2 × atan2(√a, √(1−a))
d = R × c
```

Where:
- φ = latitude in radians
- λ = longitude in radians
- R = Earth's radius (6,371 km)

## 📊 Database Schema

### bins table
```sql
CREATE TABLE bins (
    id BIGSERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    fill_level INTEGER NOT NULL,
    capacity INTEGER NOT NULL,
    location_name VARCHAR(255),
    bin_type VARCHAR(50),
    last_updated TIMESTAMP
);
```

### routes table
```sql
CREATE TABLE routes (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    total_distance DOUBLE PRECISION,
    estimated_time INTEGER,
    status VARCHAR(50)
);

CREATE TABLE route_waypoints (
    route_id BIGINT REFERENCES routes(id),
    bin_id BIGINT
);
```

## 🎨 Screenshots

### Dashboard View
- Real-time statistics cards
- Color-coded bin status
- Interactive controls

### Map View
- Clustered bin markers
- Depot location
- Filter options

### Optimized Route
- Blue polyline with arrows
- Distance and time calculations
- Depot round-trip visualization

## 🔄 Background Processes

### Bin Status Simulator
Runs every 30 seconds (configurable):

```java
@Scheduled(fixedDelayString = "${app.bin.simulation.interval:30000}")
public void simulateBinFillLevels() {
    // 10% chance to empty bin (collection)
    // Otherwise increase fill level by 1-15%
}
```

## 🌍 Configuration Options

### Depot Location
Edit in `application.properties`:
```properties
app.depot.latitude=40.7128
app.depot.longitude=-74.0060
```

### Simulation Interval
```properties
# Milliseconds between updates (default: 30000 = 30 seconds)
app.bin.simulation.interval=30000
```

## 🚀 Deployment

### Docker Deployment
```bash
# Build Docker image
docker build -t waste-optimization:1.0 .

# Run container
docker run -p 8080:8080 waste-optimization:1.0
```

### Cloud Deployment
Compatible with:
- AWS Elastic Beanstalk
- Google Cloud Run
- Azure App Service
- Heroku

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=RouteOptimizationServiceTest
```

## 📈 Future Enhancements

1. **Advanced Algorithms**
   - Genetic Algorithm for better TSP solutions
   - Dynamic programming approaches
   - Real-time traffic integration

2. **IoT Integration**
   - Actual IoT sensor data
   - MQTT protocol support
   - Real-time alerts

3. **Analytics**
   - Historical trend analysis
   - Predictive fill forecasting (ML)
   - Cost savings reports

4. **Features**
   - Multiple truck routing
   - Driver mobile app
   - Admin panel with authentication

## 🤝 Contributing

This is a portfolio project, but suggestions are welcome!

## 📄 License

MIT License - Feel free to use for learning and portfolio purposes.

## 👤 Author

**Your Name**
- Portfolio: [your-portfolio.com]
- GitHub: [@yourusername]
- LinkedIn: [your-linkedin]

## 🙏 Acknowledgments

- OpenStreetMap for map tiles
- Leaflet.js for mapping library
- Spring Boot team for excellent framework
- PostGIS for geospatial capabilities

---

**Built with ❤️ for a sustainable future**

*This project demonstrates: Full Stack Development • RESTful APIs • Algorithmic Thinking • Database Design • Geospatial Computing • Real-time Systems • Responsive UI • Social Good Impact*
