import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Landing } from "./components/Landing/Landing";
import { Lobby } from "./components/Lobby/Lobby";
import { Login } from "./components/Login/Login";
import {Register} from "./components/Register/Register";
import React from 'react'
import { RequireAuth } from "./RequireAuth";
import { Dashboard } from "./components/Dashboard/Dashboard";
import { Game } from "./components/Game/Game";

export const ApplicationRouter = () => {

  return (
    <BrowserRouter>
        <Routes>
            <Route element={<Landing/>} path="/"/>
            <Route element={<Login/>} path="/login"/>
            <Route element={<Register/>} path="/register"/>
            <Route element={<RequireAuth allowedRoles={["ROLE_USER", "ROLE_ADMIN"]}/>}>
              <Route path="/dashboard" element={<Dashboard/>}/>
            </Route>
            <Route element={<RequireAuth allowedRoles={["ROLE_USER"]}/>}>
              <Route path="/lobby" element={<Lobby/>}/>
            </Route>
            <Route element={<RequireAuth allowedRoles={["ROLE_USER"]}/>}>
              <Route path="/game/:gameId" element={<Game/>}/>
            </Route>
        </Routes>
    </BrowserRouter>
  )
}



