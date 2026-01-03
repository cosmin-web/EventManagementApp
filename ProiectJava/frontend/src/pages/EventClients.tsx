import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../utils/api';

const EventClients = () => {
    const { eventId } = useParams();
    const navigate = useNavigate();
    const [clients, setClients] = useState<any[]>([]);

    useEffect(() => {
        api.get(`http://localhost:8082/api/client-service/clients/public/by-event/${eventId}`)
           .then(res => {
               const rawData = res.data.content;
               setClients(rawData.map((c: any) => c.data));
           })
           .catch(err => console.error(err));
    }, [eventId]);

    return (
        <div className="card">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '1rem' }}>
                <div>
                    <h2>Clienti Inscrisi</h2>
                    <p style={{ color: 'var(--text-secondary)' }}>Pentru evenimentul ID: {eventId}</p>
                </div>
                <button onClick={() => navigate('/')} style={{ background: 'var(--text-secondary)' }}>
                    &larr; Inapoi
                </button>
            </div>

            {clients.length === 0 ? (
                <p style={{ textAlign: 'center', padding: '20px', color: 'var(--text-secondary)' }}>
                    Niciun client inscris momentan.
                </p>
            ) : (
                <ul style={{ listStyle: 'none', padding: 0 }}>
                    {clients.map((c: any) => (
                        <li key={c.id} style={{ 
                            padding: '1rem', 
                            borderBottom: '1px solid var(--border-color)',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'space-between'
                        }}>
                            <div>
                                <strong style={{ fontSize: '1.1rem' }}>{c.nume} {c.prenume}</strong>
                                <div style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>{c.email}</div>
                            </div>
                            <span style={{ 
                                padding: '4px 10px', 
                                background: '#d1fae5', 
                                color: '#065f46', 
                                borderRadius: '20px', 
                                fontSize: '0.85rem',
                                fontWeight: 'bold'
                            }}>
                                Inscris
                            </span>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default EventClients;