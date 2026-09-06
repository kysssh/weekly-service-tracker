/**
 * Toggle claro/oscuro. Pensado para vivir en el header azul marino,
 * por eso usa los colores de contraste del header.
 */
export default function BotonTema({ tema, onAlternar }) {
  const esOscuro = tema === 'oscuro'

  return (
    <button
      type="button"
      onClick={onAlternar}
      aria-pressed={esOscuro}
      aria-label={esOscuro ? 'Activar modo claro' : 'Activar modo oscuro'}
      title={esOscuro ? 'Modo claro' : 'Modo oscuro'}
      className="inline-flex h-11 w-11 items-center justify-center rounded-full text-header-contraste/90 transition hover:bg-white/10 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-acento"
    >
      {esOscuro ? (
        // Sol
        <svg
          viewBox="0 0 24 24"
          className="h-5 w-5"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          aria-hidden="true"
        >
          <circle cx="12" cy="12" r="4" />
          <path d="M12 2v2M12 20v2M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M2 12h2M20 12h2M4.93 19.07l1.41-1.41M17.66 6.34l1.41-1.41" />
        </svg>
      ) : (
        // Luna
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
          <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" />
        </svg>
      )}
    </button>
  )
}
