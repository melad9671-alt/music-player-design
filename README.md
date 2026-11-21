# Music Player Design

A polished Android music-player interface prototype built with **Kotlin + Jetpack Compose**.

## Design direction
A premium dark-first music experience with rich violet accents, custom crowned music-note branding, smooth section navigation, and a matching light theme.

## Included screens
- Home dashboard
- Search screen with local sample results
- Library
- Albums
- Artists
- Playlists
- Full-screen Now Playing design
- Compact mini-player and five-tab bottom navigation
- Light / dark theme toggle

## Prototype boundaries
This project is intentionally a **visual design prototype**. Sample content and navigation are included so every section can be opened and reviewed. Audio playback, device music scanning, streaming, downloads, accounts, and other production features are not implemented. Player buttons are visual/demo controls only.

## Build
Open the project in Android Studio, allow Gradle sync, then run the `app` configuration on an emulator or Android device.

A GitHub Actions workflow is configured to build a debug APK and publish it as a workflow artifact after a successful run.

## App details
- Display name: **Music Player Design**
- Package: `com.sonora.musicdesign`
- Min SDK: 24
- Target SDK: 35
