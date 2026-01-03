import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/api';

const CreatePackage = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        nume: '',
        locatie: '',
        descriere: '',
        numarLocuri: 0 
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
            const res = await api.post('/event-manager/event-packets', formData);
            const packageId = res.data.data.id;
            alert("Pachet creat! Acum adauga evenimente in el.");
            navigate(`/manage-package/${packageId}`);
        } catch (error) {
            console.error("Eroare creare pachet:", error);
            alert("Nu s-a putut crea pachetul.");
        }
    };

    return (
        <div className="card narrow">
            <h2>Creeaza Pachet Nou</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Nume Pachet</label>
                    <input name="nume" placeholder="Ex: Summer Pass" onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Locatie Generala</label>
                    <input name="locatie" placeholder="Ex: Arenele Romane" onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Descriere</label>
                    <input name="descriere" placeholder="Detalii..." onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Numar Pachete Disponibile</label>
                    <input type="number" name="numarLocuri" onChange={handleChange} required min="1" />
                </div>

                <button type="submit" style={{width: '100%'}}>
                    Pasul Urmator: Adauga Evenimente
                </button>
            </form>
        </div>
    );
};

export default CreatePackage;