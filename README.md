# AiCoN Fit

Free Android fitness app from AiCoN. Guided no-equipment workouts with
animated exercise demos that show exactly what to do. No ads, no accounts,
no tracking. All data stays on the phone.

## Features (MVP)

- 87 no-equipment exercises with animated demos and step-by-step instructions
- 4 guided plans: Starter Full Body, Core Foundation, Sweat Express, Bodyweight Plus
- Workout player with set, rep and rest timers, sounds and haptics
- Custom workout builder
- Goal quiz that curates a recommended plan and personal exercise picks
- Weekly activity chart, plan progress rings and workout celebrations
- Daily motivational quotes from real historical figures, each attributed
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
  The animated demos are open licensed: each loop is derived from that
  project's per-exercise photos and plays at natural exercise speed
  (250 ms per frame). No third-party or proprietary video is used.
- Comfortaa font: SIL Open Font License 1.1
  (https://github.com/google/fonts/tree/main/ofl/comfortaa).
- Motivational quotes: public-domain sayings plus short, famously
  attributed quotations, each shown with its real attribution.
  No invented attributions.
- AiCoN logo and brand: AiCoN. Do not alter the logo.
- App code: MIT.
