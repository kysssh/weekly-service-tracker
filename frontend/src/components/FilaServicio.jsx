import { formatearFecha, formatearSoles } from '../lib/formato'
import { IconoPeaje } from './iconos'

/** Una fila de la lista "Ver mis servicios". */
export default function FilaServicio({ servicio }) {
  const {
    fechaServicio,
    codigoServicio,
    distrito,
    tipoServicio,
    peaje,
    montoPeaje,
    montoServicio,
  } = servicio

  return (
    <li className="flex items-center justify-between gap-3 rounded-xl border border-borde bg-superficie p-4">
      <div className="min-w-0">
        <p className="truncate font-medium text-texto">
          <span className="uppercase">{codigoServicio}</span>
          <span className="text-texto-suave"> · {tipoServicio}</span>
        </p>
        <p className="mt-0.5 truncate text-sm text-texto-suave">
          {formatearFecha(fechaServicio)} · {distrito}
        </p>
      </div>

      <div className="shrink-0 text-right">
        <p className="font-display font-semibold text-texto">
          {formatearSoles(montoServicio)}
        </p>
        {peaje && (
          <p className="mt-0.5 inline-flex items-center gap-1 text-xs text-texto-suave">
            <IconoPeaje className="h-3.5 w-3.5" />
            Peaje {formatearSoles(montoPeaje)}
          </p>
        )}
      </div>
    </li>
  )
}
