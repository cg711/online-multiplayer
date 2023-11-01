import { useAuth } from "./hooks/useAuth";

const { useLocation, Outlet, Navigate } = require("react-router-dom");


export const RequireAuth = ({ allowedRoles }) => {
    const { auth } = useAuth();
    const location = useLocation();

    console.log(auth);

    return (
        [auth?.role]?.find(role => allowedRoles?.includes(role))
            ? <Outlet/>
            : auth?.username ? <Navigate to="/" state={{from: location}} replace />
            : <Navigate to="/login" state={{from: location}} replace />
    );
}