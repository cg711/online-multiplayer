import React from 'react';
import ReactDOM from 'react-dom/client';
import { ApplicationRouter } from './ApplicationRouter';
import axios from 'axios';
import Cookies from 'universal-cookie';
import "./input.css";

// AXIOS AUTH CONFIG WITH HTTPONLY COOKIE JWT STORAGE
const cookies = new Cookies();

axios.interceptors.request.use(
  async (config) => config, 
  (error) => {
    if (cookies.get("token") == null) {
      window.location.href = '/login';
    } else if (error.response.status === 404) {
      window.location.href = '/dashboard';
    }
    return Promise.reject();
  }
)

axios.defaults.headers.common['Authorization'] = `Bearer ${cookies.get("token")}`;

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  // <React.StrictMode>
      <ApplicationRouter/>
  // </React.StrictMode>
);

