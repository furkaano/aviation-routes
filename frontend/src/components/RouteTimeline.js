import React from 'react';

function RouteTimeline({ route, onClose }) {
    if (!route) return null;

    const segments = getSegmentsFromRoute(route);

    return (
        <div style={styles.panel}>
            <h3>Route Timeline</h3>
            {route.description && <p>{route.description}</p>}

            {segments.map((seg, i) => (
                <div key={i} style={styles.segment}>
                    <div style={styles.circle}></div>
                    {i < segments.length - 1 && <div style={styles.connector}></div>}
                    <div style={styles.content}>
                        <strong>{seg.from} → {seg.to}</strong>
                        <div>Type: {seg.type}</div>
                    </div>
                </div>
            ))}

            <button onClick={onClose}>Close</button>
        </div>
    );
}

export default RouteTimeline;

// same getSegmentsFromRoute function as above
function getSegmentsFromRoute(route) {
    const segments = [];
    if (route.beforeFlightTransfer) {
        segments.push({
            from: route.beforeFlightTransfer.origin,
            to: route.beforeFlightTransfer.destination,
            type: route.beforeFlightTransfer.transportationType
        });
    }
    if (route.flight) {
        segments.push({
            from: route.flight.origin,
            to: route.flight.destination,
            type: route.flight.transportationType
        });
    }
    if (route.afterFlightTransfer) {
        segments.push({
            from: route.afterFlightTransfer.origin,
            to: route.afterFlightTransfer.destination,
            type: route.afterFlightTransfer.transportationType
        });
    }
    return segments;
}

const styles = {
    panel: {
        position: 'fixed',
        right: 0,
        top: 0,
        width: '300px',
        height: '100%',
        background: '#fff',
        padding: '20px',
        boxShadow: '0 0 10px rgba(0,0,0,0.3)',
        overflowY: 'auto',
    },
    segment: {
        position: 'relative',
        marginBottom: '40px',
        paddingLeft: '40px'
    },
    circle: {
        position: 'absolute',
        left: 0,
        width: '16px',
        height: '16px',
        borderRadius: '50%',
        backgroundColor: '#fff',
        border: '2px solid #333'
    },
    connector: {
        position: 'absolute',
        left: '7px',
        top: '16px',
        width: '2px',
        height: '40px',
        background: '#333'
    },
    content: {
        backgroundColor: '#f9f9f9',
        padding: '5px',
        borderRadius: '4px'
    }
};