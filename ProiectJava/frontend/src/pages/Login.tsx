import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/api';

const Login = () => {
    const [email, setEmail] = useState('');
    const [pass, setPass] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await api.post('/auth/login', { email: email, password: pass });
            localStorage.setItem('token', res.data.token); 
            navigate('/'); 
            window.location.reload(); 
        } catch (err) {
            alert("Login esuat!");
        }
    };

    return (
        <div className="card narrow">
            <h2 style={{textAlign:'center'}}>Autentificare</h2>
            <form onSubmit={handleLogin}>
                <label>Email</label>
                <input placeholder="Email" onChange={e => setEmail(e.target.value)} />
                
                <label>Parola</label>
                <input type="password" placeholder="Parola" onChange={e => setPass(e.target.value)} />
                
                <button type="submit" style={{width: '100%', marginTop: '10px'}}>Login</button>
            </form>
        </div>
    );
};
export default Login;