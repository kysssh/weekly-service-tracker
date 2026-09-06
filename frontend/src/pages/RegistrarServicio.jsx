import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import BarraSuperior from '../components/BarraSuperior'
import {
  CampoFecha,
  CampoNumero,
  CampoSelect,
  CampoTexto,
} from '../components/campos'
import { useAuth } from '../context/AuthContext'
import { ApiError, RedError } from '../lib/api'
import { DISTRITOS } from '../lib/distritos'
import { fechaEnRango, haceUnaSemanaISO, hoyISO } from '../lib/fechas'
import { parsearMonto } from '../lib/formato'
import { registrarServicio } from '../lib/servicios'

const TIPOS_SERVICIO = ['CMV', '365', 'Kusi', 'Otros']

const VACIO = {
  fechaServicio: hoyISO(),
  codigo: '',
  distrito: '',
  tipoServicio: '',
  peaje: 'no',
  montoServicio: '',
  montoPeaje: '',
}

function validar(form) {
  const errores = {}
  const hayPeaje = form.peaje === 'si'

  if (!form.fechaServicio) {
    errores.fechaServicio = 'Elige la fecha del servicio.'
  } else if (!fechaEnRango(form.fechaServicio)) {
    errores.fechaServicio =
      'La fecha debe estar entre hoy y una semana atrás.'
  }

  if (!form.codigo.trim()) errores.codigo = 'Escribe el código del servicio.'
  if (!form.distrito) errores.distrito = 'Elige el distrito.'
  if (!form.tipoServicio) errores.tipoServicio = 'Elige el tipo de servicio.'

  const monto = parsearMonto(form.montoServicio)
  if (!form.montoServicio.trim()) {
    errores.montoServicio = 'Escribe el monto del servicio.'
  } else if (!Number.isFinite(monto) || monto <= 0) {
    errores.montoServicio = 'Escribe un monto válido mayor a 0 (ej. 25.50).'
  }

  if (hayPeaje) {
    const peaje = parsearMonto(form.montoPeaje)
    if (!form.montoPeaje.trim()) {
      errores.montoPeaje = 'Escribe el monto del peaje.'
    } else if (!Number.isFinite(peaje) || peaje <= 0) {
      errores.montoPeaje = 'Escribe un monto válido mayor a 0 (ej. 3.50).'
    }
  }

  return errores
}

