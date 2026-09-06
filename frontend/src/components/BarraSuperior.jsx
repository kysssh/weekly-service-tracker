import { Link, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useTema } from '../hooks/useTema'
import BotonTema from './BotonTema'

/**
 * Menú superior compartido (design_frontend_swt.md §5): logo de la app,
 * cambio de tema, volver al panel principal y cerrar sesión.
 * En pantallas angostas el nombre se oculta y queda solo el ícono, para
 * que ningún texto de la barra quede recortado.
 */
export default function BarraSuperior() {
  const { tema, alternarTema } = useTema()
  const { logout } = useAuth()
  const { pathname } = useLocation()
  const enPanel = pathname === '/'

  return (
    <header className="bg-header text-header-contraste">
      <div className="mx-auto flex max-w-md items-center justify-between gap-3 px-4 py-3">
        <Link
          to="/"
          aria-label="Ir al panel principal"
          className="flex shrink-0 items-center gap-2"
        >
          <img
            src="/icono_nofondo.png"
            alt=""
            aria-hidden="true"
            className="h-7 w-7 shrink-0 object-contain"
          />
          <span className="hidden whitespace-nowrap font-display text-sm font-semibold tracking-wide sm:inline">
            Weekly Service Tracker
          </span>
        </Link>

        <nav className="flex items-center gap-1">
          {!enPanel && (
            <Link
              to="/"
              className="inline-flex min-h-11 items-center whitespace-nowrap rounded-lg px-3 text-sm font-medium text-header-contraste/90 transition hover:bg-white/10"
            >
              Panel
            </Link>
          )}
          <BotonTema tema={tema} onAlternar={alternarTema} />
          <button
            type="button"
            onClick={logout}
            className="inline-flex min-h-11 items-center whitespace-nowrap rounded-lg px-3 text-sm font-medium text-header-contraste/90 transition hover:bg-white/10"
          >
            Cerrar sesión
          </button>
        </nav>
      </div>
    </header>
  )
}
