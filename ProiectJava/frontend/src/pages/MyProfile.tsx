import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/api';
import { jwtDecode } from 'jwt-decode';

interface Ticket {
    cod: string;
    event?: {
        nume: string;
        locatie: string;
    };
    package?: {
        nume: string;
    };
}

const MyProfile = () => {
    const [tickets, setTickets] = useState<Ticket[]>([]);
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded: any = jwtDecode(token);
                setEmail(decoded.email);
                
                fetchAllTickets(decoded.email);

            } catch (e) {
                console.error("Token invalid");
            }
        }
    }, []);


    const fetchAllTickets = async (userEmail: string) => {
        try {
            let allTickets: any[] = [];
            let page = 0;
            let hasMore = true;

            while (hasMore) {
                console.log(`Descarc pagina ${page} de bilete...`);
                
                const res = await api.get(`http://localhost:8082/api/client-service/clients/${userEmail}/tickets?page=${page}&size=5`);
                const data = res.data;
                
                let pageContent = [];
                if (data.content) pageContent = data.content;
                else if (Array.isArray(data)) pageContent = data;

                allTickets = [...allTickets, ...pageContent];

                if (data.totalPages && page < data.totalPages - 1) 
                {
                    page++; 
                } 
                else 
                {
                    hasMore = false; 
                }
                
                if (!data.content) hasMore = false;
            }

            console.log("Total bilete gasite:", allTickets.length);

            const processedTickets = allTickets.map((item: any) => item.data ? item.data : item);
            
            setTickets(processedTickets.reverse());

        } catch (err) {
            console.error("Eroare la incarcarea biletelor:", err);
        }
    };

    return (
        <div className="container">
            <div className="card" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
                <div>
                    <h2 style={{ margin: 0 }}>Profilul Meu</h2>
                    <span style={{ color: 'gray' }}>{email}</span>
                </div>
                <button 
                    onClick={() => navigate('/edit-profile')}
                    style={{ width: 'auto', backgroundColor: '#4f46e5' }}
                >
                    Editeaza Datele
                </button>
            </div>



            <h3>Biletele Mele ({tickets.length})</h3>
            
            {tickets.length === 0 ? (
                <p>Nu ai cumparat niciun bilet inca.</p>
            ) : (
                <div style={{ display: 'grid', gap: '15px' }}>
                    {tickets.map((t, index) => (
                        <div key={t.cod || index} className="card" style={{ 
                            padding: '15px', 
                            marginBottom: '0',
                            borderLeft: t.event ? '5px solid #4f46e5' : '5px solid #f59e0b' 
                        }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '10px' }}>
                                <div>
                                    {t.event ? (
                                        <>
                                            <span style={{ color: '#4f46e5', fontWeight: 'bold', textTransform: 'uppercase', fontSize: '0.8rem', letterSpacing: '1px' }}>Eveniment</span>
                                            <div style={{ fontSize: '1.2rem', fontWeight: 'bold', marginTop: '5px' }}>{t.event.nume}</div>
                                            <div style={{ color: '#666' }}>📍 {t.event.locatie}</div>
                                        </>
                                    ) : (
                                        <>
                                            <span style={{ color: '#f59e0b', fontWeight: 'bold', textTransform: 'uppercase', fontSize: '0.8rem', letterSpacing: '1px' }}>Pachet</span>
                                            <div style={{ fontSize: '1.2rem', fontWeight: 'bold', marginTop: '5px' }}>{t.package?.nume || 'Pachet necunoscut'}</div>
                                        </>
                                    )}
                                </div>
                                <div style={{ textAlign: 'right' }}>
                                    <div style={{ fontSize: '0.8rem', color: '#999' }}>COD BILET</div>
                                    <div style={{ fontFamily: 'monospace', background: '#f3f4f6', padding: '5px 10px', borderRadius: '5px', fontWeight: 'bold', border: '1px dashed #ccc' }}>
                                        {t.cod}
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default MyProfile;