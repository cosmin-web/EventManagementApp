import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Login from './pages/Login';
import EventsPage from './pages/EventsPage';
import MyProfile from './pages/MyProfile';
import EventClients from './pages/EventClients';
import CreateEvent from './pages/CreateEvent';
import EditProfile from './pages/EditProfile';
import EditEvent from './pages/EditEvent';
import CreatePackage from './pages/CreatePackage';
import ManagePackage from './pages/ManagePackage';
import PackageClientsPage from './pages/PackageClientsPage'; 
import { jwtDecode } from 'jwt-decode';

function Navbar() {
    const token = localStorage.getItem('token');
    let role = null;
    
    if (token) {
        try {
            const decoded: any = jwtDecode(token);
            role = decoded.role;
        } catch (e) {
            console.error("Token invalid");
        }
    }

    const handleLogout = () => {
        localStorage.removeItem('token');
        window.location.href = '/login';
    };

    return (
        <nav className="navbar">
            <div className="container navbar-content">
                <div className="nav-links">
                    <Link to="/" className="logo">MY EVENTS</Link>
                    
                    {role === 'CLIENT' && (
                         <Link to="/profile">Profilul Meu</Link>
                    )}
                    
                    {role === 'OWNER_EVENT' && (
                        <>
                             <Link to="/create-event">+ Eveniment</Link>
                             <Link to="/create-package">+ Pachet</Link>
                        </>
                    )}
                </div>

                <div>
                    {!token ? (
                        <Link to="/login" className="btn">Login</Link>
                    ) : (
                        <button onClick={handleLogout} style={{backgroundColor: '#dc2626'}}>Logout</button>
                    )}
                </div>
            </div>
        </nav>
    );
}

function App() {
    return (
        <BrowserRouter>
            <Navbar />
            <div className="container">
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/" element={<EventsPage />} />
                    
                    <Route path="/profile" element={<MyProfile />} />
                    <Route path="/edit-profile" element={<EditProfile />} />
                    
                    <Route path="/create-event" element={<CreateEvent />} />
                    <Route path="/edit-event/:eventId" element={<EditEvent />} />
                    <Route path="/event-clients/:eventId" element={<EventClients />} />

                    <Route path="/create-package" element={<CreatePackage />} />
                    <Route path="/manage-package/:id" element={<ManagePackage />} />
                    
                    <Route path="/package-clients/:id" element={<PackageClientsPage />} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;