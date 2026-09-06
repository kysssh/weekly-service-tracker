import { useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import BotonTema from '../components/BotonTema'
import { useAuth } from '../context/AuthContext'
import { useTema } from '../hooks/useTema'
import { ApiError, RedError } from '../lib/api'

/** Ícono de ruta que se usa si aún no existe /logo.svg en public. */
function LogoRuta({ className }) {
  return (
    <svg viewBox="0 0 64 64" className={className} aria-hidden="true">
      <path
        d="M18 46c0-7 7-9 14-9s14-2 14-9-7-9-14-9"
        fill="none"
        stroke="currentColor"
        strokeWidth="4"
        strokeLinecap="round"
        strokeDasharray="1 9"
      />
      <circle cx="18" cy="46" r="5" fill="currentColor" />
      <circle
        cx="46"
        cy="19"
        r="5"
        fill="none"
        stroke="currentColor"
        strokeWidth="4"
      />
    </svg>
  )
}

/** Línea de puntos tipo "trayecto" como acento decorativo del header. */
function TrayectoDecorativo() {
  return (
    <svg
      className="pointer-events-none absolute inset-0 h-full w-full"
      viewBox="0 0 400 200"
      preserveAspectRatio="none"
      aria-hidden="true"
    >
      <path
        d="M-20 150 C 80 150, 110 40, 200 40 S 320 150, 420 60"
        fill="none"
        stroke="currentColor"
        strokeWidth="2.5"
        strokeLinecap="round"
        strokeDasharray="2 12"
        opacity="0.35"
      />
    </svg>
  )
}

const CAMPO =
  'w-full min-h-12 rounded-xl border border-borde bg-fondo px-4 py-3 text-[15px] text-texto placeholder:text-texto-suave/70 outline-none transition focus:border-acento focus:ring-2 focus:ring-acento/30 disabled:opacity-60'

function mensajeDeError(err, esRegistro) {
  if (err instanceof RedError) {
    return 'No se pudo conectar con el servidor. Revisa que el backend esté encendido.'
  }
  if (err instanceof ApiError) {
    if (err.status === 401) return 'Usuario o PIN incorrectos.'
    if (err.status === 409) return 'Ese nombre de usuario ya está en uso.'
  }
  return esRegistro
    ? 'No se pudo crear la cuenta. Intenta de nuevo.'
    : 'No se pudo iniciar sesión. Intenta de nuevo.'
}

export default function LoginRegistro() {
  const { tema, alternarTema } = useTema()
  const { estaAutenticado, login, registrar } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const [modo, setModo] = useState('login') // 'login' | 'registro'
  const [verPin, setVerPin] = useState(false)
  const [logoFallo, setLogoFallo] = useState(false)
  const [enviando, setEnviando] = useState(false)
  const [form, setForm] = useState({ usuario: '', pin: '', confirmar: '' })
  const [error, setError] = useState('')

  const esRegistro = modo === 'registro'
  const destino = location.state?.from?.pathname || '/'

  // Si ya hay sesión, no tiene sentido mostrar el login.
  if (estaAutenticado) return <Navigate to={destino} replace />

  function actualizar(campo) {
    return (e) => setForm((f) => ({ ...f, [campo]: e.target.value }))
  }

  function cambiarModo(nuevoModo) {
    setModo(nuevoModo)
    setError('')
    setForm((f) => ({ ...f, pin: '', confirmar: '' }))
  }

  async function enviar(e) {
    e.preventDefault()
    setError('')

    const usuario = form.usuario.trim()
    if (!usuario || !form.pin) {
      setError('Completa tu usuario y tu PIN.')
      return
    }
    if (esRegistro && form.pin !== form.confirmar) {
      setError('Los PIN no coinciden.')
      return
    }

    setEnviando(true)
    try {
      if (esRegistro) {
        await registrar(usuario, form.pin)
      } else {
        await login(usuario, form.pin)
      }
      navigate(destino, { replace: true })
    } catch (err) {
      setError(mensajeDeError(err, esRegistro))
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="min-h-svh bg-fondo">
      {/* Header azul marino con acento de ruta y logo centrado */}
      <header className="relative overflow-hidden bg-header text-header-contraste">
        <TrayectoDecorativo />
        <div className="relative mx-auto flex max-w-md items-center justify-end px-4 pt-3">
          <BotonTema tema={tema} onAlternar={alternarTema} />
        </div>
        <div className="relative mx-auto flex max-w-md flex-col items-center px-4 pb-20 pt-2 text-center">
          {logoFallo ? (
            // Fallback mientras no exista /logo.svg en public.
            <LogoRuta className="h-16 w-16 text-header-contraste" />
          ) : (
            <img
              src="/logo.svg"
              alt="Weekly Service Tracker"
              className="h-16 w-16 object-contain"
              onError={() => setLogoFallo(true)}
            />
          )}
          <p className="mt-3 text-sm font-medium tracking-wide text-header-contraste/80">
            Weekly Service Tracker
          </p>
        </div>
      </header>

      {/* Tarjeta blanca superpuesta (relative + z-10 para quedar sobre el header) */}
      <main className="relative z-10 mx-auto -mt-12 max-w-md px-4 pb-12">
        <div className="rounded-2xl border border-borde bg-superficie p-6 shadow-lg shadow-black/5 sm:p-8">
          <h1 className="font-display text-2xl font-semibold text-texto">
            {esRegistro ? 'Crea tu cuenta' : 'Bienvenido'}
          </h1>
          <p className="mt-1 text-sm text-texto-suave">
            {esRegistro
              ? 'Regístrate para empezar a registrar tus servicios.'
              : 'Ingresa para registrar tus servicios.'}
          </p>

          <form onSubmit={enviar} className="mt-6 space-y-4" noValidate>
            <div>
              <label
                htmlFor="usuario"
                className="mb-1.5 block text-sm font-medium text-texto"
              >
                Usuario
              </label>
              <input
                id="usuario"
                type="text"
                autoComplete="username"
                autoCapitalize="none"
                spellCheck="false"
                className={CAMPO}
                placeholder="Tu nombre de usuario"
                value={form.usuario}
                onChange={actualizar('usuario')}
                disabled={enviando}
              />
            </div>

            <div>
              <label
                htmlFor="pin"
                className="mb-1.5 block text-sm font-medium text-texto"
              >
                PIN
              </label>
              <div className="relative">
                <input
                  id="pin"
                  type={verPin ? 'text' : 'password'}
                  inputMode="numeric"
                  autoComplete={esRegistro ? 'new-password' : 'current-password'}
                  className={`${CAMPO} pr-12`}
                  placeholder="Tu PIN"
                  value={form.pin}
                  onChange={actualizar('pin')}
                  disabled={enviando}
                />
                <button
                  type="button"
                  onClick={() => setVerPin((v) => !v)}
                  aria-label={verPin ? 'Ocultar PIN' : 'Mostrar PIN'}
                  className="absolute inset-y-0 right-0 flex w-12 items-center justify-center text-texto-suave transition hover:text-texto"
                >
                  {verPin ? (
                    <svg
                      viewBox="0 0 24 24"
                      className="h-5 w-5"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      aria-hidden="true"
                    >
                      <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20C5 20 1 12 1 12a18.5 18.5 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19M1 1l22 22" />
                      <path d="M9.88 9.88a3 3 0 1 0 4.24 4.24" />
                    </svg>
                  ) : (
                    <svg
                      viewBox="0 0 24 24"
                      className="h-5 w-5"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="2"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      aria-hidden="true"
                    >
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                      <circle cx="12" cy="12" r="3" />
                    </svg>
                  )}
                </button>
              </div>
            </div>

            {esRegistro && (
              <div>
                <label
                  htmlFor="confirmar"
                  className="mb-1.5 block text-sm font-medium text-texto"
                >
                  Confirmar PIN
                </label>
                <input
                  id="confirmar"
                  type={verPin ? 'text' : 'password'}
                  inputMode="numeric"
                  autoComplete="new-password"
                  className={CAMPO}
                  placeholder="Repite tu PIN"
                  value={form.confirmar}
                  onChange={actualizar('confirmar')}
                  disabled={enviando}
                />
              </div>
            )}

            {error && (
              <p
                role="alert"
                className="rounded-lg bg-alerta/10 px-3 py-2 text-sm font-medium text-alerta"
              >
                {error}
              </p>
            )}

            <button
              type="submit"
              disabled={enviando}
              className="min-h-12 w-full rounded-xl bg-primario px-4 py-3 text-[15px] font-semibold text-primario-contraste transition hover:bg-primario-hover focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-primario active:scale-[0.99] disabled:cursor-not-allowed disabled:opacity-70"
            >
              {enviando
                ? 'Un momento…'
                : esRegistro
                  ? 'Crear cuenta'
                  : 'Ingresar'}
            </button>
          </form>

          {/* Enlace secundario para registro / volver a login */}
          <p className="mt-6 text-center text-sm text-texto-suave">
            {esRegistro ? '¿Ya tienes cuenta? ' : '¿Aún no tienes cuenta? '}
            <button
              type="button"
              onClick={() => cambiarModo(esRegistro ? 'login' : 'registro')}
              disabled={enviando}
              className="font-semibold text-acento transition hover:text-acento-hover hover:underline disabled:opacity-60"
            >
              {esRegistro ? 'Inicia sesión' : 'Regístrate'}
            </button>
          </p>
        </div>
      </main>
    </div>
  )
}
