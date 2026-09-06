import { useCallback, useEffect, useRef, useState } from 'react'

/**
 * Ejecuta una petición asíncrona y expone { datos, cargando, error, recargar }.
 * `fn` debe ser estable (envuélvela en useCallback si depende de props/estado).
 *
 * Cada llamada a `ejecutar` (por montaje, por cambio de `fn` o por `recargar`)
 * invalida la anterior mediante un contador de corridas, así una respuesta
 * lenta que llega tarde no pisa a una más nueva.
 */
export function usePeticion(fn) {
  const [datos, setDatos] = useState(null)
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(null)
  const corridaRef = useRef(0)

  const ejecutar = useCallback(() => {
    const corrida = (corridaRef.current += 1)
    const vigente = () => corrida === corridaRef.current
    setCargando(true)
    setError(null)
    fn()
      .then((resultado) => {
        if (vigente()) setDatos(resultado)
      })
      .catch((err) => {
        if (vigente()) setError(err)
      })
      .finally(() => {
        if (vigente()) setCargando(false)
      })
  }, [fn])

  useEffect(() => {
    // Lanzar la petición al montar es justamente la función de este hook.
    // oxlint-disable-next-line react/set-state-in-effect
    ejecutar()
    // Al desmontar o cambiar `fn`, invalida cualquier petición en curso.
    return () => {
      corridaRef.current += 1
    }
  }, [ejecutar])

  return { datos, cargando, error, recargar: ejecutar }
}
