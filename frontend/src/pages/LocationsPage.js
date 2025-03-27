import React, { useEffect, useState } from 'react';
import axios from 'axios';

function LocationsPage() {
    const [locations, setLocations] = useState([]);
    const [formData, setFormData] = useState({
        name: '',
        country: '',
        city: '',
        locationCode: ''
    });
    const [editId, setEditId] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');

    // 1) Get All Locations
    useEffect(() => {
        fetchLocations();
    }, []);

    const fetchLocations = async () => {
        try {
            const response = await axios.get('http://localhost:8080/locations');
            setLocations(response.data);
        } catch (error) {
            console.error('Lokasyonlar çekilirken hata oluştu:', error);
        }
    };

    // 2) Form input change
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    // 3) Add or update
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (editId) {
            // Update
            try {
                await axios.put(`http://localhost:8080/locations/${editId}`, formData);
                setEditId(null);
                setFormData({ name: '', country: '', city: '', locationCode: '' });
                fetchLocations();
                setErrorMessage('');
            } catch (error) {
                setErrorMessage(error.response.data.error || 'An error occurred while updating the location.');
                console.error('Error occurred while updating location:', error);
            }
        } else {
            // Add
            try {
                await axios.post('http://localhost:8080/locations', formData);
                setFormData({ name: '', country: '', city: '', locationCode: '' });
                fetchLocations();
                setErrorMessage('');
            } catch (error) {
                setErrorMessage(error.response.data.error || 'An error occurred while adding the location.');
                console.error('Error occurred while adding location:', error);
            }
        }
    };

    // 4) Update form
    const handleEdit = (location) => {
        setEditId(location.id);
        setFormData({
            name: location.name,
            country: location.country,
            city: location.city,
            locationCode: location.locationCode
        });
    };

    // 5) Delete
    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/locations/${id}`);
            fetchLocations();
            setErrorMessage('');
        } catch (error) {
            setErrorMessage(error.response.data.error || 'An error occurred while deleting the location.');
            console.error('Error occurred while deleting location:', error);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>Locations</h2>

            {/* Form Container */}
            <form onSubmit={handleSubmit} style={{ maxWidth: '500px', marginBottom: '20px' }}>
                <div style={{ marginBottom: '10px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>Name:</label>
                    <input
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        style={{ width: '100%', padding: '8px' }}
                        required
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>Country:</label>
                    <input
                        name="country"
                        value={formData.country}
                        onChange={handleChange}
                        style={{ width: '100%', padding: '8px' }}
                        required
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>City:</label>
                    <input
                        name="city"
                        value={formData.city}
                        onChange={handleChange}
                        style={{ width: '100%', padding: '8px' }}
                        required
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>Location Code:</label>
                    <input
                        name="locationCode"
                        value={formData.locationCode}
                        onChange={handleChange}
                        style={{ width: '100%', padding: '8px' }}
                        required
                    />
                </div>

                <button type="submit" style={{ padding: '10px 15px' }}>
                    {editId ? 'Update Location' : 'Add Location'}
                </button>
                {/* Display error if exists */}
                {errorMessage && (
                    <div style={{ color: 'red', marginBottom: '10px' }}>
                        {errorMessage}
                    </div>
                )}
            </form>

            {/* Listing */}
            <table style={{ width: '100%', borderCollapse: 'collapse' }} border="1">
                <thead>
                <tr>
                    <th style={{ padding: '10px' }}>Name</th>
                    <th style={{ padding: '10px' }}>Country</th>
                    <th style={{ padding: '10px' }}>City</th>
                    <th style={{ padding: '10px' }}>Location Code</th>
                    <th style={{ padding: '10px' }}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {locations.map((loc) => (
                    <tr key={loc.id}>
                        <td style={{ padding: '10px' }}>{loc.name}</td>
                        <td style={{ padding: '10px' }}>{loc.country}</td>
                        <td style={{ padding: '10px' }}>{loc.city}</td>
                        <td style={{ padding: '10px' }}>{loc.locationCode}</td>
                        <td style={{ padding: '10px', display: 'flex', gap: '5px', justifyContent: 'center' }}>
                            <button onClick={() => handleEdit(loc)}>Edit</button>
                            <button onClick={() => handleDelete(loc.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default LocationsPage;
