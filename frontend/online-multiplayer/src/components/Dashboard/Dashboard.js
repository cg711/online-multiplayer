import React from 'react'
import { useNavigate } from 'react-router-dom';
import Cookies from 'universal-cookie';

export const Dashboard = () => {

  const cookies = new Cookies();

  const navigate = useNavigate();

  const logout = () => {
    sessionStorage.removeItem("user");
    cookies.remove("token");
    navigate("/");
  }

  return (
    <div>
      <h1>Dashboard</h1>
      <p>Hey, {JSON.parse(sessionStorage.getItem("user"))?.username}!</p>
      <button onClick={logout}>Logout</button>
    </div>

  )
}
