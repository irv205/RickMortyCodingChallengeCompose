# Rick & Morty Coding Challenge - Jetpack Compose

Aplicación Android desarrollada con **Jetpack Compose** que consume la [Rick and Morty API](https://rickandmortyapi.com/) para mostrar un listado de personajes con paginación infinita y una pantalla de detalle de cada personaje.

Este proyecto fue creado con fines educativos para demostrar la implementación de una arquitectura limpia (Clean Architecture) en Android con las mejores prácticas modernas.

## 📱 Características

- **Listado de personajes**: Muestra todos los personajes de Rick and Morty en una lista
- **Paginación infinita (Infinite Scroll)**: Carga automáticamente más personajes al llegar cerca del final de la lista
- **Detalle de personaje**: Pantalla con información completa (estado, especie, género, origen, ubicación, etc.)
- **Navegación type-safe**: Navegación entre pantallas con Kotlin Serialization
- **Soporte tema claro/oscuro**: Se adapta al tema del sistema
- **Material You (Dynamic Color)**: Colores dinámicos en Android 12+
- **Manejo de estados**: Loading, Success, Error con UI reactiva

## 🏗️ Arquitectura

Este proyecto sigue los principios de **Clean Architecture** con separación por capas:

```
com.irv205.rickmortycodingchallengecompose
├── core/           # Utilidades compartidas, DI, helpers
├── data/           # Capa de datos (DTOs, API, Repository Implementation, Mappers)
├── domain/         # Capa de dominio (Modelos, Repositorios interfaces, UseCases)
├── presentation/   # Capa de UI (Composables, ViewModels, Navigation)
└── ui/theme/       # Tema, colores y tipografía
```

### Flujo de datos

```
UI (Composable) -> ViewModel -> UseCase -> Repository (Interface) -> RepositoryImpl -> ApiService (Retrofit)
```

**Regla de dependencia**: Las capas internas no conocen las externas. Domain no depende de Data ni de Presentation.

## 🛠️ Tecnologías y Librerías

| Librería | Uso |
|---|---|
| **[Jetpack Compose](https://developer.android.com/jetpack/compose)** | UI declarativa moderna |
| **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)** | Navegación entre pantallas con type-safe routes |
| **[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)** | Inyección de dependencias |
| **[Retrofit](https://square.github.io/retrofit/)** | Cliente HTTP para consumir REST API |
| **[OkHttp](https://square.github.io/okhttp/)** | Cliente HTTP con logging interceptor |
| **[Moshi](https://github.com/square/moshi)** | Parseo JSON |
| **[Coil](https://coil-kt.github.io/coil/)** | Carga de imágenes asíncrona |
| **[Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)** | Programación asíncrona |
| **[Kotlin Flow](https://kotlinlang.org/docs/flow.html)** | Flujos reactivos para estados |
| **[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)** | Gestión de estado UI con ciclo de vida |
| **[KSP](https://kotlinlang.org/docs/ksp-overview.html)** | Procesamiento de anotaciones (Kotlin Symbol Processing) |
| **[Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)** | Serialización para navegación type-safe |

## 📂 Explicación de Paquetes

### `core/`
- **`core/util/`**: Clases utilitarias reutilizables
  - `Resource.kt`: Sealed class para representar estados (Loading, Success, Error)
  - `SafeApiCall.kt`: Wrapper para llamadas seguras a API
- **`core/di/`**: Módulos de Hilt para inyección de dependencias
  - `NetworkModule.kt`: Provee Retrofit, OkHttp, Moshi, ApiService
  - `RepositoryModule.kt`: Vincula interfaces con implementaciones

### `data/`
- **`data/model/`**: DTOs que mapean la respuesta JSON de la API
- **`data/service/`**: Definición de endpoints con Retrofit (`ApiService`)
- **`data/mapper/`**: Conversión entre DTOs y modelos de dominio
- **`data/repository/`**: Implementación concreta de los repositorios

### `domain/`
- **`domain/model/`**: Entidades puras de Kotlin (sin dependencias Android/Retrofit)
- **`domain/repository/`**: Interfaces que definen contratos para obtener datos
- **`domain/usecase/`**: Casos de uso que encapsulan lógica de negocio

### `presentation/`
- **`presentation/navigation/`**: Rutas de navegación type-safe
- **`presentation/character/home/`**: Pantalla y lógica de la lista de personajes
- **`presentation/character/detail/`**: Pantalla y lógica del detalle de personaje
- **`presentation/MainActivity.kt`**: Activity principal con configuración de navegación
- **`App.kt`**: Clase Application para inicializar Hilt

### `ui/theme/`
- **`Color.kt`**: Paleta de colores
- **`Type.kt`**: Configuración tipográfica
- **`Theme.kt`**: Configuración del tema con soporte claro/oscuro y Dynamic Color

## 🔄 Patrones Implementados

- **MVVM (Model-View-ViewModel)**: Separación entre UI y lógica de presentación
- **Clean Architecture**: Separación por capas con inversión de dependencias
- **Repository Pattern**: Abstracción de fuentes de datos
- **Use Case Pattern**: Encapsulación de lógica de negocio
- **Single Activity Architecture**: Una sola Activity con navegación entre Composables
- **Unidirectional Data Flow (UDF)**: Estado fluye hacia abajo, eventos hacia arriba
- **State Hoisting**: Estado gestionado en ViewModels, UI es stateless donde es posible

## 🚀 Cómo ejecutar el proyecto

### Requisitos

- Android Studio Ladybug o superior
- JDK 11
- Android SDK con API 35
- Conexión a internet (para consumir la API)

### Pasos

1. Clonar el repositorio
```bash
git clone https://github.com/TU_USUARIO/RickMortyCodingChallengeCompose.git
cd RickMortyCodingChallengeCompose
```

2. Abrir en Android Studio
   - File > Open > Seleccionar la carpeta del proyecto

3. Sincronizar Gradle
   - Android Studio lo hará automáticamente, o ejecutar `./gradlew build`

4. Ejecutar la app
   - Seleccionar un emulador o dispositivo físico
   - Presionar Run (▶️) o `Shift+F10`

## 📡 API Utilizada

**[Rick and Morty API](https://rickandmortyapi.com/)** - API REST gratuita y pública

Endpoints usados:
- `GET /api/character` - Lista paginada de personajes
- `GET /api/character/{id}` - Detalle de un personaje
- Paginación mediante URL `next` proporcionada por la API

## 📝 Notas para Aprendizaje

Este proyecto está **completamente comentado** con explicaciones detalladas en español para facilitar el aprendizaje. Cada archivo incluye:

- **Propósito**: ¿Para qué sirve este archivo?
- **Conceptos clave**: Explicación de patrones, anotaciones y técnicas utilizadas
- **Flujo**: Cómo se conecta con el resto de la arquitectura
- **Razones de diseño**: ¿Por qué se hizo de esta manera?

Ideal para estudiar Clean Architecture, Jetpack Compose, Hilt, Retrofit y navegación moderna en Android.

## 📄 Licencia

Este proyecto es de uso educativo.
