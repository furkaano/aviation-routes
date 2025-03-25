import React, { useEffect, useState } from 'react';
import axios from 'axios';
import RouteTimeline from "../components/RouteTimeline";

function RoutesPage() {
    const [locations, setLocations] = useState([]);
    const [origin, setOrigin] = useState('');
    const [destination, setDestination] = useState('');
    const [routes, setRoutes] = useState([]);

    const [selectedRouteForDetail, setSelectedRouteForDetail] = useState(null);
    const [selectedRouteForVisual, setSelectedRouteForVisual] = useState(null);

    useEffect(() => {
        fetchLocations();
    }, []);

    const fetchLocations = async () => {
        try {
            const res = await axios.get('http://localhost:8080/locations');
            setLocations(res.data);
        } catch (error) {
            console.error('Error occurred while getting locations:', error);
        }
    };

    const handleSearch = async () => {
        try {
            const res = await axios.get('http://localhost:8080/routes', {
                params: {
                    origin: origin,
                    destination: destination
                }
            });
            setRoutes(res.data);
            // Reset selected routes
            setSelectedRouteForDetail(null);
            setSelectedRouteForVisual(null);
        } catch (error) {
            console.error('Error occurred while searching route:', error);
        }
    };

    // Show inline detail
    const handleDetailClick = (route) => {
        setSelectedRouteForDetail(route);
        // If you only want one open at a time, you can hide the visual
        setSelectedRouteForVisual(null);
    };

    // Show timeline in a sidebar or pop-up
    const handleVisualClick = (route) => {
        setSelectedRouteForVisual(route);
        // Optionally hide detail
        setSelectedRouteForDetail(null);
    };

    const handleCloseDetail = () => {
        setSelectedRouteForDetail(null);
    };

    const handleCloseVisual = () => {
        setSelectedRouteForVisual(null);
    };

    return (
        <div>
            <h2>Routes</h2>
            <div style={{ marginBottom: '20px' }}>
                <label>Origin:</label>
                <select value={origin} onChange={(e) => setOrigin(e.target.value)}>
                    <option value="">Seçiniz</option>
                    {locations.map((loc) => (
                        <option key={loc.id} value={loc.id}>{loc.name}</option>
                    ))}
                </select>

                <label style={{ marginLeft: '10px' }}>Destination:</label>
                <select value={destination} onChange={(e) => setDestination(e.target.value)}>
                    <option value="">Seçiniz</option>
                    {locations.map((loc) => (
                        <option key={loc.id} value={loc.id}>{loc.name}</option>
                    ))}
                </select>

                <button style={{ marginLeft: '10px' }} onClick={handleSearch}>
                    Search
                </button>
            </div>

            {/* Route List */}
            <ul>
                {routes.map((r, index) => (
                    <li key={index}>
                        <strong>{r.description}</strong>
                        {' '}
                        <button onClick={() => handleDetailClick(r)}>Detail</button>
                        {' '}
                        <button onClick={() => handleVisualClick(r)}>Visual</button>
                    </li>
                ))}
            </ul>

            {/* Inline Detail */}
            {selectedRouteForDetail && (
                <div style={{ border: '1px solid #aaa', padding: '10px', marginTop: '10px' }}>
                    <h3>Route Details</h3>
                    <p><strong>Description:</strong> {selectedRouteForDetail.description}</p>

                    {selectedRouteForDetail.beforeFlightTransfer && (
                        <div>
                            <p><strong>Before Flight Transfer:</strong></p>
                            <p>Origin: {selectedRouteForDetail.beforeFlightTransfer.origin}</p>
                            <p>Destination: {selectedRouteForDetail.beforeFlightTransfer.destination}</p>
                            <p>Type: {selectedRouteForDetail.beforeFlightTransfer.transportationType}</p>
                        </div>
                    )}

                    {selectedRouteForDetail.flight && (
                        <div>
                            <p><strong>Flight:</strong></p>
                            <p>Origin: {selectedRouteForDetail.flight.origin}</p>
                            <p>Destination: {selectedRouteForDetail.flight.destination}</p>
                            <p>Type: {selectedRouteForDetail.flight.transportationType}</p>
                        </div>
                    )}

                    {selectedRouteForDetail.afterFlightTransfer && (
                        <div>
                            <p><strong>After Flight Transfer:</strong></p>
                            <p>Origin: {selectedRouteForDetail.afterFlightTransfer.origin}</p>
                            <p>Destination: {selectedRouteForDetail.afterFlightTransfer.destination}</p>
                            <p>Type: {selectedRouteForDetail.afterFlightTransfer.transportationType}</p>
                        </div>
                    )}

                    <button style={{ marginTop: '10px' }} onClick={handleCloseDetail}>
                        Close
                    </button>
                </div>
            )}

            {/* Side Panel Visual Timeline */}
            {selectedRouteForVisual && (
                <RouteTimeline route={selectedRouteForVisual} onClose={handleCloseVisual} />
            )}
        </div>
    );
}

export default RoutesPage;
