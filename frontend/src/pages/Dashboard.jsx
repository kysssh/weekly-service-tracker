import BarraSuperior from '../components/BarraSuperior'
import { Cargando, ErrorCarga } from '../components/EstadoCarga'
import { IconoLista, IconoMas } from '../components/iconos'
import TarjetaAccion from '../components/TarjetaAccion'
import { useAuth } from '../context/AuthContext'
import { usePeticion } from '../hooks/usePeticion'
import { useSesionExpira } from '../hooks/useSesionExpira'
import { formatearSoles } from '../lib/formato'
import { obtenerResumen } from '../lib/servicios'

// Pantalla 2: Panel Principal (design_frontend_swt.md §5).
export default function Dashboard() {
  const { nombreUsuario } = useAuth()
  const { datos: resumen, cargando, error, recargar } = usePeticion(obtenerResumen)
  useSesionExpira(error)

  const ganancia =
    (resumen?.totalCorporativo ?? 0) + (resumen?.totalB4 ?? 0)

  return (
    <div className="min-h-svh bg-fondo text-texto">
      <BarraSuperior />

      <main className="mx-auto max-w-md px-4 pb-12">
        {/* Header / Resumen: saludo + dato principal gigante */}
        <section className="-mt-6 rounded-2xl border border-borde bg-superficie p-6 shadow-lg shadow-black/5">
          <p className="font-display text-lg font-semibold text-texto">
            Hola, {nombreUsuario ?? 'conductor'}
          </p>

          {cargando && <Cargando texto="Cargando tu resumen…" />}

          {!cargando && error && (
            <div className="mt-4">
              <ErrorCarga error={error} onReintentar={recargar} />
            </div>
          )}

          {!cargando && !error && resumen && (
            <>
              <p className="mt-4 text-sm text-texto-suave">
                Ganancia esta semana
              </p>
              <p className="mt-1 font-display text-5xl font-bold tracking-tight text-acento">
                {formatearSoles(ganancia)}
              </p>

              <dl className="mt-5 grid grid-cols-3 gap-3 border-t border-borde pt-4 text-center">
                <Detalle etiqueta="Corporativo" valor={resumen.totalCorporativo} />
                <Detalle etiqueta="B4" valor={resumen.totalB4} />
                <Detalle etiqueta="Peajes Kusi" valor={resumen.peajesKusi} />
              </dl>
            </>
          )}
        </section>

        {/* Acciones principales */}
        <div className="mt-6 space-y-3">
          <TarjetaAccion
            to="/servicios/nuevo"
            titulo="Registrar servicio"
            descripcion="Anota un viaje que hiciste hoy"
            icono={<IconoMas />}
            destacada
          />
          <TarjetaAccion
            to="/servicios"
            titulo="Ver mis servicios"
            descripcion="Semana actual y semanas anteriores"
            icono={<IconoLista />}
          />
        </div>
      </main>
    </div>
  )
}

function Detalle({ etiqueta, valor }) {
  return (
    <div>
      <dt className="text-xs text-texto-suave">{etiqueta}</dt>
      <dd className="mt-0.5 text-sm font-semibold text-texto">
        {formatearSoles(valor)}
      </dd>
    </div>
  )
}
