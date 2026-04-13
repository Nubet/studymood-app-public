# Studymood

Studymood is a small app that supports day-to-day wellbeing and studying: quick mood check-ins, study sessions, and quick self-regulation tools (e.g. breathing, reframing).
The project is built with Kotlin Multiplatform (KMP) and Compose Multiplatform, so most UI and business logic is shared across IOS and Android.

## What you get

- Mood check-ins 
- Study mode 
- Mind tools (breathing, quotes, grounding-style exercises)
- Local persistence (SQLite via SQLDelight)

## Tech stack

- Kotlin Multiplatform
- Compose Multiplatform + Material 3
- SQLDelight (local storage)
- Koin (DI)

## Repository layout

- `composeApp/` - main KMP module (shared UI + logic)
- `composeApp/src/commonMain` - shared code (ui/domain/data)
- `composeApp/src/androidMain` - Android integration
- `composeApp/src/iosMain` - iOS integration
- `iosApp/` - Xcode project


Requirements:
- JDK 17
- Android Studio (Android) / Xcode (iOS)

Note (Windows + SQLDelight): the migration verification task possibly may fail due to file locking. If that happens:

```bash
gradlew.bat build -x verifyCommonMainStudyMoodDatabaseMigration
```


## App preview & Key Benefits

A quick visual look at the app and its key features.

| Study Mood | 10 Second Check-in |
|:---:|:---:|
| <img src="images/1.jpg" width="300" alt="Study Mood app home screen"/> | <img src="images/2.jpg" width="300" alt="10 Second Check-in mood tracker"/> |
| **Study Sessions Tracked** | **Turn Patterns Into a Plan** |
| <img src="images/3.jpg" width="300" alt="Study Sessions Tracked screen"/> | <img src="images/4.jpg" width="300" alt="Mood and study pattern analysis"/> |
| **Reset When Stress Hits** | **Private By Design** |
| <img src="images/5.jpg" width="300" alt="Guided stress relief exercises"/> | <img src="images/6.jpg" width="300" alt="Privacy features: on-device storage, no accounts, offline"/> |