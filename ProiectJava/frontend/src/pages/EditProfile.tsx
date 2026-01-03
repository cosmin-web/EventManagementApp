import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/api';
import { jwtDecode } from 'jwt-decode';

const EditProfile = () => {
    const navigate = useNavigate();
    const [clientId, setClientId] = useState<string | null>(null);
    const [email, setEmail] = useState('');
    const [formData, setFormData] = useState({ nume: '', prenume: '', isPublic: true });

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            const decoded: any = jwtDecode(token);
            setEmail(decoded.email);
            api.get(`http://localhost:8082/api/client-service/clients/email/${decoded.email}`)
               .then(res => {
                   const clientData = res.data.data;
                   if (clientData) {
                       setClientId(clientData.id);
                       setFormData({
                           nume: clientData.nume || '',
                           prenume: clientData.prenume || '',
                           isPublic: clientData.isPublic !== undefined ? clientData.isPublic : true
                       });
                   }
               })
               .catch(err => console.error(err));
        }
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!clientId) return alert("Eroare: ID client lipsa.");
        try {
            await api.put(`http://localhost:8082/api/client-service/clients/${clientId}`, {
                ...formData, email: email 
            });
            alert("Profil actualizat!");
            navigate('/profile');
        } catch (error) { alert("Eroare la actualizare."); }
    };

    return (
        <div className="card narrow">
            <h2>Editare Profil</h2>
            <p style={{marginBottom: '1rem', color: 'var(--text-secondary)'}}>Email: {email}</p>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Nume</label>
                    <input name="nume" value={formData.nume} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label>Prenume</label>
                    <input name="prenume" value={formData.prenume} onChange={handleChange} />
                </div>
                <div style={{display:'flex', gap:'10px', marginTop:'1rem'}}>
                    <button type="submit" className="btn-success" style={{flex:1}}>Salveaza</button>
                    <button type="button" className="btn-danger" onClick={() => navigate('/profile')} style={{flex:1}}>Anuleaza</button>
                </div>
            </form>
        </div>
    );
};
export default EditProfile;