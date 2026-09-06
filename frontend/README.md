# Frontend — Weekly Service Tracker

App web para conductores. React + Vite + Tailwind CSS v4 + React Router.

## Requisitos

- Node 20+ (probado con Node 24)
- Backend Spring Boot corriendo (por defecto en `http://localhost:8080`)

## Puesta en marcha

```bash
cp .env.example .env   # ajusta VITE_API_URL si el backend no está en :8080
npm install
npm run dev            # servidor de desarrollo (http://localhost:5173)
```

Otros comandos: `npm run build` (producción en `dist/`), `npm run preview`.

## Estructura

```
src/
  main.jsx                    BrowserRouter + AuthProvider
  App.jsx                     Rutas: /login (público) y / (protegido)
  pages/
    LoginRegistro.jsx         Pantalla 1: Login / Registro (conectada al backend)
    Dashboard.jsx             Pantalla 2: placeholder, se construye después
  components/
    RutaProtegida.jsx         Redirige a /login si no hay sesión
    BotonTema.jsx             Toggle de modo claro/oscuro
  context/AuthContext.jsx     useAuth(): token, login, registrar, logout
  hooks/useTema.js            Estado del tema (default oscuro, persistido)
  lib/
    api.js                    fetch con base URL, JSON y Bearer token; ApiError / RedError
    autenticacion.js          iniciarSesion (POST /auth), registrarUsuario (POST /usuarios)
    token.js                  Guardado del JWT en localStorage + decodificado del payload
public/
  logo.svg                    Logo de la app (colócalo aquí; hay un fallback si falta)
  favicon.svg
```

## Autenticación

- Login: `POST /auth` con `{ nombreUsuario, pin }` → `{ token }`. El backend
  responde `401` si el usuario o el PIN son incorrectos.
- Registro: `POST /usuarios` con `{ nombreUsuario, pin }` → `201`. Responde `409`
  si el nombre de usuario ya existe. Tras registrar, el front hace login
  automáticamente.
- El JWT (expira en 1 hora, sin refresh) se guarda en `localStorage` como
  `swt-token` y se manda como `Authorization: Bearer <token>`.
- El campo de acceso es un **PIN** (numérico en los ejemplos del backend), no una
  contraseña — el backend lo llama `pin`.

## Diseño

Paleta, tipografía y layout siguen `MD/design_frontend_swt.md`. Los colores se
exponen como tokens en `src/index.css` (`bg-fondo`, `text-primario`,
`bg-acento`, ...) y cambian de valor bajo la clase `.dark` en `<html>`.
El modo oscuro es el tema por defecto; el claro es opcional vía el toggle.
