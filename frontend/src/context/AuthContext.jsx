import { createContext, useCallback, useContext, useMemo, useState } from 'react'
import {
  iniciarSesion as apiIniciarSesion,
  registrarUsuario as apiRegistrarUsuario,
} from '../lib/autenticacion'
import {
  borrarToken,
  guardarToken,
  leerToken,
  nombreUsuarioDeToken,
  tokenExpirado,
} from '../lib/token'

const AuthContext = createContext(null)

function tokenInicial() {
  const token = leerToken()
  if (!token || tokenExpirado(token)) {
    borrarToken()
    return null
  }
  return token
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(tokenInicial)

  const login = useCallback(async (nombreUsuario, pin) => {
    const nuevoToken = await apiIniciarSesion(nombreUsuario, pin)
    guardarToken(nuevoToken)
    setToken(nuevoToken)
  }, [])

  const registrar = useCallback(async (nombreUsuario, pin) => {
    await apiRegistrarUsuario(nombreUsuario, pin)
    // El backend no devuelve token al registrar: iniciamos sesión a continuación.
    const nuevoToken = await apiIniciarSesion(nombreUsuario, pin)
    guardarToken(nuevoToken)
    setToken(nuevoToken)
  }, [])

  const logout = useCallback(() => {
    borrarToken()
    setToken(null)
  }, [])

  const valor = useMemo(
    () => ({
      token,
      estaAutenticado: Boolean(token),
      nombreUsuario: nombreUsuarioDeToken(token),
      login,
      registrar,
      logout,
    }),
    [token, login, registrar, logout],
  )

  return <AuthContext.Provider value={valor}>{children}</AuthContext.Provider>
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth debe usarse dentro de <AuthProvider>')
  return ctx
}
