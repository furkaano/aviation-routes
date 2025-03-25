import React from 'react';
import { Link } from 'react-router-dom';

function Sidebar() {
    const sidebarStyle = {
        width: '200px',
        backgroundColor: '#f1f1f1',
        padding: '10px',
        minHeight: '100vh', // Tam sayfa yüksekliği için
    };

    const linkStyle = {
        display: 'block',
        marginBottom: '10px',
        textDecoration: 'none',
        color: 'black'
    };

    return (
        <div style={sidebarStyle}>
            <h3>Menu</h3>
            <Link to="/locations" style={linkStyle}>Locations</Link>
            <Link to="/transportations" style={linkStyle}>Transportations</Link>
            <Link to="/routes" style={linkStyle}>Routes</Link>
        </div>
    );
}

export default Sidebar;