import React, { useEffect, useState } from 'react';
import api from '../utils/api';
import { jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';


const PackageEventNames = ({ packageId }: { packageId: number }) => {

    const [eventsList, setEventsList] = useState<{ name: string, desc: string }[]>([]);

    useEffect(() => {

        api.get(`/event-manager/event-packets/${packageId}/events`)
            .then(res => {
                const list = res.data.map((item: any) => ({
                    name: item.data.eventName,
                    desc: item.data.eventDescription || "Fara descriere" 
                }));
                setEventsList(list);
            })
            .catch(err => console.error(`Eroare detalii pachet ${packageId}`, err));
    }, [packageId]);

    if (eventsList.length === 0) return null;

    return (
        <div style={{ 
            marginTop: '10px', 
            marginBottom: '10px', 
            fontSize: '0.9rem', 
            color: '#4b5563', 
            backgroundColor: '#f9fafb', 
            padding: '10px', 
            borderRadius: '8px',
            border: '1px solid #e5e7eb'
        }}>
            <strong style={{ display:'block', marginBottom:'5px', color:'#111827' }}>Include:</strong>
            <ul style={{ margin: '0', paddingLeft: '20px' }}>
                {eventsList.map((ev, idx) => (
                    <li key={idx} style={{ marginBottom: '8px' }}>
                        <div style={{ fontWeight: '600', color: '#374151' }}>{ev.name}</div>
                        <div style={{ fontSize: '0.8rem', color: '#6b7280', fontStyle: 'italic', lineHeight: '1.2' }}>
                            {ev.desc}
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};




const EventsPage = () => {

    const [events, setEvents] = useState<any[]>([]);
    const [packages, setPackages] = useState<any[]>([]);
    
    const [userRole, setUserRole] = useState<string>('');
    const [userId, setUserId] = useState<number>(0);
    const [userEmail, setUserEmail] = useState<string>('');
    
    const [currentEventPage, setCurrentEventPage] = useState(0);
    const [totalEventPages, setTotalEventPages] = useState(0);
    const [currentPackagePage, setCurrentPackagePage] = useState(0);
    const [totalPackagePages, setTotalPackagePages] = useState(0);

    const PAGE_SIZE = 5; 
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded: any = jwtDecode(token);
                setUserRole(decoded.role); 
                setUserId(Number(decoded.sub));
                setUserEmail(decoded.email);
            } catch (e) {}
        }
        loadEvents(0);
        loadPackages(0);
    }, []);



    const loadEvents = async (page: number) => {
        try {
            const res = await api.get(`/event-manager/events?page=${page}&size=${PAGE_SIZE}`);
            const data = res.data;

            let rawList = data.content || (Array.isArray(data) ? data : []);
            setEvents(rawList.map((item: any) => item.data ? item.data : item));

            if (data.totalPages !== undefined) setTotalEventPages(data.totalPages);

            setCurrentEventPage(page);
        } catch (err) { console.error(err); }
    };

    const loadPackages = async (page: number) => {
        try {
            const res = await api.get(`/event-manager/event-packets?page=${page}&size=${PAGE_SIZE}`);
            const data = res.data;

            let rawList = data.content || (Array.isArray(data) ? data : []);
            const processedPackages = rawList.map((item: any) => item.data ? item.data : item);

            setPackages(processedPackages);
            
            if (data.totalPages !== undefined && data.totalPages > 0) 
            {
                setTotalPackagePages(data.totalPages);
            } 
            else 
            {
                 if (processedPackages.length === PAGE_SIZE) setTotalPackagePages((prev) => Math.max(prev, page + 2));
                 else setTotalPackagePages(page + 1);
            }
            setCurrentPackagePage(page);
        } catch (err) { console.error(err); }
    };

    // Navigare
    const handleNextPackage = () => { if (packages.length === PAGE_SIZE) loadPackages(currentPackagePage + 1); };
    const handlePrevPackage = () => { if (currentPackagePage > 0) loadPackages(currentPackagePage - 1); };
    const handleNextEvent = () => { if (currentEventPage < totalEventPages - 1) loadEvents(currentEventPage + 1); };
    const handlePrevEvent = () => { if (currentEventPage > 0) loadEvents(currentEventPage - 1); };

    // Actiuni
    const buyTicket = async (eventId: number) => {
        if (!userEmail) return alert("Logheaza-te!");
        try {
            await api.post(`http://localhost:8082/api/client-service/clients/${userEmail}/tickets/events/${eventId}`);
            alert("Bilet cumparat!");
            setTimeout(() => loadEvents(currentEventPage), 500); 
        } catch (e) { alert("Eroare cumparare bilet."); }
    };

    const buyPackage = async (packageId: number) => {
        if (!userEmail) return alert("Logheaza-te!");
        try {
            await api.post(`http://localhost:8082/api/client-service/clients/${userEmail}/tickets/packages/${packageId}`);
            alert("Pachet cumparat!");
            setTimeout(() => loadPackages(currentPackagePage), 500);
        } catch (e) { alert("Eroare cumparare pachet."); }
    };

    const deletePackage = async (id: number) => {
        if(!confirm("Esti sigur ca vrei sa stergi acest pachet?")) return;
        try { 
            await api.delete(`/event-manager/event-packets/${id}`); 
            alert("Pachet sters cu succes!");
            loadPackages(currentPackagePage); 
        } catch(e) { alert("Eroare la stergere pachet."); }
    }

    const deleteEvent = async (id: number) => {
        if(!confirm("Esti sigur ca vrei sa stergi acest eveniment?")) return;
        try { 
            await api.delete(`/event-manager/events/${id}`); 
            alert("Eveniment sters cu succes!");
            loadEvents(currentEventPage); 
        } catch(e) { alert("Eroare la stergere eveniment (posibil sa aiba bilete vandute)."); }
    }

    return (
        <div>
            <div style={{display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:'20px'}}>
                <h1>📦 Pachete Promotionale</h1>
                <span style={{color:'#666', fontWeight: '500'}}>Pagina {currentPackagePage + 1} din {totalPackagePages}</span>
            </div>

            <div className="grid-container">
                {packages.map((pkg, index) => (
                    <div key={pkg.id || index} className="card" style={{borderLeft: '5px solid #6f42c1'}}>
                        <h3>{pkg.nume}</h3>
                        <p style={{color: '#666'}}>📍 {pkg.locatie}</p>
                        <p>{pkg.descriere}</p>
                        
                        <PackageEventNames packageId={pkg.id} />
                        
                        <p style={{marginTop: '10px', fontWeight: '500', color: '#374151'}}>
                            🎟 Locuri Disponibile: <strong>{pkg.availableTickets || 0}</strong>
                        </p>

                        {userRole === 'CLIENT' && (
                            <button 
                                onClick={() => buyPackage(pkg.id)} 
                                disabled={(pkg.availableTickets || 0) <= 0} 
                                style={{
                                    width:'100%', 
                                    marginTop:'10px',
                                    backgroundColor: (pkg.availableTickets || 0) <= 0 ? '#9ca3af' : undefined,
                                    cursor: (pkg.availableTickets || 0) <= 0 ? 'not-allowed' : 'pointer'
                                }}
                            >
                                {(pkg.availableTickets || 0) <= 0 ? "Stoc Epuizat" : "Cumpara Pachet"}
                            </button>
                        )}

                        {userRole === 'OWNER_EVENT' && pkg.ownerId === userId && (
                            <div style={{display:'flex', gap:'5px', marginTop:'10px'}}>
                                <button onClick={() => navigate(`/manage-package/${pkg.id}`)} style={{flex:1, backgroundColor:'#f59e0b', fontSize:'0.9rem', padding:'8px'}}>Gestioneaza</button>
                                <button onClick={() => navigate(`/package-clients/${pkg.id}`)} style={{flex:1, backgroundColor:'#6c757d', fontSize:'0.9rem', padding:'8px'}}>Clienti</button>
                                <button onClick={() => deletePackage(pkg.id)} style={{flex:1, backgroundColor:'#dc3545', fontSize:'0.9rem', padding:'8px'}}>Sterge</button>
                            </div>
                        )}
                    </div>
                ))}
                {packages.length === 0 && <p style={{gridColumn: '1/-1', textAlign:'center'}}>Nu exista pachete.</p>}
            </div>

            <div style={{display:'flex', justifyContent:'center', gap:'20px', marginTop:'10px', marginBottom: '40px'}}>
                <button onClick={handlePrevPackage} disabled={currentPackagePage === 0} style={{width:'auto', padding:'8px 20px', backgroundColor: currentPackagePage === 0 ? '#e5e7eb' : '#4f46e5', color: currentPackagePage === 0 ? '#9ca3af' : 'white', cursor: currentPackagePage === 0 ? 'not-allowed' : 'pointer'}}>&larr; Pachete Anterioare</button>
                <button onClick={handleNextPackage} disabled={packages.length < PAGE_SIZE} style={{width:'auto', padding:'8px 20px', backgroundColor: packages.length < PAGE_SIZE ? '#e5e7eb' : '#4f46e5', color: packages.length < PAGE_SIZE ? '#9ca3af' : 'white', cursor: packages.length < PAGE_SIZE ? 'not-allowed' : 'pointer'}}>Pachete Urmatoare &rarr;</button>
            </div>

            <hr style={{margin: '40px 0', border: '0', borderTop: '1px solid #ddd'}}/>





            <div style={{display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:'20px'}}>
                <h1>🎫 Evenimente Individuale</h1>
                <span style={{color:'#666', fontWeight: '500'}}>Pagina {currentEventPage + 1} din {totalEventPages > 0 ? totalEventPages : 1}</span>
            </div>

            <div className="grid-container">
                {events.map((ev, index) => (
                   <div key={ev.id || index} className="card">
                       <h3>{ev.nume}</h3>
                       <p style={{color: '#666'}}>📍 {ev.locatie}</p>
                       <p>{ev.descriere}</p>
                       <p style={{marginTop: '10px', fontWeight: '500', color: '#374151'}}>
                           🎟 Locuri Disponibile: <strong>{ev.availableTickets}</strong>
                       </p>

                       {userRole === 'CLIENT' && (
                           <button 
                               onClick={() => buyTicket(ev.id)} 
                               disabled={(ev.availableTickets || 0) <= 0}
                               style={{
                                   width: '100%', 
                                   marginTop: '10px',
                                   backgroundColor: (ev.availableTickets || 0) <= 0 ? '#9ca3af' : undefined,
                                   cursor: (ev.availableTickets || 0) <= 0 ? 'not-allowed' : 'pointer'
                               }}
                           >
                               {(ev.availableTickets || 0) <= 0 ? "Stoc Epuizat" : "Cumpara Bilet"}
                           </button>
                       )}
                       
                       {userRole === 'OWNER_EVENT' && ev.ownerId === userId && (
                           <div style={{display:'flex', gap:'5px', marginTop:'10px'}}>
                               <button onClick={() => navigate(`/edit-event/${ev.id}`)} style={{flex:1, backgroundColor:'#f59e0b', fontSize:'0.9rem', padding:'8px'}}>Edit</button>
                               <button onClick={() => navigate(`/event-clients/${ev.id}`)} style={{flex:1, backgroundColor:'#6c757d', fontSize:'0.9rem', padding:'8px'}}>Clienti</button>
                               <button onClick={() => deleteEvent(ev.id)} style={{flex:1, backgroundColor:'#dc3545', fontSize:'0.9rem', padding:'8px'}}>Sterge</button>
                           </div>
                       )}
                   </div> 
                ))}
            </div>


            <div style={{display:'flex', justifyContent:'center', gap:'20px', marginTop:'30px'}}>
                <button onClick={handlePrevEvent} disabled={currentEventPage === 0} style={{width:'auto', padding:'10px 25px', backgroundColor: currentEventPage === 0 ? '#e5e7eb' : '#4f46e5', color: currentEventPage === 0 ? '#9ca3af' : 'white', cursor: currentEventPage === 0 ? 'not-allowed' : 'pointer'}}>&larr; Evenimente Anterioare</button>
                <button onClick={handleNextEvent} disabled={currentEventPage >= totalEventPages - 1} style={{width:'auto', padding:'10px 25px', backgroundColor: currentEventPage >= totalEventPages - 1 ? '#e5e7eb' : '#4f46e5', color: currentEventPage >= totalEventPages - 1 ? '#9ca3af' : 'white', cursor: currentEventPage >= totalEventPages - 1 ? 'not-allowed' : 'pointer'}}>Evenimente Urmatoare &rarr;</button>
            </div>
            
            {events.length === 0 && <p style={{textAlign:'center', marginTop: '20px'}}>Nu exista evenimente.</p>}
        </div>
    );
};
export default EventsPage;