const soles = new Intl.NumberFormat('es-PE', {
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
})

/** 842.5 -> "S/ 842.50" */
export function formatearSoles(monto) {
  const n = Number(monto)
  return `S/ ${soles.format(Number.isFinite(n) ? n : 0)}`
}

/**
 * Convierte lo que el usuario escribe en un campo de monto a número.
 * Acepta coma o punto como separador decimal ("90,50" y "90.50" -> 90.5).
 * Devuelve NaN si no es un número válido.
 */
export function parsearMonto(texto) {
  const s = String(texto ?? '').trim()
  if (!s) return NaN
  // Si solo hay coma, es el separador decimal. Si hay punto (con o sin coma),
  // la coma se trata como separador de miles y se quita.
  const normalizado =
    s.includes(',') && !s.includes('.') ? s.replace(',', '.') : s.replace(/,/g, '')
  return Number(normalizado)
}

const fechaLarga = new Intl.DateTimeFormat('es-PE', {
  weekday: 'short',
  day: '2-digit',
  month: 'short',
})

/** "2026-07-22" -> "mar 22 jul" (sin depender de la zona horaria del navegador) */
export function formatearFecha(iso) {
  if (!iso) return ''
  const [anio, mes, dia] = String(iso).split('-').map(Number)
  if (!anio || !mes || !dia) return String(iso)
  return fechaLarga.format(new Date(anio, mes - 1, dia))
}
