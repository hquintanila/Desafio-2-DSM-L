# Desafío Práctico 2 - Agencia de Viajes (DSM-L)

**Nombre: Harold Albeiro Quintanilla Rodríguez (QR241622)  
**Enlace Explicación: 

Aplicación móvil desarrollada en Android (Kotlin) para la gestión y visualización de viajes turísticos, integrada con Firebase Authentication y Cloud Firestore.

## Características

* **Autenticación:** Inicio de sesión y registro de usuarios con Firebase Auth.
* **Catálogo de Viajes (Read):** Listado dinámico cargado en tiempo real desde Cloud Firestore usando RecyclerView con resolución dinámica de imágenes locales y remotas.
* **Carrito de Compras:** Gestión en memoria para agregar, eliminar elementos individuales y vaciar el carrito mediante `CartManager` y la pantalla `CartActivity`.
* **Detalle del Viaje:** Pantalla con información completa del destino, precio, duración y reservas.
* **Gestión de Sesión:** Cierre de sesión seguro y barra superior personalizada (Toolbar).

## Tecnologías Utilizadas

* **Lenguaje:** Kotlin / Android SDK
* **UI/UX:** ViewBinding, Material Design 3 (Modo Oscuro `#121212`)
* **Backend & BD:** Firebase Authentication, Cloud Firestore
* **Gestión de Imágenes:** Glide y mapeo dinámico de recursos `drawable`

## Estructura del Proyecto

Desafio2DSML/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/desafio_2dsm_l/
│   │       │   ├── adapter/
│   │       │   │   └── ViajeAdapter.kt         # Adaptador con mapeo dinámico de imágenes
│   │       │   ├── model/
│   │       │   │   ├── CartManager.kt          # Administrador singleton del carrito (Agregar/Eliminar/Total)
│   │       │   │   └── Viaje.kt                # Modelo de datos sincronizado con Cloud Firestore
│   │       │   ├── CartActivity.kt             # Pantalla de gestión y visualización del carrito
│   │       │   ├── DetailActivity.kt           # Pantalla con la información detallada del viaje
│   │       │   ├── LoginActivity.kt            # Autenticación e inicio de sesión
│   │       │   ├── MainActivity.kt             # Catálogo principal desde Firestore y menú de Toolbar
│   │       │   └── RegisterActivity.kt         # Registro de nuevos usuarios
│   │       ├── res/
│   │       │   ├── drawable/               # Recursos gráficos locales de los destinos
│   │       │   ├── layout/                 # Archivos de diseño XML de actividades y tarjetas
│   │       │   ├── menu/
│   │       │   │   └── menu_main.xml           # Menú de la Toolbar (Carrito y Cierre de sesión)
│   │       │   ├── values/
│   │       │   │   ├── colors.xml              # Paleta de colores corporativa
│   │       │   │   ├── strings.xml             # Centralización de textos de la interfaz
│   │       │   │   └── themes.xml              # Tema base en modo claro
│   │       │   └── values-night/
│   │       │       └── themes.xml              # Personalización de estilos para el modo oscuro
│   │       └── AndroidManifest.xml             # Permisos de red, actividades y configuración del tema
│   └── build.gradle.kts                        # Dependencias del proyecto (Firebase, Glide)
├── build.gradle.kts                            # Configuración global de Gradle
└── README.md                                   # Documentación oficial del repositorio
