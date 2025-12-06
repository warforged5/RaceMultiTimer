# 🏁 Race Timer

A modern, feature-rich race timer application built with Kotlin Multiplatform and Compose Multiplatform. Features **Material 3 Expressive** design on Android and **Liquid Glass** aesthetics on iOS.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?logo=kotlin)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.0-blue)
![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-green)

## ✨ Features

### Timer Functionality
- **Multiple Lanes**: Add unlimited racing lanes, each with independent timers
- **Individual & Group Controls**: Start/stop/lap individual lanes or all at once
- **Lap Times**: Record split times with automatic lap duration calculation
- **Statistics**: Average, best, and worst lap times per lane
- **Countdown Start**: Configurable 3-2-1-GO countdown before race start
- **Rest Timer**: Separate countdown timer for rest intervals

### Organization
- **Lane Customization**: Rename lanes and assign custom colors
- **Drag to Reorder**: Hold and drag lane cards to rearrange order
- **Presets**: Save and load lane configurations for quick setup
- **Share Results**: Export formatted race results with rankings and stats

### Design
- **Material 3 Expressive (Android)**: Bold colors, large rounded buttons, spring animations, emotional design patterns
- **Liquid Glass (iOS)**: Translucent surfaces, glass-like effects, soft curves, refined aesthetics
- **Theme Support**: System, Light, Dark, and Dynamic (Material You) themes
- **Haptic Feedback**: Tactile responses on interactions

## 📱 Screenshots

| Android (M3 Expressive) | iOS (Liquid Glass) |
|:-----------------------:|:------------------:|
| Bold, colorful UI with large buttons | Translucent, glass-like surfaces |
| Spring animations on interactions | Smooth, fluid animations |
| Extra-rounded corners (28dp) | Softer curves (20dp) |

## 🏗️ Architecture

```
race-timer-app/
├── composeApp/
│   └── src/
│       ├── commonMain/          # Shared Kotlin code
│       │   └── kotlin/com/racetimer/app/
│       │       ├── domain/      # Models & business logic
│       │       ├── viewmodel/   # State management
│       │       └── ui/
│       │           ├── components/  # Reusable UI components
│       │           ├── screens/     # Full screens
│       │           └── theme/       # Theming system
│       ├── androidMain/         # Android-specific code
│       └── iosMain/             # iOS-specific code
└── iosApp/                      # iOS Xcode project
```

### Key Components

| Component | Description |
|-----------|-------------|
| `RaceTimerViewModel` | Centralized state management with MVI pattern |
| `RaceTimerScreen` | Main screen with lane list and controls |
| `ExpandedLaneScreen` | Full-screen detail view for individual lanes |
| `LaneCard` | Card component displaying lane timer |
| `BottomControlBar` | 4 action buttons (Add, Start, Stop, Lap) |
| `RaceTimerTheme` | Platform-aware theming system |

## 🎨 Design System

### Material 3 Expressive (Android)
Based on Google's M3 Expressive guidelines revealed at I/O 2025:
- **Bold Use of Shape**: Extra-large corner radii (28dp+)
- **Expressive Colors**: High-contrast, emotionally engaging palettes
- **Spring Animations**: Bouncy, responsive motion physics
- **Containers**: Clear visual grouping with distinct backgrounds
- **Large Touch Targets**: 64dp+ buttons for improved usability

### Liquid Glass (iOS)
Based on Apple's iOS 26 design language:
- **Translucent Surfaces**: Glass-like materials with blur effects
- **Optical Properties**: Refraction and reflection simulations
- **Fluid Morphing**: Elements that transform based on context
- **Subtle Depth**: Layered interfaces with gentle shadows
- **San Francisco Font**: Native iOS typography

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Xcode 15+ (for iOS)
- JDK 17+
- Kotlin 2.0.21+

### Build Android
```bash
./gradlew :composeApp:assembleDebug
```

### Build iOS
```bash
./gradlew :composeApp:iosSimulatorArm64MainBinaries
open iosApp/iosApp.xcodeproj
```

## 🎮 Usage

### Basic Workflow
1. **Add Lanes**: Tap "+ Add" to create racing lanes
2. **Customize**: Long-press lane cards to expand and rename/recolor
3. **Start Race**: Use countdown or direct start
4. **Record Laps**: Tap lane cards or "Lap All" button
5. **View Results**: Share formatted results via the menu

### Controls

| Button | Function |
|--------|----------|
| ➕ Add | Add a new lane |
| ▶️ Start | Start all stopped timers |
| ⏹️ Stop | Pause all running timers |
| 🏳️ Lap | Record lap for all lanes |

### Menu Options
- **Reset All Lanes**: Clear times but keep lane setup
- **Reset Everything**: Return to initial state
- **Start Countdown**: 3-2-1-GO race start
- **Start Rest Timer**: Configurable rest interval
- **Save/Load Preset**: Quick lane configurations
- **Share Results**: Export race summary

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin Multiplatform | Shared business logic |
| Compose Multiplatform | Cross-platform UI |
| Material 3 | Design system |
| Kotlinx Coroutines | Async operations |
| Kotlinx Serialization | Data persistence |
| Kotlinx DateTime | Time operations |
| Lifecycle ViewModel | State management |

## 📋 Requirements

### Android
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 35 (Android 15)

### iOS
- Minimum: iOS 15.0
- Recommended: iOS 17.0+

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Material Design 3](https://m3.material.io/) - Google's design system
- [Apple Human Interface Guidelines](https://developer.apple.com/design/human-interface-guidelines/) - iOS design principles
- [JetBrains](https://www.jetbrains.com/kotlin-multiplatform/) - Kotlin Multiplatform
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) - UI framework

---

Built with ❤️ using Kotlin Multiplatform