import { Navigate, Route, Routes } from 'react-router-dom'
import RutaProtegida from './components/RutaProtegida'
import Dashboard from './pages/Dashboard'
import LoginRegistro from './pages/LoginRegistro'
import MisServicios from './pages/MisServicios'

function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginRegistro />} />

      <Route
        path="/"
        element={
          <RutaProtegida>
            <Dashboard />
          </RutaProtegida>
        }
      />
      <Route
        path="/servicios"
        element={
          <RutaProtegida>
            <MisServicios />
          </RutaProtegida>
        }
      />

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default App
