# Setup PostgreSQL Without Docker

If you don't want to use Docker, follow these steps to install PostgreSQL locally.

## Step 1: Download PostgreSQL

1. Download PostgreSQL 15 from: https://www.postgresql.org/download/windows/
2. Run the installer (postgresql-15.x-x-windows-x64.exe)

## Step 2: Installation Options

During installation:
- **Port**: 5432 (default)
- **Password**: Set a password (remember it!)
- **Locale**: Default
- **Components**: Select all (including Stack Builder)

## Step 3: Install PostGIS Extension

### Option A: Using Stack Builder (Easier)
1. After PostgreSQL installs, Stack Builder opens automatically
2. Select your PostgreSQL installation
3. Under "Spatial Extensions", check **PostGIS**
4. Click Next and install

### Option B: Manual Download
1. Download PostGIS from: https://postgis.net/windows_downloads/
2. Run the installer
3. Select your PostgreSQL version (15)

## Step 4: Create Database

Open PowerShell and run:

```powershell
# Connect to PostgreSQL (enter your password when prompted)
psql -U postgres

# In the psql prompt, run these commands:
CREATE DATABASE smart_waste_db;
\c smart_waste_db
CREATE EXTENSION postgis;
\q
```

Alternatively, use pgAdmin (installed with PostgreSQL):
1. Open pgAdmin 4
2. Right-click "Databases" → "Create" → "Database"
3. Name: `smart_waste_db`
4. Click Save
5. Right-click the new database → "Query Tool"
6. Run: `CREATE EXTENSION postgis;`

## Step 5: Update Application Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smart_waste_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE
```

**Replace `YOUR_PASSWORD_HERE` with the password you set during installation!**

## Step 6: Verify Installation

Test the database connection:

```powershell
psql -U postgres -d smart_waste_db -c "SELECT PostGIS_Version();"
```

You should see PostGIS version information.

## Step 7: Run the Application

```powershell
mvn clean install
mvn spring-boot:run
```

## Troubleshooting

### "psql is not recognized"
Add PostgreSQL to PATH:
1. Search "Environment Variables" in Windows
2. Edit "System variables" → "Path"
3. Add: `C:\Program Files\PostgreSQL\15\bin`
4. Restart PowerShell

### Connection Refused
1. Check PostgreSQL is running:
   - Search for "Services" in Windows
   - Find "postgresql-x64-15"
   - Ensure Status is "Running"

2. If not running, start it:
   ```powershell
   # Run as Administrator
   net start postgresql-x64-15
   ```

### Password Authentication Failed
- Verify password in application.properties
- Try resetting password in pgAdmin

## Quick Commands Reference

```powershell
# Check if PostgreSQL is running
Get-Service postgresql-x64-15

# Start PostgreSQL
net start postgresql-x64-15

# Stop PostgreSQL
net stop postgresql-x64-15

# Connect to database
psql -U postgres -d smart_waste_db

# Check PostGIS
psql -U postgres -d smart_waste_db -c "SELECT PostGIS_Version();"
```

## After Setup Complete

Once database is ready:
1. Run: `mvn spring-boot:run`
2. Open: http://localhost:8080
3. The app will auto-create tables and 50 sample bins!
