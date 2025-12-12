# 🐉 Dragon Ball API - Aplicación Android

Una aplicación Android moderna desarrollada con Kotlin que consume la [Dragon Ball API](https://dragonball-api.com/) para mostrar información detallada sobre personajes y planetas del universo Dragon Ball.

## ✨ Características

- 📱 **Interfaz Moderna con Jetpack Compose**: UI declarativa y reactiva
- 🔄 **Arquitectura MVVM**: Separación clara de responsabilidades
- 💾 **Persistencia Local con Room**: Caché de datos offline
- 🌐 **Consumo de API REST**: Integración con Dragon Ball API usando Retrofit
- ⚡ **Inyección de Dependencias**: Implementado con Dagger Hilt
- 🎨 **Material Design 3**: Diseño moderno y consistente
- 🖼️ **Carga de Imágenes**: Optimizada con Coil
- 🧭 **Navegación**: Fluida entre pantallas con Navigation Compose
- ⭐ **Favoritos**: Sistema de marcado de personajes y planetas favoritos

## 📸 Capturas de Pantalla

_Próximamente_

## 🏗️ Arquitectura

El proyecto sigue la arquitectura **MVVM (Model-View-ViewModel)** recomendada por Google, con una clara separación de capas:

```
app/
├── data/                      # Capa de datos
│   ├── local/                # Persistencia con Room
│   │   ├── dao/             # Data Access Objects
│   │   ├── entity/          # Entidades de base de datos
│   │   └── database/        # Configuración de Room
│   ├── remote/              # API REST
│   │   └── DragonBallApi   # Interface de Retrofit
│   ├── model/               # Modelos de datos
│   └── repo/                # Repositorios (fuente única de verdad)
├── di/                       # Módulos de inyección de dependencias
├── ui/                       # Capa de presentación
│   ├── home/                # Pantalla principal
│   ├── list/                # Lista de personajes
│   ├── detail/              # Detalle de personaje
│   ├── planets/             # Sección de planetas
│   ├── navigation/          # Navegación
│   ├── common/              # Componentes reutilizables
│   └── theme/               # Tema de la aplicación
└── DragonBallApiApp.kt      # Punto de entrada de la app
```

## 🛠️ Tecnologías y Librerías

- **Lenguaje**: [Kotlin](https://kotlinlang.org/) 2.0
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Inyección de Dependencias**: [Dagger Hilt](https://dagger.dev/hilt/)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) + [Gson](https://github.com/google/gson)
- **Base de Datos**: [Room](https://developer.android.com/training/data-storage/room)
- **Carga de Imágenes**: [Coil](https://coil-kt.github.io/coil/)
- **Navegación**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- **Arquitectura**: [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- **Corrutinas**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Android Studio**: Hedgehog (2023.1.1) o superior
- **JDK**: Java 11 o superior
- **SDK de Android**: 
  - Nivel mínimo de API: 26 (Android 8.0)
  - Nivel objetivo de API: 34 (Android 14)
  - Nivel de compilación: 34
- **Gradle**: 8.5.1 o superior (incluido en el proyecto)

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/Mariogarluu/Dragonball_api_kotlin.git
cd Dragonball_api_kotlin
```

### 2. Abrir el Proyecto

1. Abre **Android Studio**
2. Selecciona **File > Open**
3. Navega hasta la carpeta del proyecto y selecciónala
4. Espera a que Gradle sincronice las dependencias

### 3. Ejecutar la Aplicación

#### Opción A: Usando Android Studio

1. Conecta un dispositivo Android o inicia un emulador
2. Haz clic en el botón **Run** (▶️) o presiona `Shift + F10`

#### Opción B: Usando la Línea de Comandos

```bash
# En Windows
./gradlew.bat installDebug

# En macOS/Linux
./gradlew installDebug
```

## 🔨 Compilación

### Compilar APK de Debug

```bash
./gradlew assembleDebug
```

El APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### Compilar APK de Release

```bash
./gradlew assembleRelease
```

El APK se generará en: `app/build/outputs/apk/release/app-release.apk`

## 🧪 Ejecutar Tests

```bash
# Tests unitarios
./gradlew test

# Tests de instrumentación (requiere dispositivo/emulador)
./gradlew connectedAndroidTest
```

## 📡 API de Dragon Ball

Esta aplicación consume la [Dragon Ball API](https://dragonball-api.com/), una API RESTful gratuita que proporciona información sobre:

- **Personajes**: Información detallada de todos los personajes de Dragon Ball
- **Planetas**: Datos sobre los planetas del universo Dragon Ball
- **Transformaciones**: Las diferentes transformaciones de los personajes

### Endpoints Utilizados

```kotlin
// Obtener lista de personajes
GET /api/characters?limit={limit}

// Obtener personaje por ID
GET /api/characters/{id}

// Obtener lista de planetas
GET /api/planets?limit={limit}

// Obtener planeta por ID
GET /api/planets/{id}
```

## 📂 Estructura del Proyecto

### Módulos Principales

#### 🗄️ Data Layer (Capa de Datos)

- **Remote**: Definición de la API con Retrofit
- **Local**: Base de datos Room con DAOs y entidades
- **Repository**: Implementación del patrón Repository para gestionar datos

#### 🎨 UI Layer (Capa de Presentación)

- **Screens**: Pantallas de la aplicación (Home, List, Detail, Planets)
- **ViewModels**: Lógica de negocio y estado de UI
- **Navigation**: Configuración de navegación entre pantallas
- **Theme**: Configuración de colores, tipografía y tema

#### 💉 Dependency Injection

- **DatabaseModule**: Provee instancias de Room
- **RemoteModule**: Provee instancias de Retrofit y la API

## 🎯 Funcionalidades Principales

### 1. Listado de Personajes
- Visualización en grid de todos los personajes
- Carga de imágenes optimizada
- Información básica de cada personaje

### 2. Detalle de Personaje
- Información completa del personaje
- Ki y máximo Ki
- Raza, género y afiliación
- Planeta de origen
- Transformaciones disponibles
- Opción para marcar como favorito

### 3. Planetas
- Lista de planetas del universo Dragon Ball
- Estado de destrucción
- Descripción detallada
- Imágenes de alta calidad

### 4. Sistema de Favoritos
- Persistencia local de favoritos
- Acceso rápido a personajes y planetas favoritos

## 🔧 Configuración Avanzada

### Variables de Entorno

Si necesitas cambiar la URL base de la API, edita el archivo:

```kotlin
// app/src/main/java/com/mariogarluu/dragonballapi/di/RemoteModule.kt

private const val BASE_URL = "https://dragonball-api.com/api/"
```

### Personalización del Tema

Los colores y estilos se pueden modificar en:

```kotlin
// app/src/main/java/com/mariogarluu/dragonballapi/ui/theme/
├── Color.kt      // Paleta de colores
├── Theme.kt      // Configuración del tema
└── Type.kt       // Tipografía
```

## 🤝 Contribuir

Las contribuciones son bienvenidas. Para contribuir:

1. **Fork** el proyecto
2. Crea una **rama** para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add: Amazing Feature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. Abre un **Pull Request**

### Estándares de Código

- Sigue las convenciones de Kotlin
- Usa nombres descriptivos para variables y funciones
- Añade comentarios KDoc para funciones públicas
- Mantén la arquitectura MVVM
- Escribe tests para nuevas funcionalidades

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 👨‍💻 Autor

**Mario García** - [@Mariogarluu](https://github.com/Mariogarluu)

## 🙏 Agradecimientos

- [Dragon Ball API](https://dragonball-api.com/) por proporcionar la API
- La comunidad de Android Developers
- Akira Toriyama por crear Dragon Ball

## 📞 Contacto

¿Tienes preguntas o sugerencias? No dudes en:

- Abrir un [Issue](https://github.com/Mariogarluu/Dragonball_api_kotlin/issues)
- Contactar al autor en GitHub

---

⭐ Si este proyecto te ha sido útil, no olvides darle una estrella en GitHub!
