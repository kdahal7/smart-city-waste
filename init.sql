-- Initialize Smart City Waste Management Database
-- This script is automatically run by Docker

-- Create PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Verify PostGIS installation
SELECT PostGIS_Version();

-- Create bins table if not exists (Hibernate will also create it, but this ensures it's ready)
CREATE TABLE IF NOT EXISTS bins (
    id BIGSERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    fill_level INTEGER NOT NULL CHECK (fill_level >= 0 AND fill_level <= 100),
    capacity INTEGER NOT NULL,
    location_name VARCHAR(255),
    bin_type VARCHAR(50),
    last_updated TIMESTAMP
);

-- Create spatial index for efficient geospatial queries
CREATE INDEX IF NOT EXISTS idx_bins_location 
ON bins USING GIST (ST_MakePoint(longitude, latitude));

-- Create index on fill_level for faster queries
CREATE INDEX IF NOT EXISTS idx_bins_fill_level ON bins(fill_level);

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE smart_waste_db TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;
