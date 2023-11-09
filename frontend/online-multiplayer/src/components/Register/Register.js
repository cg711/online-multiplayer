import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Cookies from 'universal-cookie';

export const Register = () => {

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

     const handleRegister = async ({username, password}) => {
        try {
            const res = await axios.post("http://localhost:8080/security/register", {password, username}, {
                headers: {
                    "Content-Type": "application/json"
                }
            });
            navigate("/login");
        } catch (err) {
            setError(err.message);
        }
    }

  return (
    <div className="flex flex-col w-fit">
        <h1 className="bg-blue-200">Register</h1>
        <p>Username</p>
        <input className="border-2" onChange={(e) => setUsername(e.target.value)} value={username} type="text"/>
        <p>Password</p>
        <input className="border-2" onChange={(e) => setPassword(e.target.value)} value={password} type="password"/>
        <button class="bg-slate-300 p-3 rounded-lg shadow-lg" onClick={() => handleRegister({username, password})}>Register</button>
        <p className="bg-red-200">{error}</p>
    </div>
  )
}
