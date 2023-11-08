import React from 'react'
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Cookies from 'universal-cookie';
import { useAuth } from '../../hooks/useAuth.js';

export const Login = () => {

    const {setAuth} = useAuth();

    const navigate = useNavigate();

    const cookies = new Cookies();

     const handleLogin = async ({username, password}) => {
        try {
            const basicHeader = btoa(`${username}:${password}`);
            const res = await axios.get("http://localhost:8080/security/login", {
                headers: {
                    Authorization: `Basic ${basicHeader}`
                }
            });
            const tokenRaw = res.headers.get('Authorization').split(' ')[1];
            console.log(tokenRaw);
            cookies.set("token", tokenRaw);
            setAuth({...res.data});
            navigate("/lobby");
        } catch (err) {
            console.log("error authenticating");
        }
    }

  return (
    <div>
        <button onClick={() => handleLogin({username: "cg711", password: "password"})}>Login</button>
    </div>
  )
}
