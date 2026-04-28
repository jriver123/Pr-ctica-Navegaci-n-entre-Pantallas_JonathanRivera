# Ruta Nica

**Ruta Nica** es una aplicación móvil básica desarrollada en Android utilizando **Kotlin** y **Jetpack Compose**. El proyecto fue realizado como parte de la práctica de navegación entre pantallas de la asignatura **POO II – Desarrollo de aplicaciones móviles en Android**.

La aplicación simula una plataforma turística donde el usuario puede explorar destinos de Nicaragua, seleccionar una ruta principal, agregar lugares a favoritos, activar un recordatorio y revisar el resumen de su viaje.

## Objetivo del proyecto

El objetivo principal de este proyecto es implementar un sistema de navegación entre pantallas utilizando Jetpack Compose, comprendiendo el flujo de usuario, el manejo de múltiples vistas y el uso de componentes interactivos básicos dentro de una aplicación móvil.

## Características principales

- Navegación entre tres pantallas principales.
- Interfaz desarrollada con Jetpack Compose.
- Uso de `NavController` y `NavHost` para la navegación.
- Barra inferior de navegación.
- Pantalla de inicio con destino destacado.
- Pantalla de destinos con tarjetas interactivas.
- Pantalla de resumen de ruta.
- Selección de destino principal.
- Sistema básico de favoritos.
- Activación de recordatorio mediante checkbox.
- Botones funcionales en todas las pantallas.

## Pantallas de la aplicación

### Inicio

La pantalla de inicio muestra el destino seleccionado actualmente, estadísticas básicas y accesos rápidos para explorar destinos, elegir un destino recomendado o revisar la ruta del usuario.

### Destinos

La pantalla de destinos permite visualizar diferentes lugares turísticos de Nicaragua. Cada destino incluye ubicación, duración, precio estimado, calificación y etiquetas descriptivas. El usuario puede seleccionar un destino o agregarlo a favoritos.

### Mi ruta

La pantalla de mi ruta presenta un resumen del destino seleccionado, la cantidad de favoritos guardados, el estado de la ruta y la opción de activar un recordatorio o confirmar la ruta.

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Android Studio

## Requisitos para ejecutar el proyecto

Antes de ejecutar la aplicación, se necesita tener instalado:

- Android Studio
- JDK configurado
- Emulador Android o dispositivo físico
- Conexión a internet para sincronizar dependencias de Gradle

## Dependencia principal

El proyecto utiliza la dependencia de Navigation Compose para manejar la navegación entre pantallas:

```kotlin
implementation("androidx.navigation:navigation-compose:2.9.8")
