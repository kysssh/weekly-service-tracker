import { api } from './api'

/**
 * La cuenta se creó (201) pero el inicio de sesión automático que va justo
 * después falló. La cuenta ya existe: el usuario debe iniciar sesión a mano.
 */
export class CuentaCreadaSinSesion extends Error {
  constructor() {
    super('La cuenta se creó, pero no se pudo iniciar sesión automáticamente.')
    this.name = 'CuentaCreadaSinSesion'
  }
}

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
  return datos?.token ?? null
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
