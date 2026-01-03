import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../utils/api';
import { jwtDecode } from 'jwt-decode';

const ManagePackage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [pkg, setPkg] = useState<any>(null);
    const [packageEvents, setPackageEvents] = useState<any[]>([]);
    const [myEvents, setMyEvents] = useState<any[]>([]);

    useEffect(() => { loadData(); }, [id]);

    const loadData = async () => {
        try {
            const resPkg = await api.get(`/event-manager/event-packets/${id}`);
            setPkg(resPkg.data.data);

            const resPkgEvents = await api.get(`/event-manager/event-packets/${id}/events`);
            setPackageEvents(resPkgEvents.data.map((item: any) => item.data));

            const resAllEvents = await api.get('/event-manager/events');
            const token = localStorage.getItem('token');

            if(token) 
            {
                const decoded: any = jwtDecode(token);
                const ownerId = Number(decoded.sub);
                const myEvts = (resAllEvents.data.content || []).map((item: any) => item.data).filter((ev: any) => ev.ownerId === ownerId);
                setMyEvents(myEvts);
            }
        } catch (err) { console.error(err); }
    };

    const addToPackage = async (eventId: number) => {
        try { await api.post(`/event-manager/event-packets/${id}/events/${eventId}`); loadData(); } catch (e) { alert("Eroare"); }
    };
    const removeFromPackage = async (eventId: number) => {
        try { await api.delete(`/event-manager/event-packets/${id}/events/${eventId}`); loadData(); } catch (e) { alert("Eroare"); }
    };

    const availableToAdd = myEvents.filter(ev => !packageEvents.some(pe => pe.eventId === ev.id));

    if (!pkg) return <div className="container">Se incarca...</div>;

    return (
        <div className="card">
            <div style={{display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                <div>
                    <h2>Gestionare: {pkg.nume}</h2>
                    <p>{pkg.descriere}</p>
                </div>
                <button className="btn-secondary" onClick={() => navigate('/')}>Inapoi</button>
            </div>
            <hr style={{margin:'20px 0', border:'0', borderTop:'1px solid var(--border-color)'}} />

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '30px' }}>
                <div>
                    <h3 style={{color: 'var(--success)'}}>Evenimente Incluse</h3>
                    {packageEvents.length === 0 && <p>Gol.</p>}
                    <ul style={{listStyle: 'none', padding: 0}}>
                        {packageEvents.map(pe => (
                            <li key={pe.eventId} style={{borderBottom:'1px solid #eee', padding:'10px', display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                                <span>{pe.eventName}</span>
                                <button className="btn-danger" style={{padding:'5px 10px', fontSize:'0.8rem'}} onClick={() => removeFromPackage(pe.eventId)}>Scoate</button>
                            </li>
                        ))}
                    </ul>
                </div>

                <div>
                    <h3 style={{color: 'var(--primary)'}}>Adauga Evenimente</h3>
                    {availableToAdd.length === 0 && <p>Nu mai ai evenimente disponibile.</p>}
                    <ul style={{listStyle: 'none', padding: 0}}>
                        {availableToAdd.map(ev => (
                            <li key={ev.id} style={{borderBottom:'1px solid #eee', padding:'10px', display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                                <span>{ev.nume}</span>
                                <button className="btn-success" style={{padding:'5px 10px', fontSize:'0.8rem'}} onClick={() => addToPackage(ev.id)}>Adauga</button>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};
export default ManagePackage;