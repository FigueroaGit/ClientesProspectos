![FinaCredito-Prospectos-Project-1200x630px](https://github.com/FigueroaGit/ClientesProspectos/assets/37342701/4f2e2f14-7736-4791-a0b8-f349e8e4e11b)

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=24"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
</p>

<p align="center">  
💸 FinaCredito Prospectos demonstrates modern Android development with Hilt, Coroutines, Jetpack (RESTful services, ViewModel), and Material Design based on MVVM architecture.
</p>
</br>

## Tech stack & Open-source libraries
- Minimum SDK level 24
* [100% Kotlin](https://kotlinlang.org/)
    + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations
    + [Kotlin Symbol Processing](https://kotlinlang.org/docs/ksp-overview.html) - enable compiler plugins
    + [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html) - parse [JSON](https://www.json.org/json-en.html)
* [Jetpack](https://developer.android.com/jetpack)
    - [Compose](https://developer.android.com/jetpack/compose) - modern, native UI kit
    - [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - in-app navigation
    - [Hilt](https://dagger.dev/hilt/): for dependency injection.
- Architecture
  - MVVM Architecture (Model - View - ViewModel)
  - Repository Pattern
  - [Android KTX](https://developer.android.com/kotlin/ktx) - Jetpack Kotlin extensions
- Material Design 3
  - [Material-Components](https://m3.material.io/components): Material design components for building all components.
  - Theme selection
    - [Dark Theme](https://material.io/develop/android/theming/dark) - dark theme for the app (Android 10+)
    - [Dynamic Theming](https://m3.material.io/styles/color/dynamic-color/overview) - use generated, wallpaper-based theme (Android 12+)
    - Theme generated via [Material Theme Builder](https://m3.material.io/theme-builder)
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API.tines Flow.
