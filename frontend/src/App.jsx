import { Navigate, Route, Routes } from 'react-router-dom'
import RutaProtegida from './components/RutaProtegida'
import LoginRegistro from './pages/LoginRegistro'

function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginRegistro />} />
      <Route
        path="/"
        element={
          <RutaProtegida>
            <div className="grid min-h-svh place-items-center bg-fondo text-texto">
              <p className="font-display text-lg font-semibold">
                Sesión iniciada
              </p>
            </div>
          </RutaProtegida>
        }
      />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default App
