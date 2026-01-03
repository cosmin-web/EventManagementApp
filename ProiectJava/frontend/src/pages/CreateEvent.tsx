import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/api';

const CreateEvent = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        nume: '', locatie: '', descriere: '', numarLocuri: 0
    });

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
            await api.post('/event-manager/events', formData);
            alert("Eveniment creat cu succes!");
            navigate('/'); 
        } catch (error) {
            console.error(error);
            alert("Eroare la creare.");
        }
    };

    return (
        <div className="card narrow">
            <h2>Adauga Eveniment Nou</h2>
            <form onSubmit={handleSubmit}>
                <label>Nume Eveniment:</label>
                <input name="nume" placeholder="Ex: Gala Absolvire" value={formData.nume} onChange={handleChange} required />
                
                <label>Locatie:</label>
                <input name="locatie" placeholder="Ex: Aula Magna" value={formData.locatie} onChange={handleChange} required />
                
                <label>Descriere:</label>
                <input name="descriere" placeholder="Scurta descriere..." value={formData.descriere} onChange={handleChange} required />
                
                <label>Numar Locuri:</label>
                <input type="number" name="numarLocuri" placeholder="50" value={formData.numarLocuri || ''} onChange={handleChange} required min="1" />
                
                <button type="submit" style={{width:'100%', marginTop:'10px', backgroundColor:'#28a745'}}>Salveaza Evenimentul</button>
            </form>
        </div>
    );
};
export default CreateEvent;