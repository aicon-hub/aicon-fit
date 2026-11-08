# AiCoN Fit

Free Android fitness app from AiCoN. Guided workouts with animated exercise
demos that show exactly what to do. No ads, no accounts, no tracking. All
data stays on the phone.

## Features (MVP)

- 49 exercises with animated demos and step-by-step instructions
- 4 guided plans: Starter Full Body, Core Foundation, Sweat Express, Dumbbell Basics
- Workout player with set, rep and rest timers, sounds and haptics
- Custom workout builder
- Onboarding, metric and imperial units, basic history and streaks
- Accessibility: reduce motion, high contrast, content descriptions
- Dark, orange AiCoN brand theme, Comfortaa headings

## Build

Requires JDK 17 and the Android SDK (platform 34, build-tools 34.0.0).

    ./gradlew assembleDebug

Unit tests:

    ./gradlew testDebugUnitTest

## Licenses

- Exercise data and demo images: Free Exercise DB
  (https://github.com/yuhonas/free-exercise-db), public domain (Unlicense).
- Comfortaa font: SIL Open Font License 1.1
  (https://github.com/google/fonts/tree/main/ofl/comfortaa).
- AiCoN logo and brand: AiCoN. Do not alter the logo.
- App code: MIT.
