# 📱 SnapNews - Android News Application

SnapNews is a modern and lightweight Android news application built using Kotlin and MVVM architecture. It fetches real-time news from [NewsAPI.org](https://newsapi.org/) and provides users with a smooth experience through intuitive navigation and user-friendly interface components.

---

## 🚀 Project Overview

SnapNews leverages Kotlin and modern Android libraries to deliver real-time breaking news, powerful search functionalities, detailed news views, and user settings. With Firebase Analytics and Crashlytics integration, the app provides insights into user interactions and robust crash reporting, enhancing overall app reliability.

The architecture follows **MVVM (Model-View-ViewModel)** principles, clearly separating business logic, UI components, and data management for maintainability and scalability.

---

## ✨ Key Features

- 📰 **Breaking News Headlines**
  - Headlines carousel using ViewPager2 for auto-scrolling top news.

- 🔍 **Live Search with Debounce**
  - Optimized instant search results using debounce to minimize API calls.

- 📄 **Article Detail View**
  - Detailed views with images, descriptions, authors, and formatted publish dates.

- 📅 **Publish Date Formatter**
  - User-friendly date formatting for articles.

- ⚙️ **Interactive Settings Screen**
  - Notification toggle with real-time Toast feedback.
  - Expandable, professionally-designed FAQ section.
  - Expandable "Contact Us" with integrated GitHub & Instagram links.

- 📊 **Firebase Analytics**
  - Tracks critical user actions such as searches, clicks, and page views.

- ⚠️ **Firebase Crashlytics**
  - Automatic tracking and detailed reporting of application crashes and errors.

- 🧩 **MVVM Architecture**
  - Clear separation of concerns, enhancing testability and maintainability.

- 🎨 **Material Design UI**
  - Modern interface based on Google's Material Design principles.

---

## 🛠️ Tech Stack & Libraries

- **Language**: Kotlin
- **Architecture**: MVVM
- **Networking**: Retrofit2
- **Image Loading**: Glide
- **UI Components**: ViewPager2, RecyclerView, Material Components
- **Analytics & Crash Reporting**: Firebase Analytics & Crashlytics
- **Navigation**: Jetpack Navigation Component

---

## 📸 Screenshots

### 🏠 Home Screen
_Displaying breaking news headlines & news list_

<img src="app/screenshoots/home_screen.png" alt="Home Screen" width="350"/>

---

### 📄 News Detail Screen
_Showing detailed news information clearly_

<img src="app/screenshoots/detail_screen.png" alt="Detail Screen" width="350"/>

---

### ⚙️ Settings Screen
_Interactive FAQ & Contact sections_

<img src="app/screenshoots/settings_screen.png" alt="Settings Screen" width="350"/>

---

## 🔧 Installation Steps

Follow these steps to set up SnapNews on your local machine:

**Step 1: Clone the Repository**

`git clone https://github.com/fatihparkin/SnapNews.git`

**Step 2: Open Project in Android Studio**

- Launch Android Studio.
- Select "Open an Existing Project".
- Navigate to the cloned SnapNews directory and open it.
  
**Step 3: Configure NewsAPI Key**
  
- Visit NewsAPI.org to obtain your API key.
- Navigate to:`app/src/main/java/com/yourpackagename/data/api/RetrofitClient.kt`
- Replace "YOUR_API_KEY" with your NewsAPI key.

 **Step 4: Sync & Run the App**
- Sync Gradle files (File > Sync Project with Gradle Files).
- Connect an emulator or physical Android device.
- Press Run ▶️ to launch SnapNews.
- 🎉 That's it! SnapNews is now ready!

🔗 Contact & Social Links
Feel free to reach out or follow me:

## 📩 Contact

- **GitHub:** [@fatihparkin](https://github.com/fatihparkin)  
- **Instagram:** [@fatihparkin](https://instagram.com/fatihparkin)
  
## 📄 License
- This project is built for educational and portfolio purposes only.
- No commercial license is applied to this repository.
  

