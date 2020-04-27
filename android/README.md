# CoroutinesAndroid
Proyecto Android base que implementa los ultimos componentes de arquitectura. Escrito en Kotlin cuya arquitectura es basada en MVVM con los componentes de Jitpack y Coroutines tomada y modificada del ejemplo [GithubBrowserSample](https://github.com/android/architecture-components-samples/tree/master/GithubBrowserSample) de Google. Este repositorio también incluye la libreria de utilitarios varios que te ayudarán a desarrollar más rápido.

### Requisitos
Para correr correctamente el proyecto correctamente se requiere:
```
Conocimientos de Kotlin y Jitpack.
Android Studio 3.5 o superior.
```
## Autor
* **Juan Ortiz** - *Ing.Multimedia* - [Athorfeo](https://github.com/Athorfeo)

## Licencia
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
**Free Software, Hell Yeah!**

# Kotlin Utilities Library
Libreria que contiene varios utilitarios en Kotlin que te ayudarán a desarrollar más rapido. Aún se encuentra en desarrollo por lo que la implementación debe ser en SNAPSHOT.

## Implementación
Lo primero es agregar la url de maven a las dependecias del proyecto en el archivo *build.gradle*
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
...
```
Luego, debemos agregar en las dependecias del la applicacion *(app/build.gradle)* la librería
```gradle
dependencies {
    implementation 'com.github.athorfeo:coroutinesandroid:-SNAPSHOT'
...
```
