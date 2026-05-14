**Shishu-Sneh Android App**

**Shishu-Sneh** (meaning "Love for the Child" in Hindi) is a native Android application designed as a comprehensive digital companion for new mothers, supporting them through the critical first year of their baby’s life.

Built with modern Android development practices using **Kotlin**, **Jetpack Compose**, and **Room Database**.

### Requirements
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 35 (API Level 35)

### Setup Instructions
1. Open **Android Studio**.
2. Go to **File → Open** and select the `shishu-sneh-android` project folder.
3. Allow Gradle to sync the project and download all required dependencies.
4. Connect a physical Android device or launch an emulator (minimum API Level 26).
5. Click the **Run** button to build and install the application.

### Technology Stack

| Layer              | Technology                          |
|--------------------|-------------------------------------|
| UI                 | Jetpack Compose + Material 3        |
| Navigation         | Jetpack Navigation Compose          |
| Database           | Room Database (SQLite)              |
| Dependency Injection | Hilt                              |
| Local Storage      | Jetpack DataStore                   |
| Asynchronous Programming | Kotlin Coroutines + Flow       |

### Key Features

- **Onboarding**: Engaging 5-slide introduction carousel
- **Baby Profiles**: Create, switch between, and manage multiple baby profiles
- **Home Dashboard**: Overview of baby’s age, upcoming vaccinations, growth statistics, and daily parenting tips
- **Growth Tracker**: Record and monitor weight, height, and head circumference with historical charts
- **Vaccination Schedule**: Complete Indian National Immunization Schedule (NIS) from birth to 18 months
- **Developmental Milestones**: Track 26 key milestones across 5 developmental categories
- **AI Health Guide**: Offline, keyword-based intelligent assistant for common queries (fever, feeding, sleep, vaccines, etc.)
- **Feeding Tracker**: Breastfeeding timer, bottle feeding, and solid food logging
- **Sleep Tracker**: Real-time sleep timer with daily summary statistics
- **Diaper Log**: Quick one-tap logging with daily pattern insights
- **Doctor Visits**: Log medical consultations with measurements and notes
- **Feeding & Nutrition Guide**: Age-appropriate feeding recommendations aligned with Indian NIS
- **Emergency Assistance**: Quick access to Indian emergency numbers (112, 108, 1098) and basic first-aid guides
- **Settings**: Profile management and application preferences

### Database Schema (Room)

All data is stored locally on the device with no internet connection required. The app includes the following entities:

- `baby_profiles`
- `growth_records`
- `vaccine_statuses`
- `milestone_statuses`
- `feeding_records`
- `sleep_records`
- `diaper_records`
- `doctor_visits`

---

**Shishu-Sneh** empowers parents with reliable, accessible, and privacy-focused tools to confidently navigate early parenthood.
