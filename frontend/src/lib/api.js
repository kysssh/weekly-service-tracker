import { borrarToken, leerToken } from './token'

const BASE_URL = (
  import.meta.env.VITE_API_URL ?? 'http://localhost:8080'
).replace(/\/$/, '')

/** Error de una respuesta HTTP no exitosa. Lleva el status para decidir el mensaje. */
export class ApiError extends Error {
  constructor(status, mensaje, cuerpo) {
    super(mensaje)
    this.name = 'ApiError'
    this.status = status
    this.cuerpo = cuerpo
  }
}

/** Error de red / servidor caído (fetch ni siquiera llegó a responder). */
export class RedError extends Error {
  constructor() {
    super('No se pudo conectar con el servidor.')
    this.name = 'RedError'
  }
}

/**
 * Envoltorio de fetch para la API:
 * - antepone VITE_API_URL
 * - manda y recibe JSON
 * - adjunta el header Authorization: Bearer <token> si hay sesión
 * - ante un 401/403 borra el token (sesión vencida) y lanza ApiError
 * - devuelve el JSON de la respuesta, o null si viene vacía (201/204)
 */
export async function api(ruta, { method = 'GET', body, auth = true } = {}) {
  const headers = {}
  if (body !== undefined) headers['Content-Type'] = 'application/json'

  if (auth) {
    const token = leerToken()
    if (token) headers.Authorization = `Bearer ${token}`
  }

  let respuesta
  try {
    respuesta = await fetch(`${BASE_URL}${ruta}`, {
      method,
      headers,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    })
  } catch {
    throw new RedError()
  }

  // El backend responde 401 con credenciales incorrectas en /auth y 403 cuando
  // falta el token o ya venció en una ruta protegida. En ambos casos no hay
  // sesión válida que conservar.
  if (respuesta.status === 401 || respuesta.status === 403) {
    borrarToken()
    throw new ApiError(respuesta.status, 'No autorizado.')
  }

  if (!respuesta.ok) {
    let cuerpo = null
    try {
      cuerpo = await respuesta.json()
    } catch {
      // respuesta sin cuerpo JSON
    }
    throw new ApiError(
      respuesta.status,
      `La petición falló (${respuesta.status}).`,
      cuerpo,
    )
  }

  if (respuesta.status === 204 || respuesta.status === 201) return null
  const texto = await respuesta.text()
  return texto ? JSON.parse(texto) : null
}
