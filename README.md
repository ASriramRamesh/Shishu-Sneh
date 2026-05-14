<<<<<<< HEAD
# Shishu-Sneh Android App

Native Android app built with **Kotlin + Jetpack Compose + Room DB**.

## Requirements
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 35

## Setup
1. Open Android Studio → **File → Open** → select this `shishu-sneh-android` folder
2. Wait for Gradle sync to complete (downloads dependencies automatically)
3. Connect a device or start an emulator (API 26+)
4. Press **Run ▶**

## Stack
| Layer | Technology |
|-------|-----------|
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Database | Room DB (SQLite) |
| DI | Hilt |
| Preferences | DataStore |
| Async | Kotlin Coroutines + Flow |

## Features
- **Onboarding** — 5-slide intro carousel
- **Baby Profiles** — add / switch / delete multiple profiles
- **Home Dashboard** — age, next vaccine countdown, growth stats, daily tip
- **Growth Tracker** — log weight / height / head circumference with history
- **Vaccination Schedule** — full Indian NIS from birth to 18 months
- **Developmental Milestones** — 26 milestones across 5 categories
- **AI Health Guide** — offline keyword-based Q&A (fever, feeding, sleep, vaccines…)
- **Feeding Tracker** — live breastfeed timer, bottle and solid logging
- **Sleep Tracker** — live sleep timer with daily total
- **Diaper Log** — one-tap logging with daily stats
- **Doctor Visits** — log visits with measurements and notes
- **Feeding & Nutrition** — Indian NIS-aligned feeding stages guide
- **Emergency Help** — emergency numbers (112, 108, 1098) + first-aid guides
- **Settings** — manage profiles, app info

## Database Schema (Room)
- `baby_profiles`
- `growth_records`
- `vaccine_statuses`
- `milestone_statuses`
- `feeding_records`
- `sleep_records`
- `diaper_records`
- `doctor_visits`

All data is stored locally — no internet required.
=======
# Shishu-Sneh
Shishu-Sneh (meaning "Love for the Child" in Hindi) is an Android mobile application designed as a digital companion for new mothers during the critical first year of a baby's life.
>>>>>>> ca6ea7b079d0a4f1a16548377961d27cb7e2390d
