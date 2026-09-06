import { api } from './api'

/**
 * GET /servicios/resumen — totales de la semana actual (lunes a domingo).
 * Devuelve { totalCorporativo, totalB4, peajesKusi } como números.
 */
export async function obtenerResumen() {
  const dto = await api('/servicios/resumen')
  return {
    totalCorporativo: Number(dto?.totalCorporativo ?? 0),
    totalB4: Number(dto?.totalB4 ?? 0),
    peajesKusi: Number(dto?.peajesKusi ?? 0),
  }
}

/** GET /servicios/semana-actual — servicios de la semana en curso. */
export function obtenerSemanaActual() {
  return api('/servicios/semana-actual')
}

/**
 * POST /servicios — registra un servicio nuevo. Responde 201 sin cuerpo.
 * `dto`: { codigo, distrito, tipoServicio, peaje, montoServicio, montoPeaje, fechaServicio }
 */
export function registrarServicio(dto) {
  return api('/servicios', { method: 'POST', body: dto })
}

/** GET /servicios/historial — servicios de semanas anteriores (últimos ~30 días). */
export function obtenerHistorial() {
  return api('/servicios/historial')
}
