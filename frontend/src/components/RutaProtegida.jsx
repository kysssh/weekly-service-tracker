import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

/** Envuelve rutas que requieren sesión. Sin token, redirige a /login. */
export default function RutaProtegida({ children }) {
  const { estaAutenticado } = useAuth()
  const location = useLocation()

  if (!estaAutenticado) {
    return <Navigate to="/login" replace state={{ from: location }} />
  }
  return children
}
