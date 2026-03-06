# Copilot Instructions for KickItKMP

## Project Overview

**KickItKMP** is a Kotlin Multiplatform (KMP) mobile application for managing soccer/football matches. It allows players to organize into groups, plan matches, record results, track attendance, and view player statistics. Authentication is JWT-based with a Spring Boot backend.

**Two modules:**
- `:shared` — Kotlin Multiplatform library containing all business logic, data layer, and ViewModels (targets: Android, iOS arm64/x64/simulator)
- `:composeApp` — Android application with Jetpack Compose UI, navigation, and Android-specific screen implementations

**iOS** is supported via the `shared` KMP framework (`Shared.xcframework`) consumed by `iosApp/`.

---

## Technology Stack

| Layer | Technology | Version |
|---|---|---|
| Language | Kotlin Multiplatform | 2.2.10 |
| UI | Jetpack Compose / Compose Multiplatform | 1.8.2 |
| DI | Koin | 4.1.1 |
| HTTP | Ktor + Ktorfit | 3.4.0 / 2.7.2 |
| Local DB | Room (SQLite) | 2.8.4 |
| Async | Kotlin Coroutines | 1.10.2 |
| Serialization | kotlinx-serialization | 1.10.0 |
| Navigation | Navigation Compose | 2.9.3 |
| Image Loading | Coil | 2.7.0 |
| JWT | JJWT (Android only) | 0.13.0 |
| Linting | Ktlint + Detekt | 14.0.1 / 1.23.8 |
| Code Gen | KSP | bundled |
| Build | Gradle Kotlin DSL | — |

All library versions are centralized in `gradle/libs.versions.toml`.

---

## Build Commands

```bash
# Build everything
./gradlew build

# Build only the Android app
./gradlew :composeApp:build

# Build only the shared library
./gradlew :shared:build

# Run Android unit tests
./gradlew :composeApp:testDebugUnitTest
./gradlew :shared:testDebugUnitTest

# Code formatting check (Ktlint)
./gradlew ktlintCheck

# Auto-format code (Ktlint)
./gradlew ktlintFormat

# Static analysis (Detekt)
./gradlew detekt
```

**Important:** Always run `./gradlew ktlintFormat` before committing. Ktlint and Detekt are applied automatically to all subprojects. Generated code in `build/` directories is excluded.

---

## Backend / Local Development

The backend is a Spring Boot service. Run it locally with Docker Compose:

```bash
docker-compose up -d
```

Services started:
- **Backend API** at `http://localhost:8085` (`fspruhs/kick-app-kotlin-backend` image)
- **PostgreSQL** at `localhost:5432` (user: `admin`, password: `password123`, db: `kick_app`)
- **MongoDB** at `localhost:27017`
- **MailHog** (SMTP: 1025, Web UI: 8025)
- **MinIO** (S3-compatible object storage: 9000, console: 9001)

Credentials are in `.env` (development only — do not commit secrets).

---

## Architecture: Clean Architecture + MVI

### Layer Structure (per feature in `:shared`)

```
com.spruhs.{feature}/
├── application/         # Domain layer: use cases, repository interfaces, domain models
├── data/                # Data layer: repository implementations, API clients, Room DAOs
├── presentation/        # Presentation layer: ViewModels, UIState, Intent, Effect
└── di/                  # Koin module for this feature
```

### MVI Pattern

Every feature follows the **Model-View-Intent (MVI)** pattern:

1. **Intent** — sealed class representing user actions (e.g., `HomeIntent.SelectMatch`)
2. **UIState** — data class implementing `BaseUIState<S>` with `isLoading`, `error`, and feature-specific fields
3. **Effect** — sealed class for one-off side effects (e.g., navigation, toast messages)
4. **ViewModel** — extends `BaseViewModel<I, E, S>`, exposes `uiState: StateFlow<S>` and `effects: SharedFlow<E>`

### Base Classes

**`BaseViewModel<I, E, S>`** (`shared/src/commonMain/kotlin/com/spruhs/BaseViewModel.kt`):
- `processIntent(intent: I)` — override to handle intents
- `performAction(onSuccess, onError, action)` — wraps a coroutine with automatic `isLoading`/`error` state management
- Uses `viewModelScope` (from `androidx.lifecycle:lifecycle-viewmodel`)

**`BaseUIState<S>`** (`shared/src/commonMain/kotlin/com/spruhs/BaseUIState.kt`):
- Interface requiring `isLoading: Boolean`, `error: String?`, and `copyWith(isLoading, error): S`

### Example ViewModel Pattern

```kotlin
class MyViewModel(
    private val myUseCase: MyUseCase
) : BaseViewModel<MyIntent, MyEffect, MyUIState>(MyUIState()) {

    init { loadData() }

    private fun loadData() {
        performAction(
            action = { myUseCase.getData() },
            onSuccess = { result -> uiStateMutable.update { it.copy(data = result) } }
        )
    }

    override fun processIntent(intent: MyIntent) {
        when (intent) {
            is MyIntent.DoSomething -> viewModelScope.launch {
                effectsMutable.emit(MyEffect.Navigate(intent.id))
            }
        }
    }
}

data class MyUIState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val data: List<Item> = emptyList()
) : BaseUIState<MyUIState> {
    override fun copyWith(isLoading: Boolean, error: String?) = copy(isLoading = isLoading, error = error)
}

sealed class MyIntent {
    data class DoSomething(val id: String) : MyIntent()
}

sealed class MyEffect {
    data class Navigate(val id: String) : MyEffect()
}
```

---

## Adding a New Feature

Follow these steps to add a complete feature (example: `notifications`):

