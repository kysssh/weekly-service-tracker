// Helpers de fecha en formato ISO corto (YYYY-MM-DD), en hora local.
// El backend acepta fechaServicio entre hoy y hoy - 1 semana.

function aISO(fecha) {
  const anio = fecha.getFullYear()
  const mes = String(fecha.getMonth() + 1).padStart(2, '0')
  const dia = String(fecha.getDate()).padStart(2, '0')
  return `${anio}-${mes}-${dia}`
}

export function hoyISO() {
  return aISO(new Date())
}

export function haceUnaSemanaISO() {
  const d = new Date()
  d.setDate(d.getDate() - 7)
  return aISO(d)
}

/** true si `iso` está dentro del rango permitido por el backend [hoy-7, hoy]. */
export function fechaEnRango(iso) {
  return Boolean(iso) && iso >= haceUnaSemanaISO() && iso <= hoyISO()
}
