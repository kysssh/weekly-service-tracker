import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import { defineConfig } from 'vite'
import { VitePWA } from 'vite-plugin-pwa'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
    VitePWA({
      // El Service Worker se actualiza solo cuando hay una versión nueva
      // desplegada; el usuario no tiene que hacer nada.
      registerType: 'autoUpdate',

      // Ficheros extra (no versionados con hash) que también se precachean
      // para que la app arranque sin conexión.
      includeAssets: ['apple-touch-icon.png', 'logo_app.png', 'icono_nofondo.png'],

      // Contenido del manifest. vite-plugin-pwa lo emite como
      // /manifest.webmanifest y lo enlaza en el <head> automáticamente.
      manifest: {
        name: 'Weekly Service Tracker',
        short_name: 'Servicios',
        description: 'Registro semanal de servicios y ganancias.',
        lang: 'es',
        start_url: '/',
        scope: '/',
        display: 'standalone',
        orientation: 'portrait',
        // Fondo de la splash screen (modo oscuro, que es el default de la app).
        background_color: '#101826',
        // Color de la barra de estado / UI del sistema. Igual que el <meta>
        // theme-color del index.html.
        theme_color: '#1e3a5f',
        icons: [
          {
            src: 'pwa-192x192.png',
            sizes: '192x192',
            type: 'image/png',
          },
          {
            src: 'pwa-512x512.png',
            sizes: '512x512',
            type: 'image/png',
          },
          {
            // Versión "maskable": Android la recorta a su forma de icono
            // (círculo, squircle...) sin cortar el logo.
            src: 'pwa-maskable-512x512.png',
            sizes: '512x512',
            type: 'image/png',
            purpose: 'maskable',
          },
        ],
      },

      workbox: {
        // Todo lo que entra en el precache del Service Worker.
        globPatterns: ['**/*.{js,css,html,ico,png,svg,woff,woff2}'],
        // SPA: cualquier ruta desconocida cae en index.html.
        navigateFallback: 'index.html',
        cleanupOutdatedCaches: true,
        clientsClaim: true,
      },

      // El SW no se registra en `vite dev` para no ensuciar el desarrollo.
      devOptions: {
        enabled: false,
      },
    }),
  ],
})
