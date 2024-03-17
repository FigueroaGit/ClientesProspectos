![FinaCredito-Prospectos-Project-1200x630px](https://github.com/FigueroaGit/ClientesProspectos/assets/37342701/4f2e2f14-7736-4791-a0b8-f349e8e4e11b)

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=24"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
</p>

<p align="center">  
💸 FinaCredito Prospectos demonstrates modern Android development with Hilt, Coroutines, Jetpack (RESTful services, ViewModel), and Material Design based on MVVM architecture.
</p>
</br>

## 📷 Screenshots (Light & Dark Color Schemes)
### Light mode
<div align="center">
  <img width="300" alt="finacredito-login" src="/screenshots/login_light_mode.png"> <img width="300" alt="finacredito-home" src="/screenshots/home_light_mode.png"> <img width="300" alt="finacredito-capture-data" src="/screenshots/capture_data_light_mode.png"> <img width="300" alt="finacredito-data-list" src="/screenshots/data_list_light_mode.png"> <img width="300" alt="finacredito-prospect-information" src="/screenshots/prospect_information_light_mode.png">
</div>

### Dark mode
<div align="center">
  <img width="300" alt="finacredito-login-dark" src="/screenshots/login_dark_mode.png"> <img width="300" alt="finacredito-home-dark" src="/screenshots/home_dark_mode.png"> <img width="300" alt="finacredito-capture-data-dark" src="/screenshots/capture_data_dark_mode.png"> <img width="300" alt="finacredito-data-list-dark" src="/screenshots/data_list_dark_mode.png"> <img width="300" alt="finacredito-prospect-information-dark" src="/screenshots/prospect_information_dark_mode.png">
</div>

### Dynamic Colors - Android 12+
Dynamic Color enables users to personalize their devices to align tonally with the color scheme of their personal wallpaper or through a selected color in the wallpaper picker. You can read more about Dynamic Colors [here](https://developer.android.com/develop/ui/views/theming/dynamic-colors).


## Example of App working

https://github.com/FigueroaGit/ClientesProspectos/assets/37342701/7fb7f250-e618-45b8-9157-eaccfbefb3a7

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

## Prospects API

FinaCredito Prospectos using my own API called [prospects](https://github.com/FigueroaGit/prospects)
Prospects provides a RESTful API interface to manage all data of people and files upload in your own server with SpringBoot + MongoDB

## Get Started

There are a few ways to open this project.

### Android Studio

1. `Android Studio` -> `File` -> `New` -> `From Version control` -> `Git`
2. Enter `https://github.com/FigueroaGit/ClientesProspectos.git` into URL field and press `Clone` button

### Command-line And Android Studio

1. Run `git clone https://github.com/FigueroaGit/ClientesProspectos.git` command to clone the project
2. Open `Android Studio` and select `File | Open...` from the menu. Select the cloned directory and press `Open` button

## ⏭️ Future Work
- Add Room database for persistance
- Enhance UI
- Modularize code

## 🤝 Want to Contribute?
All contributions are welcomed. This project is still in development. If you encounter any problems, please create an issue [here](https://github.com/FigueroaGit/ClientesProspectos/issues) & if you want to contribute to this project, PRs are welcomed! 🙂

## 👨‍💻 Developed By

<a href="https://www.facebook.com/hmfp24/" target="_blank">
  <img src="https://avatars.githubusercontent.com/u/37342701?v=4" width="70" align="left">
</a>

**Hector Figueroa**

[![Facebook](https://img.shields.io/badge/-facebook-grey?logo=facebook)](https://www.facebook.com/hmfp24/)
[![Linkedin](https://img.shields.io/badge/-linkedin-grey?logo=linkedin)](https://www.linkedin.com/in/hmfp24/)
