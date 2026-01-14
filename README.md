# 🎵 SilverMP3 - Android Music Streaming Application

**SilverMP3** is a modern music streaming application for Android. The app is built using a standard MVVM architecture, featuring a sleek UI and integrating the latest technologies from Google and the open-source community.

---

## 📺 Video Demo

> [Xem video Demo tại đây](https://drive.google.com/file/d/178amsAOGWTIa2ScNKdSoAn8MARMqWVSw/view)

---

## ✨ Key Features

- **High-Quality Playback:** Utilizes the latest **Media3 ExoPlayer** library to optimize music playback and data streaming.
- **Background Play & Notifications:** Integrated with **MediaSessionService**, allowing music to continue playing in the background with controls available in the notification shade.
- **Dynamic UI:** Automatically extracts colors from song artwork using the **Palette API** to create beautiful dynamic gradient backgrounds.
- **Multi-Method Authentication:** Supports login via **Email**, **Phone (OTP)**, and **Google Auth** through Firebase.
- **Playlist Management:** Users can create, edit, and add songs to personal playlists.
- **Favorites & Library:** Save favorite songs with real-time synchronization using **Cloud Firestore**.
- **Modern Architecture:** Uses **Koin** for Dependency Injection, ensuring clean, testable, and maintainable code.

---

## 🛠 Tech Stack

- **Language:** Kotlin
- **UI Framework:** XML (ViewBinding)
- **Media:** AndroidX Media3 (ExoPlayer, Session, UI).
- **Backend:** Firebase Authentication, Cloud Firestore.
- **Dependency Injection:** Koin (Android, Coroutines).
- **Image Loading:** Glide (v5.0.5).
- **Asynchronous Processing:** Coroutines & Flow.

---

## 🚀 Getting Started

### 1. Firebase Configuration
- Create a new project on the [Firebase Console](https://console.firebase.google.com/).
- Add an Android app with the Package Name: `com.cbtool.silvermp3`.
- Download the `google-services.json` file and place it in the `app/` directory.
- Enable **Authentication** (Email, Google, Phone) and **Cloud Firestore**.

### 2. System Requirements
- Android Studio Ladybug or newer.
- Minimum SDK: **API 24 (Android 7.0)**.
- Target/Compile SDK: **API 36**.

### 3. Build & Run
- Open the project in Android Studio.
- Wait for Gradle Sync to complete.
- Click **Run** to install on an emulator or physical device.

---

## 📂 Project Structure

- `com.cbtool.silvermp3.service`: Contains `PlayBackService` for core playback logic.
- `com.cbtool.silvermp3.ui`: Contains Fragments and ViewModels for all screens (Home, Player, Library, Search).
- `com.cbtool.silvermp3.data`: Manages Models (Song, Artist, Playlist) and Repositories for Firestore interaction.
- `com.cbtool.silvermp3.di`: Koin module configurations.
- `com.cbtool.silvermp3.utils`: Extension functions and helper utilities.

---

## 📄 License

This project is developed for educational purposes.

---

**SilverMP3** - *Unlimited music experience!* 🎧