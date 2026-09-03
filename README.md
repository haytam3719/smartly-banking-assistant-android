# Smartly Banking Assistant for Android

Android client for the Smartly Banking AI platform. The app provides a conversational interface for account questions and displays the backend source metadata attached to assistant responses.

## Architecture

The client is a single-screen Kotlin application built with Jetpack Compose and Material 3. It follows an MVVM-style flow:

```text
Compose chat UI
    -> ChatViewModel / StateFlow<ChatUiState>
    -> ChatRepository
    -> Retrofit BankingApi
    -> Smartly Mobile BFF
```

Hilt supplies the ViewModel, repository, Retrofit API, and OkHttp client. Gson-backed DTOs model the API contract, while domain models keep chat messages and response metadata independent of the transport layer. The current app has one destination, so it does not need a navigation graph.

## Requirements

- Android Studio with JDK 11 or newer
- Android SDK 36
- A running Smartly Mobile BFF for live chat responses

## Local backend setup

The debug build uses `http://10.0.2.2:8080/`. Android reserves `10.0.2.2` as the emulator alias for the development host's loopback interface:

```text
Android Emulator
    -> 10.0.2.2
    -> developer host machine
    -> Mobile BFF :8080
```

Start the Mobile BFF on port `8080`, then run the `debug` variant in an Android emulator. Cleartext HTTP is enabled only by the debug manifest. Physical devices require a reachable development-host address and a corresponding local build configuration.

The release placeholder is deliberately non-routable. Before deploying, supply the real backend endpoint through a secure environment-specific build configuration and use HTTPS. Never use `10.0.2.2`, cleartext HTTP, demo identities, or client-embedded credentials in a deployed environment.

## Build and test

On Windows:

```powershell
.\gradlew.bat test
.\gradlew.bat assembleDebug
```

## Example questions

- Quel est mon solde ?
- Affiche mes dernières transactions.
- Quel est mon plafond de carte ?
- Quel est le statut de mon virement TR4587 ?
- Quels sont les frais d'un virement international ?

## Security notes

Authentication in this demo uses fixed, non-secret development identifiers passed to the Mobile BFF. Production authentication must be implemented with a secure server-managed session or token flow. Local SDK paths, environment files, signing keys, build output, IDE state, and Gradle caches are excluded from version control.
