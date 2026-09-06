import { useCallback, useEffect, useState } from 'react'

/**
 * Ejecuta una petición asíncrona y expone { datos, cargando, error, recargar }.
 * `fn` debe ser estable (envuélvela en useCallback si depende de props/estado).
 */
export function usePeticion(fn) {
  const [datos, setDatos] = useState(null)
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(null)

  const ejecutar = useCallback(() => {
    let vigente = true
    // Marcar "cargando" al montar es el comportamiento buscado de este hook de
    // fetch; el aviso de set-state-in-effect no aplica a este caso.
    // oxlint-disable-next-line react/set-state-in-effect
    setCargando(true)
    setError(null)
    fn()
      .then((resultado) => {
        if (vigente) setDatos(resultado)
      })
      .catch((err) => {
        if (vigente) setError(err)
      })
      .finally(() => {
        if (vigente) setCargando(false)
      })
    return () => {
      vigente = false
    }
  }, [fn])

  useEffect(ejecutar, [ejecutar])

  const recargar = useCallback(() => {
    ejecutar()
  }, [ejecutar])

  return { datos, cargando, error, recargar }
}
