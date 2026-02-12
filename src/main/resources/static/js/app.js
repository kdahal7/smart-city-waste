// API Configuration
// Auto-detect API URL: Use current origin in production, localhost in development
const API_BASE_URL = window.location.hostname === 'localhost' 
    ? 'http://localhost:8080/api' 
    : `${window.location.origin}/api`;

// Map Configuration
const DEPOT_LAT = 40.7128;
const DEPOT_LON = -74.0060;

let map;
let bins = [];
let routePolyline = null;
let routeDecorator = null;
let depotMarker = null;
let binMarkers = [];

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    initializeMap();
    loadDashboardStats();
    loadBins();
    
    // Setup event listeners
    document.getElementById('refreshBtn').addEventListener('click', refreshData);
    document.getElementById('optimizeBtn').addEventListener('click', generateOptimizedRoute);
    document.getElementById('clearRouteBtn').addEventListener('click', clearRoute);
    document.getElementById('filterSelect').addEventListener('change', applyFilter);
    
    // Auto-refresh every 30 seconds
    setInterval(refreshData, 30000);
});

// Initialize Leaflet Map
function initializeMap() {
    map = L.map('map').setView([DEPOT_LAT, DEPOT_LON], 12);
    
    // Add OpenStreetMap tiles
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19
    }).addTo(map);
    
    // Add depot marker
    depotMarker = L.marker([DEPOT_LAT, DEPOT_LON], {
        icon: L.icon({
            iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-blue.png',
            shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34],
            shadowSize: [41, 41]
        })
    }).addTo(map);
    
    depotMarker.bindPopup('<div class="popup-content"><h4>🏢 Depot</h4><p>Truck starting point</p></div>');
}

// Load Dashboard Statistics
async function loadDashboardStats() {
    try {
        const response = await fetch(`${API_BASE_URL}/bins/stats`);
        const stats = await response.json();
        
        document.getElementById('totalBins').textContent = stats.totalBins;
        document.getElementById('lowFillBins').textContent = stats.lowFillBins;
        document.getElementById('mediumFillBins').textContent = stats.mediumFillBins;
        document.getElementById('highFillBins').textContent = stats.highFillBins;
        document.getElementById('avgFillLevel').textContent = stats.averageFillLevel.toFixed(1) + '%';
    } catch (error) {
        console.error('Error loading dashboard stats:', error);
        showNotification('Failed to load dashboard statistics', 'error');
    }
}

// Load All Bins
async function loadBins() {
    try {
        const response = await fetch(`${API_BASE_URL}/bins`);
        bins = await response.json();
        displayBinsOnMap(bins);
    } catch (error) {
        console.error('Error loading bins:', error);
        showNotification('Failed to load bins data', 'error');
    }
}

// Display Bins on Map
function displayBinsOnMap(binsToDisplay) {
    // Clear existing markers
    binMarkers.forEach(marker => map.removeLayer(marker));
    binMarkers = [];
    
    binsToDisplay.forEach(bin => {
        const color = getBinColor(bin.fillLevel);
        const icon = createBinIcon(color);
        
        const marker = L.marker([bin.latitude, bin.longitude], { icon })
            .addTo(map)
            .bindPopup(createPopupContent(bin));
        
        binMarkers.push(marker);
    });
}

// Get Bin Color Based on Fill Level
function getBinColor(fillLevel) {
    if (fillLevel < 50) return '#22c55e'; // Green
    if (fillLevel < 80) return '#eab308'; // Yellow
    return '#ef4444'; // Red
}

