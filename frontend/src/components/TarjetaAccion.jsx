import { Link } from 'react-router-dom'

/**
 * Tarjeta de acción grande del panel principal (design_frontend_swt.md §5).
 * `destacada` le da el borde de color de acento para la acción primaria.
 */
export default function TarjetaAccion({
  to,
  titulo,
  descripcion,
  icono,
  destacada = false,
}) {
  return (
    <Link
      to={to}
      className={`flex items-center gap-4 rounded-2xl border bg-superficie p-5 shadow-sm shadow-black/5 transition hover:shadow-md focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-acento ${
        destacada ? 'border-acento' : 'border-borde'
      }`}
    >
      <span
        className={`flex h-12 w-12 shrink-0 items-center justify-center rounded-xl ${
          destacada
            ? 'bg-acento text-white'
            : 'bg-primario/10 text-primario dark:bg-white/10 dark:text-header-contraste'
        }`}
        aria-hidden="true"
      >
        {icono}
      </span>
      <span className="min-w-0">
        <span className="block font-display text-base font-semibold text-texto">
          {titulo}
        </span>
        <span className="mt-0.5 block text-sm text-texto-suave">
          {descripcion}
        </span>
      </span>
    </Link>
  )
}
