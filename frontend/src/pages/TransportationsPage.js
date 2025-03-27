import React, { useEffect, useState } from 'react';
import axios from 'axios';

function TransportationsPage() {
    const [transportations, setTransportations] = useState([]);
    const [formData, setFormData] = useState({
        originLocationId: '',
        destinationLocationId: '',
        transportationType: ''
    });
    const [editId, setEditId] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(() => {
        fetchTransportations();
    }, []);

    const fetchTransportations = async () => {
        try {
            const response = await axios.get('http://localhost:8080/transportations');
            setTransportations(response.data);
        } catch (error) {
            console.error('Error occurred while getting transportations:', error);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Build the payload matching the backend’s expectation
        const payload = {
            originLocation: { id: formData.originLocationId },
            destinationLocation: { id: formData.destinationLocationId },
            transportationType: formData.transportationType
        };

        try {
            if (editId) {
                // PUT for update
                await axios.put(`http://localhost:8080/transportations/${editId}`, payload);
            } else {
                // POST for create
                await axios.post(`http://localhost:8080/transportations`, payload);
            }
            // Reset state or refetch after success
            setFormData({ originLocationId: '', destinationLocationId: '', transportationType: '' });
            setEditId(null);
            fetchTransportations();
            setErrorMessage('');
        } catch (error) {
            if(editId){
                setErrorMessage(error.response.data.error || 'An error occurred while aupdating the location.');
                console.error('Error occurred while updating transportation:', error);
            } else {
                setErrorMessage(error.response.data.error || 'An error occurred while adding the location.');
                console.error('Error occurred while adding transportation:', error);
            }
        }
    };


    const handleEdit = (item) => {
        setEditId(item.id);
        setFormData({
            originLocationId: item.originLocation.id,
            destinationLocationId: item.destinationLocation.id,
            transportationType: item.transportationType
        });
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/transportations/${id}`);
            fetchTransportations();
            setErrorMessage('');
        } catch (error) {
            setErrorMessage(error.response.data.error || 'An error occurred while deleting the location.');
            console.error('Error occurred while deleting transportation:', error);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>Transportations</h2>

            {/* Form Container */}
            <form onSubmit={handleSubmit} style={{ maxWidth: '500px', marginBottom: '20px' }}>
                <div style={{ marginBottom: '10px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>Origin Location ID:</label>
                    <input
                        name="originLocationId"
                        value={formData.originLocationId}
                        onChange={handleChange}
                        style={{ width: '100%', padding: '8px' }}
                        required
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>Destination Location ID:</label>
                    <input
                        name="destinationLocationId"
                        value={formData.destinationLocationId}
                        onChange={handleChange}
                        style={{ width: '100%', padding: '8px' }}
                        required
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <label style={{ display: 'block', marginBottom: '5px' }}>Transportation Type:</label>
                    <select
                        name="transportationType"
                        value={formData.transportationType}
                        onChange={handleChange}
                        style={{ width: '100%', padding: '8px' }}
                        required
                    >
                        <option value="">Seçiniz</option>
                        <option value="FLIGHT">FLIGHT</option>
                        <option value="BUS">BUS</option>
                        <option value="SUBWAY">SUBWAY</option>
                        <option value="UBER">UBER</option>
                    </select>
                </div>

                <button type="submit" style={{ padding: '10px 15px' }}>
                    {editId ? 'Update Transportation' : 'Add Transportation'}
                </button>
                {/* Display error if exists */}
                {errorMessage && (
                    <div style={{ color: 'red', marginBottom: '10px' }}>
                        {errorMessage}
                    </div>
                )}
            </form>

            {/* Listeleme */}
            <table style={{ width: '100%', borderCollapse: 'collapse' }} border="1">
                <thead>
                <tr>
                    <th style={{ padding: '10px' }}>Origin</th>
                    <th style={{ padding: '10px' }}>Destination</th>
                    <th style={{ padding: '10px' }}>Type</th>
                    <th style={{ padding: '10px' }}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {transportations.map((trans) => (
                    <tr key={trans.id}>
                        <td style={{ padding: '10px' }}>{JSON.stringify(trans.originLocation)}</td>
                        {/*<td>*/}
                        {/*     {trans.originLocation.name}, {trans.originLocation.city},
                                {trans.originLocation.country}, {trans.originLocation.locationCode}*/}
                        {/*</td>*/}
                        <td style={{ padding: '10px' }}>{JSON.stringify(trans.destinationLocation)}</td>
                        {/*<td>*/}
                        {/*     {destinationLocation.originLocation.name}, {destinationLocation.originLocation.city},
                                {destinationLocation.originLocation.country}, {destinationLocation.originLocation.locationCode}*/}
                        {/*</td>*/}
                        <td style={{ padding: '10px' }}>{trans.transportationType}</td>
                        <td style={{ padding: '10px', display: 'flex', gap: '5px', justifyContent: 'center' }}>
                            <button onClick={() => handleEdit(trans)}>Edit</button>
                            <button onClick={() => handleDelete(trans.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default TransportationsPage;
