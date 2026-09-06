// Primitivas de formulario con estilo y espaciado consistentes.
// Etiqueta siempre visible arriba, campo a lo ancho, texto legible sin recortes.

const BASE =
  'w-full min-h-12 rounded-xl border bg-fondo px-4 py-3 text-[15px] leading-normal text-texto outline-none transition placeholder:text-texto-suave/70 focus:ring-2 focus:ring-acento/30 disabled:opacity-60'

function clasesCampo(hayError) {
  return `${BASE} ${hayError ? 'border-alerta focus:border-alerta' : 'border-borde focus:border-acento'}`
}

function Contenedor({ id, etiqueta, ayuda, error, children }) {
  return (
    <div>
      <label htmlFor={id} className="mb-1.5 block text-sm font-medium text-texto">
        {etiqueta}
      </label>
      {children}
      {ayuda && !error && (
        <p className="mt-1 text-xs leading-snug text-texto-suave">{ayuda}</p>
      )}
      {error && (
        <p className="mt-1 text-sm font-medium leading-snug text-alerta">
          {error}
        </p>
      )}
    </div>
  )
}

export function CampoTexto({ id, etiqueta, ayuda, error, ...props }) {
  return (
    <Contenedor id={id} etiqueta={etiqueta} ayuda={ayuda} error={error}>
      <input id={id} className={clasesCampo(Boolean(error))} {...props} />
    </Contenedor>
  )
}

export function CampoNumero({ id, etiqueta, ayuda, error, prefijo, ...props }) {
  return (
    <Contenedor id={id} etiqueta={etiqueta} ayuda={ayuda} error={error}>
      <div className="relative">
        {prefijo && (
          <span className="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-4 text-[15px] text-texto-suave">
            {prefijo}
          </span>
        )}
        <input
          id={id}
          inputMode="decimal"
          className={`${clasesCampo(Boolean(error))} ${prefijo ? 'pl-12' : ''}`}
          {...props}
        />
      </div>
    </Contenedor>
  )
}

export function CampoFecha({ id, etiqueta, ayuda, error, ...props }) {
  return (
    <Contenedor id={id} etiqueta={etiqueta} ayuda={ayuda} error={error}>
      <input
        id={id}
        type="date"
        className={clasesCampo(Boolean(error))}
        {...props}
      />
    </Contenedor>
  )
}

export function CampoSelect({ id, etiqueta, ayuda, error, children, ...props }) {
  return (
    <Contenedor id={id} etiqueta={etiqueta} ayuda={ayuda} error={error}>
      <div className="relative">
        <select
          id={id}
          className={`${clasesCampo(Boolean(error))} appearance-none pr-11`}
          {...props}
        >
          {children}
        </select>
        <svg
          viewBox="0 0 24 24"
          className="pointer-events-none absolute inset-y-0 right-3 my-auto h-5 w-5 text-texto-suave"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
          aria-hidden="true"
        >
          <path d="m6 9 6 6 6-6" />
        </svg>
      </div>
    </Contenedor>
  )
}
