import axios from 'axios';
import React, { useState } from 'react';

function App() {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isLogin, setIsLogin] = useState(true);

    const handleSubmit = async (event) => {
        event.preventDefault();
        const payload = { username, email, password };

        const apiURL = isLogin ? 'http://localhost:8080/auth/login' : 'http://localhost:8080/auth/register';

        try {
            const response = await axios.post(apiURL, payload);
            alert(response.data.message || response.data);
        } catch (error) {
            alert('Failed: ' + error.response.data.error || 'Error occurred');
        }
    };

    return (
        <div className="App">
            <h2>{isLogin ? "Login" : "Register"}</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username:</label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
                </div>
                {!isLogin && (
                    <div>
                        <label>Email:</label>
                        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                    </div>
                )}
                <div>
                    <label>Password:</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </div>
                <button type="submit">{isLogin ? "Login" : "Register"}</button>
            </form>
            <button onClick={() => setIsLogin(!isLogin)}>
                {isLogin ? "Go to Register" : "Go to Login"}
            </button>
        </div>
    );
}

export default App;
