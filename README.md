# Music Player Design

A fresh Android music-player UI concept built with **Kotlin + Jetpack Compose**.

> This repository is a visual prototype. Music playback, media scanning, downloads, accounts, and streaming are intentionally not implemented yet.

## Design preview
- Premium dark-first music dashboard
- Home, Search, Library, Albums, Artists, Playlists and Now Playing screens
- Light / dark appearance switch
- Interactive navigation, search field, library filters and player controls (visual state only)
- Responsive Compose layouts and Material 3 components

## Run
1. Open this repository in the latest stable Android Studio.
2. Allow Gradle to sync and install the Android SDK requested by the project.
3. Run the `app` configuration on an emulator or Android device.

Package: `com.sonora.musicdesign`

## Project structure
- `app/src/main/java/com/sonora/musicdesign/MainActivity.kt` — app UI and navigation
- `app/src/main/java/com/sonora/musicdesign/ui/theme/Theme.kt` — color system and typography
- `app/src/main/AndroidManifest.xml` — Android app entry point

## Prototype scope
All displayed music, artists, albums and playlists are sample content. Player controls update the interface only; no audio is played.
