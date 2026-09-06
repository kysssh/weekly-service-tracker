import { Link, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useTema } from '../hooks/useTema'
import BotonTema from './BotonTema'

/**
 * Menú superior compartido (design_frontend_swt.md §5): cambio de tema,
 * volver al panel principal y cerrar sesión.
 */
export default function BarraSuperior() {
  const { tema, alternarTema } = useTema()
  const { logout } = useAuth()
  const { pathname } = useLocation()
  const enPanel = pathname === '/'

  return (
    <header className="bg-header text-header-contraste">
      <div className="mx-auto flex max-w-md items-center justify-between gap-2 px-4 py-3">
        <Link
          to="/"
          className="font-display text-sm font-semibold tracking-wide"
        >
          Weekly Service Tracker
        </Link>

        <nav className="flex items-center gap-1">
          {!enPanel && (
            <Link
              to="/"
              className="inline-flex min-h-11 items-center rounded-lg px-3 text-sm font-medium text-header-contraste/90 transition hover:bg-white/10"
            >
              Panel
            </Link>
          )}
          <BotonTema tema={tema} onAlternar={alternarTema} />
          <button
            type="button"
            onClick={logout}
            className="inline-flex min-h-11 items-center rounded-lg px-3 text-sm font-medium text-header-contraste/90 transition hover:bg-white/10"
          >
            Cerrar sesión
          </button>
        </nav>
      </div>
    </header>
  )
}
