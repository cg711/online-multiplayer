import React from 'react'
import { useAuth } from '../../hooks/useAuth';
import Cookies from 'universal-cookie';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export const Dashboard = () => {
    const {setAuth} = useAuth();
    const cookies = new Cookies();
    const navigate = useNavigate();
    const logout = () => {
        setAuth({});
        cookies.remove("token");
        navigate("/");
    }

    const handleTest = async () => {
        const data = await axios.get("http://localhost:8080/test/test");
        console.log(data.data);
    }

  return (
    <div>
        <h1>Dashboard</h1>
        <button onClick={() => logout()}>Logout</button>
        <button onClick={() => handleTest()}>Test</button>
    </div>
  )
}
