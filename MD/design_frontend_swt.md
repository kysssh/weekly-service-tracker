# Frontend Design Brief: Weekly Service Tracker (App para Conductores)

## 1. Contexto y Audiencia
- **Usuario objetivo:** Conductores de vehículos que necesitan registrar sus servicios diarios/semanales y controlar sus ganancias y peajes.
- **Nivel técnico:** Bajo/Medio. La aplicación debe ser extremadamente intuitiva, sin jerga técnica (ej. usar "Registrar servicio" en lugar de "Crear entidad").
- **Contexto de uso:** Se utilizará principalmente en pausas, al finalizar un viaje o al terminar el turno (no mientras conducen). Por lo tanto, no requiere un diseño tipo "GPS en vivo", pero sí legibilidad clara.
- **Personalidad:** Confiable, seria, pero amigable y moderna. (Inspiración de vibra: Yape, pero sin los colores ni el tono excesivamente infantil).

## 2. Principios de Diseño Visual
- **Jerarquía y Fricción:** Botones grandes, textos legibles, pocos pasos por pantalla.
- **Iconografía y Decoración:** Evitar clip-arts genéricos (como un autito genérico de stock). Utilizar en su lugar líneas simples tipo "trayecto/ruta" como acento decorativo (ej. una línea de puntos sutil en el fondo del header del login).
- **Soporte de Tema:** Modo claro por defecto, con modo oscuro opcional a través de un toggle.

## 3. Paleta de Colores (Tokens Semánticos)
Se implementará utilizando tokens o variables CSS (ej. en Tailwind) para facilitar el cambio entre modo claro y oscuro.

### Modo Claro (Default)
- **Fondo principal (`color-fondo`):** `#F7F8FA` (Gris muy claro, reduce la fatiga comparado con el blanco puro).
- **Superficie / Tarjetas (`color-superficie`):** `#FFFFFF` (Blanco con sombras suaves para despegarse del fondo).
- **Color Primario / Confianza (`color-primario`):** `#1E3A5F` (Azul marino profundo. Usado en navegación, headers, y botones de acción principal. Transmite seguridad).
- **Color de Acento / Positivo (`color-acento`):** `#0F9B8E` (Verde-azulado. Usado para acciones positivas, indicadores de ganancia, bordes de selección. Aporta el tono "amigable").
- **Color Alerta / Error (`color-alerta`):** `#D64545` (Rojo suave para errores o acciones destructivas).
- **Texto Principal (`color-texto`):** Gris oscuro/Casi negro para máxima legibilidad.

### Modo Oscuro
- **Fondo principal (`color-fondo-dark`):** `#101826` (Gris azulado muy oscuro, no negro puro, para ser menos agresivo a la vista).
- **Superficie / Tarjetas (`color-superficie-dark`):** Un tono ligeramente más claro que el fondo oscuro (ej. `#1F2937`) para mantener la jerarquía visual de las tarjetas.

## 4. Tipografía
- **Títulos y Números grandes (Ganancias):** Fuente con un poco de personalidad, ligeramente redondeada o geométrica, pero muy sólida (transmite "amigable" pero "confiable").
- **Cuerpo / Datos:** Fuente neutra, limpia y altamente legible (ej. Inter, Roboto o sistema base).

## 5. Estructura de Pantallas Principales

### Pantalla 1: Login / Registro
- **Inspiración del flujo:** Tipo Facebook login.
- **Header:** Fondo azul marino (`#1E3A5F`), puede incluir un acento decorativo de ruta (línea de puntos) y el logo de la aplicación centrado (ej. ícono de ruta/S o el avión).
- **Contenedor (Card):** Tarjeta blanca superpuesta con bordes redondeados.
- **Contenido:** 
  - Saludo amigable ("Bienvenido", "Ingresa para registrar tus servicios").
  - Formulario claro con inputs mediano (Usuario y Contraseña).
  - Botón principal de tamaño completo en azul marino.
  - Enlace secundario para registro en la parte inferior (usando el verde-azulado de acento).

### Pantalla 2: Panel Principal (Dashboard)
- **Header/Resumen:** 
  - Saludo ("Hola, [Nombre]").
  - **Dato principal gigante:** "Ganancia esta semana" seguido del monto (ej. `S/ 842.50`). Este es el punto focal de la pantalla.
- **Acciones principales (Tarjetas grandes o botones expansivos):**
  1. **Registrar servicio:** Tarjeta prominente con ícono de `+`. (En el modo activo puede tener un borde del color de acento `#0F9B8E`).
  2. **Ver mis servicios:** Tarjeta secundaria con ícono de lista. Debe permitir al usuario alternar internamente entre la semana actual y semanas anteriores.
- **Navegación:** Un pequeño menú superior o toggle para cambiar entre modo claro y oscuro, panel principal y cerrar sesión.

## 6. Notas de Implementación para la IA (Claude)
- Por favor, utiliza Tailwind CSS (o el framework preferido del proyecto) para estructurar esto mediante componentes.
- Implementa soporte para `dark:` mode en Tailwind directamente basándote en los colores mencionados.
- Estructura el CSS para que los botones y tarjetas tengan una buena área táctil (`min-height: 48px` o clases tipo `p-4`) pensando en usuarios móviles, aunque sea una web app.
