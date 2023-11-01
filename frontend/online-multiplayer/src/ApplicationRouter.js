import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Landing } from "./components/Landing/Landing";
import { Lobby } from "./components/Lobby/Lobby";
import { Login } from "./components/Login/Login";
import React from 'react'
import { RequireAuth } from "./RequireAuth";
import { Dashboard } from "./components/Dashboard/Dashboard";

export const ApplicationRouter = () => {

  return (
    <BrowserRouter>
        <Routes>
            <Route element={<Landing/>} path="/"/>
            <Route element={<Login/>} path="/login"/>
            <Route element={<RequireAuth allowedRoles={["ROLE_USER", "ROLE_ADMIN"]}/>}>
              <Route path="/dashboard" element={<Dashboard/>}/>
            </Route>
            <Route element={<RequireAuth allowedRoles={["ROLE_USER"]}/>}>
              <Route path="/lobby" element={<Lobby/>}/>
            </Route>
        </Routes>
    </BrowserRouter>
  )
}



