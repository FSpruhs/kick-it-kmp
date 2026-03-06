# KickIt KMP ⚽

A **Kotlin Multiplatform (KMP)** mobile application for organizing soccer/football matches. Players can form groups, plan upcoming matches, record results, track attendance, and view personal statistics — all backed by a Spring Boot REST API.

---

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Backend (Docker)](#backend-docker)
  - [Android App](#android-app)
- [Build Commands](#build-commands)
- [Code Style](#code-style)
- [Contributing](#contributing)

---

## Features

- 🔐 **Authentication** — JWT-based login and registration
- 👥 **Groups** — Create groups, invite players, manage members
- 📅 **Match Planning** — Schedule upcoming matches with configurable squad sizes and locations
- ✅ **Attendance** — Register or deregister for matches; waiting bench support
- 🏆 **Results** — Enter match results and track win/loss/draw per player
- 📊 **Statistics** — View individual player stats across all matches
- 👤 **Profile** — Manage your user profile and group roles

---

## Architecture

The project follows **Clean Architecture** combined with the **MVI (Model-View-Intent)** pattern.

```
Feature/
├── application/     # Domain layer: use cases, repository interfaces, domain models
├── data/            # Data layer: repository implementations, Ktorfit API clients
├── presentation/    # Presentation layer: ViewModel, UIState, Intent, Effect
└── di/              # Koin dependency injection module
```

### MVI Flow

```
User Action → Intent → ViewModel.processIntent() → update UIState / emit Effect → UI re-renders
```

Each feature ViewModel extends `BaseViewModel<Intent, Effect, UIState>` and uses `performAction { }` to handle loading/error state automatically.

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Kotlin Multiplatform | 2.2.10 |
| UI (Android) | Jetpack Compose / Compose Multiplatform | 1.8.2 |
| Dependency Injection | Koin | 4.1.1 |
| HTTP Client | Ktor + Ktorfit | 3.4.0 / 2.7.2 |
| Local Database | Room (SQLite) | 2.8.4 |
| Async | Kotlin Coroutines | 1.10.2 |
| Serialization | kotlinx-serialization | 1.10.0 |
| Navigation | Navigation Compose | 2.9.3 |
| Image Loading | Coil | 2.7.0 |
| JWT Parsing | JJWT (Android only) | 0.13.0 |
| Linting | Ktlint + Detekt | 14.0.1 / 1.23.8 |
| Code Generation | KSP | bundled |
| Backend | Spring Boot (Docker image) | — |

---

## Project Structure

```
kick-it-kmp/
├── composeApp/              # Android application (Jetpack Compose UI, navigation)
│   └── src/androidMain/
│       └── kotlin/com/spruhs/
│           ├── screens/     # Composable screens (auth, group, match, user, …)
│           ├── navigation/  # NavHost, route definitions
│           └── ui/          # Shared UI components & theme
├── shared/                  # KMP shared library (Android + iOS)
│   └── src/
│       ├── commonMain/      # Business logic, ViewModels, API clients, domain models
│       ├── androidMain/     # Android-specific implementations (Room DB, JWT, logging)
│       └── iosMain/         # iOS-specific implementations
├── iosApp/                  # iOS application (consumes Shared.xcframework)
├── docker-compose.yml       # Local backend infrastructure
├── config/detekt/           # Detekt static-analysis configuration
└── gradle/libs.versions.toml # Centralized dependency versions
```

---

## Getting Started

### Prerequisites

| Tool | Minimum Version |
|---|---|
| JDK | 17 |
| Android Studio | Hedgehog (2023.1.1) or newer |
| Kotlin Multiplatform plugin | latest |
| Docker & Docker Compose | any recent version |
| Xcode (iOS only) | 15+ |

### Backend (Docker)

The backend is provided as a pre-built Docker image. Start all required services with:

```bash
docker-compose up -d
```

This starts:

| Service | URL / Port |
|---|---|
| Backend API | `http://localhost:8085` |
| PostgreSQL | `localhost:5432` |
| MongoDB | `localhost:27017` |
| MailHog (web UI) | `http://localhost:8025` |
| MinIO (object storage) | `http://localhost:9001` |

Sample data is loaded automatically (`APP_LOAD_SAMPLE_DATA=true`).

### Android App

1. Clone the repository:
   ```bash
   git clone https://github.com/FSpruhs/kick-it-kmp.git
   cd kick-it-kmp
   ```

2. Open the project in **Android Studio**.

3. Ensure the backend is running (see above).

4. Select the `composeApp` run configuration and run on an emulator or physical device.

> **iOS:** Open `iosApp/iosApp.xcodeproj` in Xcode. Build the `shared` framework first with `./gradlew :shared:assembleXCFramework`, then run the iOS target.

---

## Build Commands

```bash
# Build everything
./gradlew build

# Build only the Android app
./gradlew :composeApp:build

# Build only the shared KMP library
./gradlew :shared:build

# Run Android unit tests
./gradlew :composeApp:testDebugUnitTest
./gradlew :shared:allTests

# Format code (Ktlint)
./gradlew ktlintFormat

# Check code style (Ktlint)
./gradlew ktlintCheck

# Run static analysis (Detekt)
./gradlew detekt
```

---

## Code Style

- **Ktlint** enforces formatting (Android Studio conventions via `.editorconfig`).
- **Detekt** enforces complexity, naming, and structural rules (`config/detekt/detekt.yml`).
- Always run `./gradlew ktlintFormat` before committing.
- Generated code under `build/` is excluded from all lint checks.

---

## Contributing

1. Fork the repository and create a feature branch (`git checkout -b feature/my-feature`).
2. Follow the [Architecture](#architecture) conventions when adding new features.
3. Run `./gradlew ktlintFormat detekt` to ensure code quality.
4. Open a pull request with a clear description of your changes.
