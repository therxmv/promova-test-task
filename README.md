# Promova test task

### Description
Application that allows user to review the list of movies and add them to favorites.

:heavy_exclamation_mark: To build project locally you need to add `TMDB_TOKEN={your token here}` in `local.properties` OR you can just [download Release APK](https://github.com/therxmv/promova-test-task/releases/tag/v1.0.0).

### Optional Requirements
- :x: Implement sign-in with Google or Facebook
- :white_check_mark: Implement movie sharing via any existing provider

### Tech Stack:
- Kotlin
- Jetpack Compose
- Navigation 3
- Paging 3
- Coroutines
- Koin
- Room
- Ktor

#### Architecutre:
- MVVM (UDF)
- Data/Domain/UI
- Multi-module

#### Tests:
- Junit
- Mockk
- Kotest
- Turbine