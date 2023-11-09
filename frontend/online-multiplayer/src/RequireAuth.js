import { useEffect } from "react";

const { useLocation, Outlet, Navigate } = require("react-router-dom");

export const RequireAuth = ({ allowedRoles }) => {
    const auth = JSON.parse(sessionStorage.getItem("user"));
    const location = useLocation();
    return (
        [auth?.role]?.find(role => allowedRoles?.includes(role))
            ? <Outlet/>
            : auth?.username ? <Navigate to="/" state={{from: location}} replace />
            : <Navigate to="/login" state={{from: location}} replace />
    );
}