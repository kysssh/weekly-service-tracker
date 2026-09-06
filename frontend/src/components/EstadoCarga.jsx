import { RedError } from '../lib/api'

export function Cargando({ texto = 'Cargando…' }) {
  return (
    <div className="flex items-center justify-center gap-3 py-10 text-texto-suave">
      <span
        className="h-5 w-5 animate-spin rounded-full border-2 border-borde border-t-acento"
        aria-hidden="true"
      />
      <span className="text-sm">{texto}</span>
    </div>
  )
}

export function ErrorCarga({ error, onReintentar }) {
  const mensaje =
    error instanceof RedError
      ? 'No se pudo conectar con el servidor.'
      : 'No se pudo cargar la información.'

  return (
    <div
      role="alert"
      className="rounded-2xl border border-alerta/30 bg-alerta/10 p-4 text-center"
    >
      <p className="text-sm font-medium text-alerta">{mensaje}</p>
      {onReintentar && (
        <button
          type="button"
          onClick={onReintentar}
          className="mt-3 inline-flex min-h-11 items-center rounded-lg border border-alerta/40 px-4 text-sm font-semibold text-alerta transition hover:bg-alerta/10"
        >
          Reintentar
        </button>
      )}
    </div>
  )
}
