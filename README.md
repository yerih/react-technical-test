# Face SDK offline Match Face Sample (Android)

## Solución

Se desarrolla una aplicación android de diseño simple. Consta de 4 pantallas:

1. WelcomeScreen: para bienvenida e inicialización de librerías.
2. ImageAScreen: para selección de imágenes mediante android gallery.
3. ImageBScreen: para lanzar el FaceSDK de regular e identificar rostro y guardar imagen a comparar.
4. MatchScreen: donde se mostrarán los resultados y similitud de acuerdo al FaceSDK.

En este proyecto se aplica una arquitectura multimodular basada en clean architecture para lograr un código
que aplique los SOLID principles. 

Adicionalmente, se hace uso de patrones de presentación MVVM y vistas reactivas mediante StateFlows para lograr 
una separación de lógica de negocio y lógica de interfaz gráfica.

## Decisiones técnicas destacadas

### Flujo de una sola vista
Al principio del proyecto se intentó implementar el desarrollo en una vista, pero iba a romper con los patrones 
y código limpio. Al final se decidió añadir más pantallas a la navegación y compartir el viewModel. 


## Patrones de diseño aplicados

Arquitectónicos
2. Multimodular y clean architecture
3. MVVM
4. Repository


Creacionales
1. Inyeccion de dependencias
2. Singleton (scopes)
3. Factory/Provider
4. Builder






