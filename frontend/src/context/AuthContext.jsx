import { createContext, useCallback, useContext, useMemo, useState } from 'react'
import {
  CuentaCreadaSinSesion,
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

  const iniciarConToken = useCallback((nuevoToken) => {
    if (!nuevoToken) throw new Error('El servidor no devolvió un token.')
    guardarToken(nuevoToken)
    setToken(nuevoToken)
  }, [])

  const login = useCallback(
    async (nombreUsuario, pin) => {
      iniciarConToken(await apiIniciarSesion(nombreUsuario, pin))
    },
    [iniciarConToken],
  )

  const registrar = useCallback(
    async (nombreUsuario, pin) => {
      await apiRegistrarUsuario(nombreUsuario, pin)
      // La cuenta ya se creó. Si el login automático que sigue falla, avisamos
      // con un error propio para no decir "no se pudo crear la cuenta".
      try {
        iniciarConToken(await apiIniciarSesion(nombreUsuario, pin))
      } catch {
        throw new CuentaCreadaSinSesion()
      }
    },
    [iniciarConToken],
  )

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
