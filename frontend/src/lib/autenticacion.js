import { api } from './api'

/**
 * POST /auth — devuelve el JWT.
 * 401 si el usuario o el PIN son incorrectos.
 */
export async function iniciarSesion(nombreUsuario, pin) {
  const datos = await api('/auth', {
    method: 'POST',
    auth: false,
    body: { nombreUsuario, pin },
  })
  return datos.token
}

/**
 * POST /usuarios — crea la cuenta. Responde 201 sin cuerpo.
 * 409 si el nombre de usuario ya está en uso.
 */
export async function registrarUsuario(nombreUsuario, pin) {
  await api('/usuarios', {
    method: 'POST',
    auth: false,
    body: { nombreUsuario, pin },
  })
}
