import { useCallback, useEffect, useState } from 'react'

const CLAVE = 'swt-tema'

function temaInicial() {
  try {
    const guardado = localStorage.getItem(CLAVE)
    if (guardado === 'claro' || guardado === 'oscuro') return guardado
  } catch {
    // localStorage no disponible (modo privado, etc.)
  }
  // Default: modo oscuro. El modo claro es opcional vía el toggle.
  return 'oscuro'
}

/**
 * Maneja el modo claro/oscuro por toggle (design_frontend_swt.md §2).
 * Sincroniza la clase `.dark` en <html> y persiste la elección.
 */
export function useTema() {
  const [tema, setTema] = useState(temaInicial)

  useEffect(() => {
    const raiz = document.documentElement
    raiz.classList.toggle('dark', tema === 'oscuro')
    try {
      localStorage.setItem(CLAVE, tema)
    } catch {
      // Sin persistencia; el tema sigue funcionando en esta sesión.
    }
  }, [tema])

  const alternarTema = useCallback(() => {
    setTema((actual) => (actual === 'oscuro' ? 'claro' : 'oscuro'))
  }, [])

  return { tema, alternarTema }
}
