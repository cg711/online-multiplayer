import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Cookies from 'universal-cookie';

export const Login = () => {

    const navigate = useNavigate();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const cookies = new Cookies();

    // if user is already logged in
    useEffect(() => {
        if(cookies.get("token") != null) {
            navigate("/dashboard");
        }
    }, []);

     const handleLogin = async ({username, password}) => {
        try {
            const basicHeader = btoa(`${username}:${password}`);
            const res = await axios.get("http://localhost:8080/security/login", {
                headers: {
                    Authorization: `Basic ${basicHeader}`
                }
            });
            const tokenRaw = res.headers.get('Authorization').split(' ')[1];
            cookies.set("token", tokenRaw);
            sessionStorage.setItem("user", JSON.stringify(res.data));
            navigate("/dashboard");
        } catch (err) {
            setError(err.message);
        }
    }

  return (
    <div className="flex flex-col w-fit">
        <h1 className="bg-blue-200">Login</h1>
        <p>Username</p>
        <input className="border-2" onChange={(e) => setUsername(e.target.value)} value={username} type="text"/>
        <p>Password</p>
        <input className="border-2" onChange={(e) => setPassword(e.target.value)} value={password} type="password"/>
        <button class="bg-slate-300 p-3 rounded-lg shadow-lg" onClick={() => handleLogin({username, password})}>Login</button>
        <p className="bg-red-200">{error}</p>
    </div>
  )
}
