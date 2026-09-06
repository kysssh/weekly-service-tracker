// Iconos de línea simples, sin dependencias. Heredan color con currentColor.

const base = {
  viewBox: '0 0 24 24',
  fill: 'none',
  stroke: 'currentColor',
  strokeWidth: 2,
  strokeLinecap: 'round',
  strokeLinejoin: 'round',
  'aria-hidden': true,
}

export function IconoMas({ className = 'h-6 w-6' }) {
  return (
    <svg {...base} className={className}>
      <path d="M12 5v14M5 12h14" />
    </svg>
  )
}

export function IconoLista({ className = 'h-6 w-6' }) {
  return (
    <svg {...base} className={className}>
      <path d="M8 6h13M8 12h13M8 18h13M3 6h.01M3 12h.01M3 18h.01" />
    </svg>
  )
}

export function IconoPeaje({ className = 'h-4 w-4' }) {
  return (
    <svg {...base} className={className}>
      <path d="M4 20V10l8-5 8 5v10" />
      <path d="M9 20v-5h6v5" />
    </svg>
  )
}
