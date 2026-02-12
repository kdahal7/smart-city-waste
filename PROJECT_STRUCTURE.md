# Project Structure

```
Smart City Waste Optimization Dashboard/
│
├── src/
│   ├── main/
│   │   ├── java/com/smartcity/waste/
│   │   │   ├── WasteOptimizationApplication.java    # Main Spring Boot application
│   │   │   │
│   │   │   ├── config/
│   │   │   │   └── DataInitializer.java             # Sample data initialization
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── WasteBinController.java          # REST API for bins
│   │   │   │   └── RouteController.java             # REST API for routes
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── WasteBinDTO.java                 # Bin data transfer object
│   │   │   │   ├── RouteResponse.java               # Route response DTO
│   │   │   │   └── DashboardStats.java              # Statistics DTO
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── WasteBin.java                    # Bin entity (JPA)
│   │   │   │   └── Route.java                       # Route entity (JPA)
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── WasteBinRepository.java          # Bin data access
│   │   │   │   └── RouteRepository.java             # Route data access
│   │   │   │
│   │   │   ├── scheduler/
│   │   │   │   └── BinStatusSimulator.java          # Background scheduler
│   │   │   │
│   │   │   └── service/
│   │   │       ├── WasteBinService.java             # Bin business logic
│   │   │       └── RouteOptimizationService.java    # Route optimization algorithm
│   │   │
│   │   └── resources/
│   │       ├── application.properties                # Configuration
│   │       │
│   │       └── static/                               # Frontend files
│   │           ├── index.html                        # Main HTML page
│   │           ├── css/
│   │           │   └── style.css                     # Styles
│   │           └── js/
│   │               └── app.js                        # JavaScript logic
│   │
│   └── test/                                         # Test files (structure for future)
│
├── pom.xml                                           # Maven dependencies
├── docker-compose.yml                                # PostgreSQL + PostGIS setup
├── init.sql                                          # Database initialization
├── Dockerfile                                        # Container image definition
├── .gitignore                                        # Git ignore rules
├── README.md                                         # Complete documentation
├── QUICKSTART.md                                     # Quick setup guide
└── PROJECT_STRUCTURE.md                              # This file

```

## File Descriptions

### Backend (Java/Spring Boot)

#### Main Application
- **WasteOptimizationApplication.java**: Entry point, enables scheduling

#### Configuration
- **DataInitializer.java**: Creates 50 sample bins on first run

#### Controllers (REST API)
- **WasteBinController.java**: 
  - GET /api/bins - All bins
  - GET /api/bins/full - Full bins only
  - GET /api/bins/stats - Dashboard statistics
  - POST /api/bins - Create bin
  - PUT /api/bins/{id}/fill-level - Update fill level
  - DELETE /api/bins/{id} - Delete bin

- **RouteController.java**:
  - POST /api/routes/optimize - Generate route
  - GET /api/routes/recent - Recent routes

#### DTOs (Data Transfer Objects)
- **WasteBinDTO.java**: Bin data for API responses
- **RouteResponse.java**: Route data with bins array
- **DashboardStats.java**: Aggregated statistics

#### Entities (Database Models)
- **WasteBin.java**: Bin with location, fill level, capacity
- **Route.java**: Saved routes with waypoints

#### Repositories (Data Access)
- **WasteBinRepository.java**: JPA repository with custom queries
- **RouteRepository.java**: Route persistence

#### Scheduler
- **BinStatusSimulator.java**: Updates bins every 30 seconds

#### Services (Business Logic)
- **WasteBinService.java**: CRUD operations, statistics
- **RouteOptimizationService.java**: 
  - Nearest Neighbor TSP algorithm
  - Haversine distance calculations
  - Route persistence

### Frontend (HTML/CSS/JavaScript)

#### HTML
- **index.html**: Single-page application
  - Dashboard with stats cards
  - Control panel with buttons
  - Map container
  - Route information panel

#### CSS
- **style.css**: Modern, responsive design
  - Gradient background
  - Card layouts
  - Map styling
  - Animations

#### JavaScript
- **app.js**: Application logic
  - Leaflet.js map initialization
  - API integration
  - Real-time updates
  - Route visualization
  - Filtering and notifications

### Configuration Files

#### Maven
- **pom.xml**: Dependencies
  - Spring Boot Web
  - Spring Data JPA
  - PostgreSQL driver
  - Hibernate Spatial
  - Lombok

#### Docker
- **docker-compose.yml**: PostgreSQL + PostGIS container
- **Dockerfile**: Application containerization
- **init.sql**: Database initialization script

#### Git
- **.gitignore**: Excludes build files, IDE configs

#### Documentation
- **README.md**: Complete documentation
- **QUICKSTART.md**: Fast setup guide
- **PROJECT_STRUCTURE.md**: This file

## Technology Stack Summary

### Backend Technologies
- Java 17
- Spring Boot 3.2.2
- Spring Data JPA
- Hibernate Spatial
- PostgreSQL 15
- PostGIS 3.3
- Lombok
- Maven

### Frontend Technologies
- HTML5
- CSS3
- JavaScript (ES6+)
- Leaflet.js 1.9.4
- OpenStreetMap tiles

### Algorithms
- Nearest Neighbor (Greedy TSP)
- Haversine Formula
- Geospatial indexing

### DevOps
- Docker
- Docker Compose
- Git

## Key Design Patterns

1. **MVC Pattern**: Controllers → Services → Repositories
2. **DTO Pattern**: Separate entity and API models
3. **Repository Pattern**: Data access abstraction
4. **Dependency Injection**: Spring autowiring
5. **Scheduled Tasks**: Background processing
6. **RESTful API**: Stateless HTTP endpoints

## Database Schema

### Tables
1. **bins**: Waste bin locations and status
2. **routes**: Saved optimization routes
3. **route_waypoints**: Route bin associations

### Indexes
- Spatial index on bin locations
- Index on fill_level for queries

## API Flow

1. Frontend calls REST API
2. Controller receives request
3. Service performs business logic
4. Repository accesses database
5. Response sent back as JSON
6. Frontend updates UI

## Data Flow

1. **Initialization**: DataInitializer creates sample bins
2. **Simulation**: BinStatusSimulator updates fill levels
3. **Display**: Frontend fetches and displays bins
4. **Optimization**: User triggers route generation
5. **Visualization**: Route drawn on map

## Extension Points

- Add authentication/authorization
- Implement WebSocket for real-time updates
- Add predictive analytics (ML)
- Multi-truck routing
- Driver mobile application
- IoT sensor integration
