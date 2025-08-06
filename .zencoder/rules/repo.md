---
description: Repository Information Overview
alwaysApply: true
---

# Smart-AutoClicker Information

## Summary
Smart-AutoClicker (Klick'r) is an Android application that provides automated clicking functionality through an accessibility service. It allows users to create and manage click scenarios with configurable actions, conditions, and timing. The app supports both "smart" mode with image detection and "dumb" mode for simple timed clicks.

## Structure
- **core/**: Core functionality modules
  - **common/**: Shared utilities and base components
  - **dumb/**: Implementation for simple timed clicking
  - **smart/**: Implementation for image detection-based clicking
- **feature/**: Feature modules
  - **backup/**: Backup and restore functionality
  - **dumb-config/**: Configuration UI for dumb mode
  - **smart-config/**: Configuration UI for smart mode
  - **notifications/**: Notification system
  - **quick-settings-tile/**: Quick settings integration
  - **revenue/**: Monetization features
  - **review/**: App review prompts
  - **tutorial/**: In-app tutorials
- **smartautoclicker/**: Main application module

## Language & Runtime
**Language**: Kotlin
**Version**: Uses Kotlin 2.1.0
**Build System**: Gradle (8.11.1)
**Package Manager**: Gradle/Maven

## Dependencies
**Main Dependencies**:
- AndroidX Core KTX (1.16.0)
- AndroidX AppCompat (1.7.0)
- AndroidX Room (2.7.1)
- AndroidX Lifecycle (2.8.7)
- AndroidX Navigation (2.9.0)
- Dagger Hilt (2.55)
- Kotlinx Coroutines (1.10.0)
- OpenCV (4.11.0)
- Material Components (1.12.0)
- Lottie (6.1.0)

**Development Dependencies**:
- JUnit (4.13.2)
- Mockito (5.6.0)
- Robolectric (4.11.1)
- Espresso (3.6.1)
- AndroidX Test Core (1.6.1)

## Build & Installation
```bash
# Build debug version
./gradlew assembleDebug

# Build release version
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

## Testing
**Frameworks**: JUnit, Mockito, Robolectric, Espresso
**Test Location**: Each module has its own test directory
**Run Command**:
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## Main Components
**Application**: `SmartAutoClickerApplication` - Main application class with Hilt integration
**Service**: `SmartAutoClickerService` - Accessibility service for performing clicks
**Activities**:
- `ScenarioActivity` - Main entry point for scenario management
- `SettingsActivity` - Application settings

## Features
- **Smart Mode**: Uses image detection to trigger actions
- **Dumb Mode**: Simple timed clicking without detection
- **Scenario Management**: Create, edit, and manage click scenarios
- **Action Types**: Clicks, swipes, pauses, and more
- **Monetization**: AdMob integration (PlayStore version only)
- **Quick Settings**: Tile for quick access to the service
- **Backup/Restore**: Export and import scenarios