### 1. Domain Layer (`shared/src/commonMain/kotlin/com/spruhs/notifications/application/`)

- Create domain model (plain data class, no Android/framework dependencies)
- Create repository interface (suspend functions, return `Flow<T>` where reactive)
- Create use case classes (one class per use case)

### 2. Data Layer (`shared/src/commonMain/kotlin/com/spruhs/notifications/data/`)

- Implement the repository interface
- Create Ktorfit service interface (annotated with `@GET`, `@POST`, etc.)
- Create API implementation class

### 3. Presentation Layer (`shared/src/commonMain/kotlin/com/spruhs/notifications/presentation/`)

- Create ViewModel, UIState, Intent, Effect (as shown above)
- Register ViewModel in Koin module using `viewModelOf(::MyViewModel)`

### 4. DI Module (`shared/src/commonMain/kotlin/com/spruhs/notifications/di/NotificationsModule.kt`)

```kotlin
val notificationsModule = module {
    single { MyUseCase(get()) }
    single<MyRepository> { MyRepositoryImpl() }
    viewModelOf(::MyViewModel)
}
```

### 5. Register Module in `KickItApplication.kt` (`composeApp/src/androidMain/kotlin/com/spruhs/KickItApplication.kt`)

Add `notificationsModule` to the `modules` list in `startKoin { }`.

### 6. UI Screen (`composeApp/src/androidMain/kotlin/com/spruhs/screens/notifications/`)

```kotlin
@Composable
fun NotificationsScreen(myViewModel: MyViewModel = koinViewModel()) {
    val uiState by myViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        myViewModel.effects.collect { effect -> /* handle navigation */ }
    }
    // Render UI based on uiState
}
```

### 7. Navigation

- Add route to `MainScreens.kt` or `StartScreens.kt`
- Add `composable(...)` entry in `AppMainNavigation.kt` or `AppStartNavigation.kt`
- Add bottom nav item to `BottomNavigationBar.kt` if needed

---

## HTTP Client (Ktorfit)

API interfaces use Ktorfit annotations (similar to Retrofit):

```kotlin
interface MyService {
    @GET("v1/my-resource")
    suspend fun getItems(): List<ItemResponse>

    @POST("v1/my-resource")
    suspend fun createItem(@Body request: CreateItemRequest): ItemResponse
}

// In DI module:
single<MyService> { get<Ktorfit>().createMyService() }
```

Two Ktorfit clients are available (injected by Koin):
- `Ktorfit` (named: default) — authenticated client (attaches JWT bearer token automatically)
- `Ktorfit` (named: `"NoAuthKtorfit"`) — unauthenticated client (used only for auth endpoints)

---

## Local Database (Room)

Room is used for token storage. The pattern for adding new entities:

1. Create `@Entity` data class in `data/`
2. Create `@Dao` interface
3. Create `@Database` class
4. Add platform-specific builder in `shared/src/androidMain/` and `shared/src/iosMain/`
5. Expose via `platformModule`

---

## Expect/Actual Pattern (KMP)

For platform-specific code, use `expect`/`actual`:

- Declare `expect` in `commonMain`
- Provide `actual` in `androidMain` and `iosMain`

Existing examples:
- `AppLogger` — logging via `android.util.Log` on Android
- `TokenHelper` — JWT parsing via JJWT on Android
- `platformModule` — Koin module providing platform-specific singletons (e.g., Room database)

```kotlin
// commonMain
expect object AppLogger {
    fun e(tag: String, message: String, throwable: Throwable? = null)
}

// androidMain
actual object AppLogger {
    actual fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}
```

---

## Dependency Injection (Koin)

- Each feature has its own Koin `module` in its `di/` package
- Singletons use `single { ... }` or `single<Interface> { Impl(...) }`
- ViewModels use `viewModelOf(::ViewModel)` (constructor injection)
- All modules are registered in `KickItApplication.onCreate()`
- Platform-specific bindings go in `platformModule` (actual implementation per platform)

---

## Common Utilities

- `dateTimeNow()` — returns current `LocalDateTime` using `kotlinx-datetime`
- `validateEmail(email: String): Boolean` — basic email format check
- `AppLogger.e/d/i(tag, message, throwable?)` — cross-platform logging

---

## Code Style & Linting

- **Ktlint** enforces code style (Android Studio conventions via `.editorconfig`)
- **Detekt** enforces complexity, naming, and structural rules (`config/detekt/detekt.yml`)
- `@Composable` function naming is exempt from standard function naming rules (lowercase start)
- Generated code (`build/` directories) is excluded from all lint checks
- Run `./gradlew ktlintFormat` to auto-fix formatting issues before committing

---

## Testing

- Tests live in `shared/src/commonTest/` (KMP shared tests) and `composeApp/src/androidUnitTest/`
- Test framework: `kotlin-test`
- Run: `./gradlew :shared:allTests` or `./gradlew :composeApp:testDebugUnitTest`

---

## Project-Specific Notes

- **No README.md** exists at the repo root — documentation lives here and in code
- **No CI/CD workflows** are configured in `.github/workflows/` as of initial setup
- The backend image `fspruhs/kick-app-kotlin-backend` loads sample data when `APP_LOAD_SAMPLE_DATA=true`
- The JWT secret key `"my-256-bit-secret-my-256-bit-secret"` in `TokenHelper.android.kt` is a development placeholder only
- `settings.gradle.kts` includes exactly two modules: `:composeApp` and `:shared`
- Gradle configuration cache and build cache are both enabled (see `gradle.properties`)
- iOS framework is named `Shared` — referenced in `iosApp.xcodeproj`
