import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../utils/api';

const PackageClientsPage = () => {
    const { id } = useParams(); 
    const [clients, setClients] = useState<any[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        loadClients();
    }, [id]);

const loadClients = async () => {
        try {
            const res = await api.get(`http://localhost:8082/api/client-service/clients/public/by-package/${id}?size=100`);
            
            const data = res.data;
            
            let list = [];
            if (data.content) list = data.content;
            else if (Array.isArray(data)) list = data;

            setClients(list.map((item: any) => item.data ? item.data : item));
        } catch (err) {
            console.error("Eroare la incarcarea clientilor pachetului:", err);
            alert("Nu am putut incarca lista de clienti. Verifica daca Client Service (8082) este pornit.");
        }
    };

    return (
        <div style={{ maxWidth: '800px', margin: '0 auto' }}>
            <button onClick={() => navigate(-1)} style={{ marginBottom: '20px', backgroundColor: '#6c757d', width: 'auto' }}>
                &larr; Inapoi
            </button>

            <div className="card">
                <h2>👥 Clienti Inscrisi la Pachet (ID: {id})</h2>
                
                {clients.length === 0 ? (
                    <p style={{ color: '#666', fontStyle: 'italic' }}>Niciun client nu a cumparat acest pachet momentan.</p>
                ) : (
                    <ul style={{ listStyle: 'none', padding: 0 }}>
                        {clients.map((client, index) => (
                            <li key={index} style={{ 
                                padding: '15px', 
                                borderBottom: '1px solid #eee',
                                display: 'flex', 
                                justifyContent: 'space-between',
                                alignItems: 'center'
                            }}>
                                <div>
                                    <strong>{client.nume} {client.prenume}</strong>
                                    <div style={{ fontSize: '0.85rem', color: '#666' }}>{client.email}</div>
                                </div>
                                <span style={{ 
                                    backgroundColor: '#d1fae5', 
                                    color: '#065f46', 
                                    padding: '5px 10px', 
                                    borderRadius: '15px',
                                    fontSize: '0.8rem',
                                    fontWeight: 'bold'
                                }}>
                                    Inscris
                                </span>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
};

export default PackageClientsPage;