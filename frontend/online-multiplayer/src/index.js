import React from 'react';
import ReactDOM from 'react-dom/client';
import { ApplicationRouter } from './ApplicationRouter';
import axios from 'axios';
import Cookies from 'universal-cookie';
import { AuthProvider } from './context/AuthProvider';

// AXIOS AUTH CONFIG WITH HTTPONLY COOKIE STORAGE
const cookies = new Cookies();

axios.interceptors.request.use(
  async (config) => {
    const token = cookies.get("token");

    if(token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }

    return config;
  }, (error) => {
    if (error.response.status === 401) {
      window.location.href = '/login';
    }
    return Promise.reject();
  }
)

axios.defaults.headers.common['Authorization'] = `Bearer ${cookies.get("token")}`;

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  // <React.StrictMode>
    <AuthProvider>
      <ApplicationRouter/>
    </AuthProvider>
  // </React.StrictMode>
);