// Create Custom Bin Icon
function createBinIcon(color) {
    return L.icon({
        iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-${getMarkerColorName(color)}.png`,
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });
}

function getMarkerColorName(hexColor) {
    if (hexColor === '#22c55e') return 'green';
    if (hexColor === '#eab308') return 'yellow';
    if (hexColor === '#ef4444') return 'red';
    return 'grey';
}

// Create Popup Content
function createPopupContent(bin) {
    const statusClass = bin.fillLevel < 50 ? 'status-low' : 
                       bin.fillLevel < 80 ? 'status-medium' : 'status-high';
    
    return `
        <div class="popup-content">
            <h4>🗑️ Bin #${bin.id}</h4>
            <p><strong>Location:</strong> ${bin.locationName || 'N/A'}</p>
            <p><strong>Type:</strong> ${bin.binType || 'General'}</p>
            <p><strong>Fill Level:</strong> ${bin.fillLevel}%</p>
            <p><strong>Capacity:</strong> ${bin.capacity}L</p>
            <span class="popup-status ${statusClass}">${bin.status}</span>
        </div>
    `;
}

// Generate Optimized Route
async function generateOptimizedRoute() {
    try {
        showNotification('Calculating optimal route...', 'info');
        
        const response = await fetch(`${API_BASE_URL}/routes/optimize`, {
            method: 'POST'
        });
        
        const routeData = await response.json();
        
        if (routeData.bins && routeData.bins.length > 0) {
            displayRoute(routeData);
            showRouteInfo(routeData);
            showNotification('Route optimized successfully!', 'success');
        } else {
            showNotification('No full bins found to create a route', 'warning');
        }
    } catch (error) {
        console.error('Error generating route:', error);
        showNotification('Failed to generate optimized route', 'error');
    }
}

// Display Route on Map
function displayRoute(routeData) {
    // Clear existing route
    if (routePolyline) {
        map.removeLayer(routePolyline);
    }
    if (routeDecorator) {
        map.removeLayer(routeDecorator);
    }
    
    // Create route coordinates
    const routeCoords = [
        [DEPOT_LAT, DEPOT_LON], // Start at depot
        ...routeData.bins.map(bin => [bin.latitude, bin.longitude]),
        [DEPOT_LAT, DEPOT_LON]  // Return to depot
    ];
    
    // Draw polyline
    routePolyline = L.polyline(routeCoords, {
        color: '#3b82f6',
        weight: 4,
        opacity: 0.7,
        dashArray: '10, 10',
        lineJoin: 'round'
    }).addTo(map);
    
    // Add arrows to show direction
    routeDecorator = L.polylineDecorator(routePolyline, {
        patterns: [
            {
                offset: 25,
                repeat: 100,
                symbol: L.Symbol.arrowHead({
                    pixelSize: 10,
                    polygon: false,
                    pathOptions: { stroke: true, color: '#3b82f6', weight: 2 }
                })
            }
        ]
    }).addTo(map);
    
    // Fit map to show entire route
    map.fitBounds(routePolyline.getBounds(), { padding: [50, 50] });
}

// Show Route Information Panel
function showRouteInfo(routeData) {
    const routeInfo = document.getElementById('routeInfo');
    routeInfo.style.display = 'block';
    
    document.getElementById('routeBinCount').textContent = routeData.bins.length;
    document.getElementById('routeDistance').textContent = routeData.totalDistance.toFixed(2);
    document.getElementById('routeTime').textContent = routeData.estimatedTime;
}

// Clear Route from Map
function clearRoute() {
    if (routePolyline) {
        map.removeLayer(routePolyline);
        routePolyline = null;
    }
    if (routeDecorator) {
        map.removeLayer(routeDecorator);
        routeDecorator = null;
    }
    
    document.getElementById('routeInfo').style.display = 'none';
    map.setView([DEPOT_LAT, DEPOT_LON], 12);
    showNotification('Route cleared', 'info');
}

// Refresh All Data
async function refreshData() {
    await Promise.all([
        loadDashboardStats(),
        loadBins()
    ]);
    showNotification('Data refreshed', 'success');
}

// Apply Filter
function applyFilter() {
    const filterValue = document.getElementById('filterSelect').value;
    
    let filteredBins = bins;
    
    switch (filterValue) {
        case 'full':
            filteredBins = bins.filter(bin => bin.fillLevel >= 80);
            break;
        case 'general':
            filteredBins = bins.filter(bin => bin.binType === 'General');
            break;
        case 'recyclable':
            filteredBins = bins.filter(bin => bin.binType === 'Recyclable');
            break;
        case 'organic':
            filteredBins = bins.filter(bin => bin.binType === 'Organic');
            break;
        default:
            filteredBins = bins;
    }
    
    displayBinsOnMap(filteredBins);
    showNotification(`Filter applied: ${filterValue}`, 'info');
}

// Show Notification
function showNotification(message, type = 'info') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    
    // Style the notification
    Object.assign(notification.style, {
        position: 'fixed',
        top: '20px',
        right: '20px',
        padding: '15px 25px',
        borderRadius: '8px',
        backgroundColor: getNotificationColor(type),
        color: 'white',
        fontWeight: '600',
        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
        zIndex: '10000',
        animation: 'slideIn 0.3s ease-out'
    });
    
    document.body.appendChild(notification);
    
    // Remove notification after 3 seconds
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease-out';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

function getNotificationColor(type) {
    switch (type) {
        case 'success': return '#22c55e';
        case 'error': return '#ef4444';
        case 'warning': return '#eab308';
        default: return '#3b82f6';
    }
}

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);
