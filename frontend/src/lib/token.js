// Guardado y lectura del JWT. El backend usa expiración de 1 hora y NO tiene
// refresh tokens (decisión deliberada), así que cuando el token vence el usuario
// simplemente vuelve a iniciar sesión.

const CLAVE = 'swt-token'

export function leerToken() {
  try {
    return localStorage.getItem(CLAVE)
  } catch {
    return null
  }
}

export function guardarToken(token) {
  try {
    localStorage.setItem(CLAVE, token)
  } catch {
    // Sin persistencia (modo privado); la sesión vive solo en memoria.
  }
}

export function borrarToken() {
  try {
    localStorage.removeItem(CLAVE)
  } catch {
    // no-op
  }
}

/** Decodifica un tramo base64url a texto UTF-8 (nombres con tilde o ñ incluidos). */
function base64UrlADecodedText(segmento) {
  const b64 = segmento.replace(/-/g, '+').replace(/_/g, '/')
  const relleno = b64.length % 4 ? '='.repeat(4 - (b64.length % 4)) : ''
  const binario = atob(b64 + relleno)
  const bytes = Uint8Array.from(binario, (c) => c.charCodeAt(0))
  return new TextDecoder('utf-8').decode(bytes)
}

/**
 * Decodifica el payload del JWT sin verificar la firma (eso lo hace el backend).
 * Sirve para leer el `sub` (nombre de usuario) y el `exp` en el cliente.
 */
export function decodificarToken(token) {
  if (!token) return null
  try {
    return JSON.parse(base64UrlADecodedText(token.split('.')[1]))
  } catch {
    return null
  }
}

/** true si el token no existe o su fecha de expiración ya pasó. */
export function tokenExpirado(token) {
  const payload = decodificarToken(token)
  if (!payload || typeof payload.exp !== 'number') return true
  return payload.exp * 1000 <= Date.now()
}

export function nombreUsuarioDeToken(token) {
  return decodificarToken(token)?.sub ?? null
}
