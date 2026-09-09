# Desafío Práctico 2 - Agencia de Viajes (DSM-L)

Nombre: Harold Albeiro Quintanilla Rodriguez (QR241622)

Link Explicacion:

Aplicación móvil desarrollada en Android (Kotlin) para la gestión y visualización de viajes turísticos, integrada con **Firebase Authentication** y **Cloud Firestore**.

##  Características
- **Autenticación:** Inicio de sesión y registro de usuarios con Firebase Auth.
- **Catálogo de Viajes:** Listado dinámico cargado en tiempo real desde Cloud Firestore usando RecyclerView.
- **Detalle del Viaje:** Pantalla con información completa del destino, precio y duraciones.
- **Gestión de Sesión:** Cierre de sesión seguro desde la barra superior de herramientas (Toolbar).

##  Tecnologías utilizadas
- Kotlin / Android SDK
- View Binding & Material Design
- Firebase Authentication
- Cloud Firestore
- Glide (Carga de imágenes)

-------

##  Estructura del Proyecto

A continuación se detalla la arquitectura de archivos del proyecto, organizada bajo patrones de diseño limpios y separación de responsabilidades:

```text
Desafio2DSML/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/desafio_2dsm_l/
│   │       │   ├── adapter/
│   │       │   │   └── ViajeAdapter.kt         # Adaptador para el RecyclerView (Manejo de ítems)
│   │       │   ├── model/
│   │       │   │   └── Viaje.kt                # Data Class / Modelo de datos para Firestore
│   │       │   ├── DetailActivity.kt           # Pantalla con la información detallada del viaje
│   │       │   ├── LoginActivity.kt            # Autenticación e inicio de sesión
│   │       │   ├── MainActivity.kt             # Listado de viajes desde Firestore y menú
│   │       │   └── RegisterActivity.kt         # Registro de nuevos usuarios
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   ├── activity_detail.xml     # Diseño de la vista de detalles
│   │       │   │   ├── activity_login.xml      # Diseño del formulario de Login
│   │       │   │   ├── activity_main.xml       # Diseño principal (Toolbar + RecyclerView)
│   │       │   │   ├── activity_register.xml   # Diseño del formulario de Registro
│   │       │   │   └── item_viaje.xml          # Diseño de la tarjeta individual (CardView)
│   │       │   ├── menu/
│   │       │   │   └── menu_main.xml           # Menú contextual de la Toolbar (Cerrar sesión)
│   │       │   └── values/
│   │       │       ├── colors.xml              # Paleta de colores de la app
│   │       │       ├── strings.xml             # Recursos de cadenas de texto
│   │       │       └── themes.xml              # Estilos y temas de Material Design
│   │       └── AndroidManifest.xml             # Configuración de permisos y actividades
│   └── build.gradle.kts                        # Dependencias a nivel de módulo (Firebase, Glide)
├── build.gradle.kts                            # Configuración de Gradle a nivel de proyecto
└── README.md                                   # Documentación oficial del repositorio
