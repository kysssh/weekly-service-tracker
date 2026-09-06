import { useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import { ApiError } from '../lib/api'

/**
 * Si una petición falla con 401/403, la sesión ya no sirve: cerramos sesión
 * para que <RutaProtegida> mande al usuario de vuelta al login.
 */
export function useSesionExpira(error) {
  const { logout } = useAuth()
  useEffect(() => {
    if (error instanceof ApiError && (error.status === 401 || error.status === 403)) {
      logout()
    }
  }, [error, logout])
}
