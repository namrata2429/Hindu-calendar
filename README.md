# 🕉️ Hindu Panchang Calendar — Android App

A beautiful, fully offline Hindu Calendar app for Android with support for **English**, **Hindi (हिन्दी)**, and **Odia (ଓଡ଼ିଆ)** languages.

---

## ✨ Features

- 📅 **Full year calendar grid** — month-by-month navigation
- 🎉 **60+ Hindu festivals** with descriptions in all 3 languages
- 🌙 **Tithi, Paksha, Nakshatra** data on every event
- 🔔 **Vrat / Upavas reminders** (Ekadashi, Chaturthi, Pradosh, Purnima, Amavasya)
- ⏰ **Puja timing** for major festivals (Midnight for Janmashtami, Sandhi Puja, etc.)
- 🌐 **3-language support** — chosen at first launch, changeable anytime
- 🎨 **Color-coded event categories** (festivals, vrat, ekadashi, purnima, etc.)
- 📱 **100% offline** — no internet needed, all data bundled in the app

---

## 🗓️ Events Included (2026)

| Category | Examples |
|----------|---------|
| Major Festivals | Diwali, Holi, Navratri, Janmashtami, Ganesh Chaturthi, Ram Navami, Rath Yatra |
| Ekadashi | All 26 Ekadashis of 2026 (incl. Adhik Maas) |
| Purnima | All 12 Purnimas |
| Amavasya | Major Amavasyas (Mauni, Kartik, etc.) |
| Vrat/Upavas | Sankashti Chaturthi, Pradosh, Masik Shivaratri, Maha Shivaratri |
| Sankranti | Makar Sankranti, all solar transitions |
| Special | Adhik Maas (May 17 – Jun 15), Pitru Paksha, Guru Purnima |

---

## 🏗️ Project Structure

```
HinduCalendar/
├── app/src/main/
│   ├── java/com/hindupanchang/calendar/
│   │   ├── activities/
│   │   │   ├── SplashActivity.kt
│   │   │   ├── LanguageSelectionActivity.kt
│   │   │   ├── MainActivity.kt
│   │   │   └── EventDetailActivity.kt
│   │   ├── adapters/
│   │   │   ├── CalendarDayAdapter.kt
│   │   │   └── EventListAdapter.kt
│   │   ├── models/
│   │   │   └── HinduEvent.kt
│   │   ├── data/
│   │   │   └── HinduCalendarData2026.kt   ← MAIN DATA FILE
│   │   └── utils/
│   │       ├── LanguageManager.kt
│   │       └── CalendarHelper.kt
│   └── res/
│       ├── layout/         (5 layouts)
│       ├── drawable/       (10 vector icons)
│       ├── values/         (colors, strings, themes, dimens)
│       ├── values-hi/      (Hindi strings)
│       └── values-or/      (Odia strings)
```

---

## 🔄 HOW TO UPDATE FOR A NEW YEAR (e.g. 2027)

**Every year you upload a new version. Here is the exact process:**

### Step 1 — Duplicate the data file
```
Copy: HinduCalendarData2026.kt
Rename to: HinduCalendarData2027.kt
```

### Step 2 — Update the year constants at the top
```kotlin
val YEAR = 2027
val VIKRAM_SAMVAT = "2084"
val SAKA_SAMVAT = "1949"
```

### Step 3 — Update ALL event dates
- Visit https://www.drikpanchang.com/calendars/hindu/hinducalendar.html
- Change the year to 2027
- Update each event's:
  - `dateGregorian` (e.g. "2027-01-14")
  - `dayOfMonth`
  - `month`
  - `year`
  - `tithiEn`, `tithiHi`, `tithiOr` (tithi may differ)
  - `timeInfo` if the puja timings change

### Step 4 — Update CalendarHelper.kt
In `CalendarHelper.kt`, change all references from `HinduCalendarData2026` → `HinduCalendarData2027`

### Step 5 — Update app version in build.gradle
```groovy
versionCode 2          // increment by 1
versionName "2027.1.0" // change to new year
```

### Step 6 — Update toolbar text
In `MainActivity.kt` and `strings.xml` — change "2026" → "2027" and Vikram Samvat accordingly.

### Step 7 — Build & Sign
```bash
./gradlew assembleRelease
# Sign with your keystore
# Upload to Google Play Console
```

---

## 🛠️ Build Instructions

### Requirements
- Android Studio Flamingo (or newer)
- Android SDK 34
- Kotlin 1.9+
- Java 8+

### Steps
1. Open `HinduCalendar/` in Android Studio
2. Wait for Gradle sync
3. Run on emulator or device (minSdk 24 = Android 7.0+)
4. For release: `Build → Generate Signed Bundle/APK`

---

## 📦 Google Play Checklist

- [ ] App icon (provided as adaptive icon)
- [ ] Screenshots (phone + 7" tablet)
- [ ] Short description (80 chars): "Hindu Panchang Calendar 2026 with festivals in English, Hindi & Odia"
- [ ] Full description: mention all festivals, 3 languages, offline
- [ ] Content rating: Everyone
- [ ] Category: Lifestyle / Religion & Spirituality
- [ ] Privacy policy (required) — use a free generator
- [ ] Version code: 1, Version name: 2026.1.0
- [ ] Target SDK: 34
- [ ] Min SDK: 24

---

## 🎨 Color Scheme

| Color | Usage |
|-------|-------|
| `#D4521C` (Saffron) | Primary, toolbar, today |
| `#F5C842` (Gold) | Accent, Om symbol |
| `#FFF8F0` (Cream) | Background |
| Festival-specific | Each category has its own color |

---

## 📱 Screens

1. **Splash** — Om symbol on saffron background (2s)
2. **Language Selection** — Choose English / Hindi / Odia
3. **Main Calendar** — Monthly grid with event dots + upcoming list
4. **Event Detail** — Full details with tithi, timing, description in selected language

---

*ॐ नमः शिवाय • ଜୟ ଜଗନ୍ନାଥ • जय श्री राम*
