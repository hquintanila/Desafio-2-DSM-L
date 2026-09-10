# Desafío Práctico 2 - Agencia de Viajes (DSM-L)

Nombre: Harold Albeiro Quintanilla Rodriguez (QR241622)

Link Explicación:

Aplicación móvil desarrollada en Android (Kotlin) para la gestión y visualización de viajes turísticos, integrada con **Firebase Authentication** y **Cloud Firestore**.

## Características
- **Autenticación:** Inicio de sesión y registro de usuarios con Firebase Auth.
- **Catálogo de Viajes:** Listado dinámico cargado en tiempo real desde Cloud Firestore usando RecyclerView con resolución dinámica de imágenes locales y remotas.
- **Carrito de Compras:** Gestión completa de pedidos mediante `CartManager` y pantalla `CartActivity`.
- **Detalle del Viaje:** Pantalla con información completa del destino, precio y duración.
- **Gestión de Sesión:** Cierre de sesión seguro y privacidad del correo electrónico enmascarado desde la barra superior de herramientas (Toolbar).

## Tecnologías utilizadas
- Kotlin / Android SDK
- View Binding & Material Design
- Firebase Authentication
- Cloud Firestore
- Glide (Carga de imágenes)

---

## Estructura del Proyecto

A continuación se detalla la arquitectura de archivos del proyecto, organizada bajo patrones de diseño limpios y separación de responsabilidades:

```text
Desafio2DSML/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/desafio_2dsm_l/
│   │       │   ├── adapter/
│   │       │   │   └── ViajeAdapter.kt         # Adaptador con soporte para mapeo dinámico de drawables
│   │       │   ├── model/
│   │       │   │   ├── CartManager.kt          # Administrador en memoria del carrito de compras
│   │       │   │   └── Viaje.kt                # Data Class / Modelo de datos sincronizado con Firestore
│   │       │   ├── CartActivity.kt             # Pantalla de gestión y visualización del carrito
│   │       │   ├── DetailActivity.kt           # Pantalla con la información detallada del viaje
│   │       │   ├── LoginActivity.kt            # Autenticación e inicio de sesión
│   │       │   ├── MainActivity.kt             # Listado exclusivo de Firestore y menú contextual
│   │       │   └── RegisterActivity.kt         # Registro de nuevos usuarios
│   │       ├── res/
│   │       │   ├── drawable/
│   │       │   │   ├── alaska.jpg              # Recursos gráficos del catálogo
│   │       │   │   ├── cancun1.jpg
│   │       │   │   ├── cartagena.jpg
│   │       │   │   └── janeiro.jpg
│   │       │   ├── layout/
│   │       │   │   ├── activity_cart.xml       # Diseño del carrito de compras
│   │       │   │   ├── activity_detail.xml     # Diseño de la vista de detalles
│   │       │   │   ├── activity_login.xml      # Diseño del formulario de Login
│   │       │   │   ├── activity_main.xml       # Diseño principal (Toolbar + RecyclerView)
│   │       │   │   ├── activity_register.xml   # Diseño del formulario de Registro
│   │       │   │   └── item_viaje.xml          # Diseño de la tarjeta individual (CardView estilizado)
│   │       │   ├── menu/
│   │       │   │   └── menu_main.xml           # Menú de la Toolbar (Carrito de compras y Cerrar sesión)
│   │       │   └── values/
│   │       │       ├── colors.xml              # Paleta de colores de la app
│   │       │       ├── strings.xml             # Recursos de cadenas de texto
│   │       │       └── themes.xml              # Estilos y temas de Material Design
│   │       └── AndroidManifest.xml             # Permisos de red, declaraciones y navegación parental
│   └── build.gradle.kts                        # Dependencias a nivel de módulo (Firebase, Glide)
├── build.gradle.kts                            # Configuración de Gradle a nivel de proyecto
└── README.md                                   # Documentación oficial del repositorio
