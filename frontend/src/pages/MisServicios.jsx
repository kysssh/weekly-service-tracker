import { useCallback, useState } from 'react'
import BarraSuperior from '../components/BarraSuperior'
import { Cargando, ErrorCarga } from '../components/EstadoCarga'
import FilaServicio from '../components/FilaServicio'
import { usePeticion } from '../hooks/usePeticion'
import { useSesionExpira } from '../hooks/useSesionExpira'
import { formatearSoles } from '../lib/formato'
import { obtenerHistorial, obtenerSemanaActual } from '../lib/servicios'

// "Ver mis servicios" (design_frontend_swt.md §5): lista que alterna
// internamente entre la semana actual y las semanas anteriores.
export default function MisServicios() {
  const [vista, setVista] = useState('actual') // 'actual' | 'anteriores'

  const cargar = useCallback(
    () => (vista === 'actual' ? obtenerSemanaActual() : obtenerHistorial()),
    [vista],
  )
  const { datos, cargando, error, recargar } = usePeticion(cargar)
  useSesionExpira(error)

  const servicios = Array.isArray(datos) ? datos : []
  const total = servicios.reduce((s, x) => s + Number(x.montoServicio ?? 0), 0)

  return (
    <div className="min-h-svh bg-fondo text-texto">
      <BarraSuperior />

      <main className="mx-auto max-w-md px-4 py-6">
        <h1 className="font-display text-xl font-semibold">Mis servicios</h1>

        {/* Alternador semana actual / anteriores */}
        <div
          role="tablist"
          aria-label="Periodo"
          className="mt-4 flex rounded-xl border border-borde bg-superficie p-1"
        >
          <Pestania
            id="tab-actual"
            panelId="panel-servicios"
            activa={vista === 'actual'}
            onClick={() => setVista('actual')}
          >
            Esta semana
          </Pestania>
          <Pestania
            id="tab-anteriores"
            panelId="panel-servicios"
            activa={vista === 'anteriores'}
            onClick={() => setVista('anteriores')}
          >
            Semanas anteriores
          </Pestania>
        </div>

        <div
          id="panel-servicios"
          role="tabpanel"
          aria-labelledby={vista === 'actual' ? 'tab-actual' : 'tab-anteriores'}
          className="mt-5"
        >
          {cargando && <Cargando texto="Cargando servicios…" />}

          {!cargando && error && (
            <ErrorCarga error={error} onReintentar={recargar} />
          )}

          {!cargando && !error && servicios.length === 0 && (
            <p className="rounded-2xl border border-borde bg-superficie p-6 text-center text-sm text-texto-suave">
              {vista === 'actual'
                ? 'Aún no has registrado servicios esta semana.'
                : 'No hay servicios en semanas anteriores.'}
            </p>
          )}

          {!cargando && !error && servicios.length > 0 && (
            <>
              <div className="flex items-center justify-between px-1 text-sm text-texto-suave">
                <span>
                  {servicios.length}{' '}
                  {servicios.length === 1 ? 'servicio' : 'servicios'}
                </span>
                <span>
                  Total <span className="font-semibold text-texto">{formatearSoles(total)}</span>
                </span>
              </div>
              <ul className="mt-2 space-y-2">
                {servicios.map((servicio) => (
                  <FilaServicio key={servicio.id} servicio={servicio} />
                ))}
              </ul>
            </>
          )}
        </div>
      </main>
    </div>
  )
}

function Pestania({ id, panelId, activa, onClick, children }) {
  return (
    <button
      type="button"
      role="tab"
      id={id}
      aria-controls={panelId}
      aria-selected={activa}
      onClick={onClick}
      className={`min-h-11 flex-1 rounded-lg px-3 text-sm font-semibold transition ${
        activa
          ? 'bg-primario text-primario-contraste'
          : 'text-texto-suave hover:text-texto'
      }`}
    >
      {children}
    </button>
  )
}