export default function RegistrarServicio() {
  const navigate = useNavigate()
  const { logout } = useAuth()

  const [form, setForm] = useState(VACIO)
  const [errores, setErrores] = useState({})
  const [errorEnvio, setErrorEnvio] = useState('')
  const [enviando, setEnviando] = useState(false)

  const hayPeaje = form.peaje === 'si'

  function actualizar(campo) {
    return (e) => {
      const { value } = e.target
      setForm((f) => ({ ...f, [campo]: value }))
      setErrores((prev) => (prev[campo] ? { ...prev, [campo]: undefined } : prev))
    }
  }

  async function enviar(e) {
    e.preventDefault()
    setErrorEnvio('')

    const encontrados = validar(form)
    setErrores(encontrados)
    if (Object.keys(encontrados).some((k) => encontrados[k])) return

    setEnviando(true)
    try {
      await registrarServicio({
        codigo: form.codigo.trim(),
        distrito: form.distrito,
        tipoServicio: form.tipoServicio,
        peaje: hayPeaje,
        montoServicio: parsearMonto(form.montoServicio),
        montoPeaje: hayPeaje ? parsearMonto(form.montoPeaje) : null,
        fechaServicio: form.fechaServicio,
      })
      navigate('/servicios')
    } catch (err) {
      if (err instanceof ApiError && (err.status === 401 || err.status === 403)) {
        logout()
        return
      }
      if (err instanceof RedError) {
        setErrorEnvio('No se pudo conectar con el servidor.')
      } else if (err instanceof ApiError && err.status === 400 && err.cuerpo?.mensaje) {
        // El backend devuelve el motivo concreto de la validación.
        setErrorEnvio(err.cuerpo.mensaje)
      } else {
        setErrorEnvio(
          'No se pudo registrar el servicio. Revisa los datos e intenta de nuevo.',
        )
      }
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="min-h-svh bg-fondo text-texto">
      <BarraSuperior />

      <main className="mx-auto max-w-md px-4 py-6">
        <h1 className="font-display text-xl font-semibold">Registrar servicio</h1>
        <p className="mt-1 text-sm text-texto-suave">
          Anota un viaje que hiciste esta semana.
        </p>

        <form onSubmit={enviar} className="mt-6 space-y-4" noValidate>
          <CampoFecha
            id="fechaServicio"
            etiqueta="Fecha del servicio"
            value={form.fechaServicio}
            onChange={actualizar('fechaServicio')}
            min={haceUnaSemanaISO()}
            max={hoyISO()}
            ayuda="Puedes registrar desde hoy hasta una semana atrás. Si el calendario no te funciona, escribe la fecha como AAAA-MM-DD."
            error={errores.fechaServicio}
            disabled={enviando}
          />

          <CampoTexto
            id="codigo"
            etiqueta="Código del servicio"
            value={form.codigo}
            onChange={actualizar('codigo')}
            placeholder="Ej. ID-4521 o 550123"
            autoCapitalize="characters"
            spellCheck="false"
            ayuda="Empieza con ID- para servicios corporativos; solo números para códigos B4."
            error={errores.codigo}
            disabled={enviando}
          />

          <CampoSelect
            id="distrito"
            etiqueta="Distrito"
            value={form.distrito}
            onChange={actualizar('distrito')}
            error={errores.distrito}
            disabled={enviando}
          >
            <option value="" disabled>
              Elige un distrito
            </option>
            {DISTRITOS.map(({ grupo, opciones }) => (
              <optgroup key={grupo} label={grupo}>
                {opciones.map((d) => (
                  <option key={d} value={d}>
                    {d}
                  </option>
                ))}
              </optgroup>
            ))}
          </CampoSelect>

          <CampoSelect
            id="tipoServicio"
            etiqueta="Tipo de servicio"
            value={form.tipoServicio}
            onChange={actualizar('tipoServicio')}
            error={errores.tipoServicio}
            disabled={enviando}
          >
            <option value="" disabled>
              Elige el tipo
            </option>
            {TIPOS_SERVICIO.map((t) => (
              <option key={t} value={t}>
                {t}
              </option>
            ))}
          </CampoSelect>

          <CampoSelect
            id="peaje"
            etiqueta="¿Hubo peaje?"
            value={form.peaje}
            onChange={actualizar('peaje')}
            disabled={enviando}
          >
            <option value="no">No</option>
            <option value="si">Sí</option>
          </CampoSelect>

          <CampoNumero
            id="montoServicio"
            etiqueta="Monto del servicio"
            prefijo="S/"
            value={form.montoServicio}
            onChange={actualizar('montoServicio')}
            placeholder="0.00"
            error={errores.montoServicio}
            disabled={enviando}
          />

          {hayPeaje && (
            <CampoNumero
              id="montoPeaje"
              etiqueta="Monto del peaje"
              prefijo="S/"
              value={form.montoPeaje}
              onChange={actualizar('montoPeaje')}
              placeholder="0.00"
              error={errores.montoPeaje}
              disabled={enviando}
            />
          )}

          {errorEnvio && (
            <p
              role="alert"
              className="rounded-lg bg-alerta/10 px-3 py-2 text-sm font-medium text-alerta"
            >
              {errorEnvio}
            </p>
          )}

          <div className="flex gap-3 pt-2">
            <Link
              to="/"
              className="inline-flex min-h-12 flex-1 items-center justify-center rounded-xl border border-borde px-4 text-[15px] font-semibold text-texto transition hover:bg-texto/5"
            >
              Cancelar
            </Link>
            <button
              type="submit"
              disabled={enviando}
              className="min-h-12 flex-1 rounded-xl bg-primario px-4 text-[15px] font-semibold text-primario-contraste transition hover:bg-primario-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primario active:scale-[0.99] disabled:cursor-not-allowed disabled:opacity-70"
            >
              {enviando ? 'Guardando…' : 'Guardar servicio'}
            </button>
          </div>
        </form>
      </main>
    </div>
  )
}
