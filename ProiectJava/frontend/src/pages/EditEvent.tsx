import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../utils/api';

const EditEvent = () => {
    const { eventId } = useParams(); 
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        nume: '',
        locatie: '',
        descriere: '',
        numarLocuri: 0
    });

    useEffect(() => {
        if (!eventId) return;

        api.get(`/event-manager/events/${eventId}`)
            .then(res => {
                const event = res.data.data; 
                setFormData({
                    nume: event.nume || '',
                    locatie: event.locatie || '',
                    descriere: event.descriere || '',
                    numarLocuri: event.numarLocuri || 0
                });
            })
            .catch(err => {
                console.error("Eroare la incarcare eveniment:", err);
                alert("Nu s-a putut incarca evenimentul.");
                navigate('/');
            });
    }, [eventId, navigate]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: name === 'numarLocuri' ? (parseInt(value) || 0) : value
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await api.put(`/event-manager/events/${eventId}`, formData);
            alert("Eveniment actualizat cu succes!");
            navigate('/');
        } catch (error) {
            console.error("Eroare la editare:", error);
            alert("Eroare la actualizare. Verifica daca esti proprietarul evenimentului.");
        }
    };

    return (
        <div className="card narrow">
            <h2>Editeaza Eveniment</h2>
            <form onSubmit={handleSubmit}>
                
                <div className="form-group">
                    <label>Nume Eveniment</label>
                    <input 
                        name="nume" 
                        value={formData.nume} 
                        onChange={handleChange} 
                    />
                </div>

                <div className="form-group">
                    <label>Locatie</label>
                    <input 
                        name="locatie" 
                        value={formData.locatie} 
                        onChange={handleChange} 
                    />
                </div>

                <div className="form-group">
                    <label>Descriere</label>
                    <input 
                        name="descriere" 
                        value={formData.descriere} 
                        onChange={handleChange} 
                    />
                </div>

                <div className="form-group">
                    <label>Numar Locuri</label>
                    <input 
                        type="number"
                        name="numarLocuri" 
                        value={formData.numarLocuri} 
                        onChange={handleChange} 
                    />
                </div>

                <div style={{ display: 'flex', gap: '10px', marginTop: '1.5rem' }}>
                    <button type="submit" className="btn-warning" style={{ flex: 1 }}>
                        Salveaza Modificarile
                    </button>
                    
                    <button 
                        type="button" 
                        className="btn-secondary"
                        onClick={() => navigate('/')}
                        style={{ flex: 1, background: '#e5e7eb', color: '#374151' }}
                    >
                        Anuleaza
                    </button>
                </div>
            </form>
        </div>
    );
};

export default EditEvent;