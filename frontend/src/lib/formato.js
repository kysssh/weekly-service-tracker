const soles = new Intl.NumberFormat('es-PE', {
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
})

/** 842.5 -> "S/ 842.50" */
export function formatearSoles(monto) {
  const n = Number(monto)
  return `S/ ${soles.format(Number.isFinite(n) ? n : 0)}`
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
