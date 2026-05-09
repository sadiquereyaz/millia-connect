# MiliaConnect

📡 **MiliaConnect** simplifies campus digital life by automating WiFi login, tracking entrance results, and organizing notices in one clean app.

<img width="150" height="150" alt="logo_png" src="https://github.com/user-attachments/assets/be5b3d76-6ae0-4785-8da1-e6c445630ce9" />

[![Kotlin Version](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-brightgreen)](https://developer.android.com/jetpack/compose)

---

## ✨ Features

**Auto WiFi Login**
No more repeated captive portal logins on Jamia campus WiFi.

**Result Tracking**
Be notified instantly when JMI-Entrance results are released.

**Organized Notices**
All official notices from different JMI-Websites are in one place, clearly sorted and easy to read.

**Offline Support**
Access past notices and results even when offline.

**Clean, distraction-free interface**
Built with **Jetpack Compose** and **Modern Android Architecture**.


## Challenge:
Newer Android versions restrict apps from accessing WiFi SSID without sensitive permissions (`ACCESS_FINE_LOCATION` or `ACCESS_WIFI_STATE`), which would compromise user privacy.

**My Approach:**  
Discovered that campus WiFi exposes special URLs that:
1. Return `HTTP 200` only when connected to campus network
2. Are inaccessible/return errors on other networks

**Implementation:**  
```kotlin
suspend fun isJamiaWifi(): Boolean {
    return try {
        val response = client.get(JMI_URL)
        response.statusCode == HttpStatusCode.OK
    } catch (e: Exception) {
        false
    }
}
```

**Benefits:**
- 🛡️ No sensitive permissions required
- ⚡ Reliable campus network detection

## 📱 Screenshots

| Auto WiFi Login | Result Notifications | Notice Board |
|-----------------|----------------------|--------------|
| ![WiFi](https://github.com/user-attachments/assets/a2d63696-1d46-43a7-9f45-5a71b1f147d9) | ![Result](https://github.com/user-attachments/assets/5dd79fb2-73c2-417f-9c5b-85a0778724ce) | ![Notices](https://github.com/user-attachments/assets/a5ee29fa-f811-4ad7-9808-f176f31281f1) |

## Project Structure
```
milia-connect/
├── app/                  # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── di/       # App-level DI
│   │   │   ├── navigation/ # Navigation graphs
│   │   │   └── ui/       # App-level UI components
│
├── core/                 # Shared modules
│   ├── auth/             # Authentication system
│   │   ├── data/         # Repositories, data sources (Firebase/Auth APIs)
│   │   └── domain/       # Auth models, use cases
│   │
│   ├── common/           # Utilities, extensions, constants
│   ├── data/             # Base data models
│   ├── network/          # Retrofit, API clients
│   ├── notification/     # Notification system
│   └── ui/               # Shared UI components
│
├── feature/              # Feature modules
│   ├── attendance/       # Attendance tracking
│   │   ├── data/        # Local DB (Room), models
│   │   ├── domain/      # Business logic
│   │   └── presentation/ # UI components
│   │
│   ├── notice/           # Notice board
│   │   ├── data/        # Scrapers, repositories
│   │   ├── domain/      # Domain models
│   │   └── presentation/ # Notice screens
│   │
│   ├── portal/           # WiFi portal automation
│   │   ├── data/        # Credential storage, workers
│   │   ├── domain/      # Login logic
│   │   └── presentation/ # Connection UI
│   │
│   ├── result/           # Entrance results
│   │   ├── data/        # Scrapers, DB
│   │   ├── domain/      # Result models
│   │   └── presentation/ # Result screens
│   │
│   └── rent/             # Rent feature
│       ├── data/        # Room entities, repositories
│       ├── domain/      # Use cases, domain models
│       └── presentation/ # Rent feature UI screens
│
├── gradle/              # Version catalog
└── build-logic/         # Custom Gradle conventions

│
├── gradle/              # Version catalog
└── build-logic/         # Custom Gradle conventions
```
**Key Modularization Benefits:**
- Faster build times (parallel compilation)
- Clear feature boundaries
- Reusable components
- Independent testing
- Scalable architecture

## 🚀 Tech Stack

* **100% Kotlin** with Coroutines & Flow
* **Jetpack Compose** (UI Toolkit)
* **Feature-wise Modularization** (Clean Architecture)
* **Koin** (Dependency Injection)
* **HTMLUnit** (Web Scraping)
* **Room** (Local Database)
* **WorkManager** (Background tasks)
* **Retrofit** (Network operations)
* **Material 3** (Design system)
* **firebase storage**
* **google signin with credential manager**

## 📦 Play Store

[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=com.reyaz.milliaconnect1)

## 🏗️ Detailed Project Structure

```
millia-connect/
    ├── build.gradle.kts
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    ├── settings.gradle.kts
    ├── app/
    │   ├── build.gradle.kts
    │   └── src/
    │       ├── androidTest/
    │       │   └── java/
    │       │       └── com/
    │       │           └── reyaz/
    │       │               └── milliaconnect1/
    │       │                   └── ExampleInstrumentedTest.kt
    │       ├── main/
    │       │   └── java/
    │       │       └── com/
    │       │           └── reyaz/
    │       │               └── milliaconnect1/
    │       │                   ├── BaseApplication.kt
    │       │                   ├── MainActivity.kt
    │       │                   ├── MilliaConnectApp.kt
    │       │                   ├── di/
    │       │                   │   └── AppModule.kt
    │       │                   ├── navigation/
    │       │                   │   ├── AppDestinations.kt
    │       │                   │   ├── AppTopLevelDestination.kt
    │       │                   │   ├── MCNavHost.kt
    │       │                   │   └── graph/
    │       │                   │       ├── AttendanceNavGraph.kt
    │       │                   │       └── ResultNavGraph.kt
    │       │                   ├── ui/
    │       │                   │   └── components/
    │       │                   │       └── WifiIconComposable.kt
    │       │                   └── util/
    │       │                       └── NetworkConnectivityObserver.kt
    │       └── test/
    │           └── java/
    │               └── com/
    │                   └── reyaz/
    │                       └── milliaconnect1/
    │                           └── ExampleUnitTest.kt
    ├── core/
    │   ├── common/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           └── java/
    │   │               ├── com/
    │   │               │   └── reyaz/
    │   │               │       └── core/
    │   │               │           └── common/
    │   │               │               └── utils/
    │   │               │                   ├── DateUtils.kt
    │   │               │                   ├── Extensions.kt
    │   │               │                   ├── IntentActions.kt
    │   │               │                   ├── NetworkManager.kt
    │   │               │                   ├── Resource.kt
    │   │               │                   └── SafeCall.kt
    │   │               └── constants/
    │   │                   ├── AppConstants.kt
    │   │                   └── NavigationRoute.kt
    │   ├── data/
    │   │   ├── build.gradle.kts
    │   │   ├── consumer-rules.pro
    │   │   ├── proguard-rules.pro
    │   │   └── src/
    │   │       ├── androidTest/
    │   │       │   └── java/
    │   │       │       └── com/
    │   │       │           └── reyaz/
    │   │       │               └── core/
    │   │       │                   └── data/
    │   │       │                       └── ExampleInstrumentedTest.kt
    │   │       ├── main/
    │   │       │   └── java/
    │   │       │       └── com/
    │   │       │           └── reyaz/
    │   │       │               └── core/
    │   │       │                   └── data/
    │   │       │                       └── models/
    │   │       │                           └── BaseResponse.kt
    │   │       └── test/
    │   │           └── java/
    │   │               └── com/
    │   │                   └── reyaz/
    │   │                       └── core/
    │   │                           └── data/
    │   │                               └── ExampleUnitTest.kt
    │   ├── domain/
    │   │   └── build.gradle.kts
    │   ├── navigation/
    │   │   ├── build.gradle.kts
    │   │   ├── consumer-rules.pro
    │   │   ├── proguard-rules.pro
    │   │   └── src/
    │   │       ├── androidTest/
    │   │       │   └── java/
    │   │       │       └── com/
    │   │       │           └── reyaz/
    │   │       │               └── core/
    │   │       │                   └── navigation/
    │   │       │                       └── ExampleInstrumentedTest.kt
    │   │       ├── main/
    │   │       │   └── java/
    │   │       └── test/
    │   ├── network/
    │   │   ├── build.gradle.kts
    │   │   ├── consumer-rules.pro
    │   │   ├── proguard-rules.pro
    │   │   └── src/
    │   │       ├── androidTest/
    │   │       ├── main/
    │   │       │   └── java/
    │   │       │       └── com/
    │   │       │           └── reyaz/
    │   │       │               └── core/
    │   │       │                   └── network/
    │   │       │                       ├── NetworkClient.kt
    │   │       │                       ├── NetworkModule.kt
    │   │       │                       ├── PdfManager.kt
    │   │       │                       ├── RetrofitBuilder.kt
    │   │       │                       ├── interceptor/
    │   │       │                       │   └── AuthInterceptor.kt
    │   │       │                       ├── model/
    │   │       │                       │   └── DownloadResult.kt
    │   │       │                       └── utils/
    │   │       │                           ├── NoOpJavaScriptErrorListener.kt
    │   │       │                           ├── RequestTimeStore.kt
    │   │       │                           └── SSLTrustUtils.kt
    │   │       └── test/
    │   │                               └── ExampleUnitTest.kt
    │   ├── notification/
    │   │   ├── build.gradle.kts
    │   │   ├── consumer-rules.pro
    │   │   ├── proguard-rules.pro
    │   │   └── src/
    │   │       ├── main/
    │   │       │   └── java/
    │   │       │       └── com/
    │   │       │           └── reyaz/
    │   │       │               └── core/
    │   │       │                   └── notification/
    │   │       │                       ├── NotificationModule.kt
    │   │       │                       ├── manager/
    │   │       │                       │   └── AppNotificationManager.kt
    │   │       │                       ├── model/
    │   │       │                       │   └── NotificationData.kt
    │   │       │                       └── utils/
    │   │       │                           ├── CreateNotificationChannel.kt
    │   │       │                           └── NotificationConstant.kt
    │   │       └── test/
    │   └── ui/
    │       ├── build.gradle.kts
    │       ├── consumer-rules.pro
    │       ├── proguard-rules.pro
    │       └── src/
    │           ├── androidTest/
    │           ├── main/
    │           │   └── java/
    │           │       └── com/
    │           │           └── reyaz/
    │           │               └── core/
    │           │                   └── ui/
    │           │                       ├── components/
    │           │                       │   ├── CustomBottomNavigationBar.kt
    │           │                       │   ├── CustomListDivider.kt
    │           │                       │   ├── CustomTopAppBar.kt
    │           │                       │   ├── ErrorScreen.kt
    │           │                       │   ├── ListItemWithTrailingIcon.kt
    │           │                       │   ├── NavigationDrawerContent.kt
    │           │                       │   ├── textWithIndicator.kt
    │           │                       │   └── TranslucentLoader.kt
    │           │                       ├── helper/
    │           │                       │   ├── BrowserLinkOpener.kt
    │           │                       │   └── getNoticeActionModel.kt
    │           │                       ├── model/
    │           │                       │   └── ListItemUiModel.kt
    │           │                       ├── preview/
    │           │                       │   └── ThemePreview.kt
    │           │                       ├── screen/
    │           │                       │   └── PdfViewerScreen.kt
    │           │                       └── theme/
    │           │                           ├── Color.kt
    │           │                           ├── Theme.kt
    │           │                           └── Type.kt
    │           └── test/
    ├── extraFiles/
    │   ├── internetConnectedVector.zip
    │   └── structureTree.md
    ├── feature/
    │   ├── attendance/
    │   │   ├── build.gradle.kts
    │   │   ├── consumer-rules.pro
    │   │   ├── proguard-rules.pro
    │   │   └── src/
    │   │       ├── androidTest/
    │   │       ├── main/
    │   │       │   └── java/
    │   │       │       └── com/
    │   │       │           └── reyaz/
    │   │       │               └── feature/
    │   │       │                   └── attendance/
    │   │       │                       ├── add_schedule/
    │   │       │                       │   ├── domain/
    │   │       │                       │   │   └── AddAttendanceRepository.kt
    │   │       │                       │   └── presentation/
    │   │       │                       │       ├── AddAttendanceScreen.kt
    │   │       │                       │       └── AddAttendanceViewModel.kt
    │   │       │                       └── schedule/
    │   │       │                           ├── data/
    │   │       │                           │   └── dao/
    │   │       │                           │       ├── ScheduleModel.kt
    │   │       │                           │       ├── SubjectModel.kt
    │   │       │                           │       ├── TaskAttendanceModel.kt
    │   │       │                           │       └── utils/
    │   │       │                           │           ├── AttendanceTypeConverter.kt
    │   │       │                           │           └── TimeConverters.kt
    │   │       │                           ├── di/
    │   │       │                           │   └── ScheduleModule.kt
    │   │       │                           ├── domain/
    │   │       │                           │   ├── AttendanceType.kt
    │   │       │                           │   ├── ReminderModal.kt
    │   │       │                           │   └── models/
    │   │       │                           │       ├── DayDateModel.kt
    │   │       │                           │       └── SubjectAttendanceSummaryModel.kt
    │   │       │                           └── presentation/
    │   │       │                               ├── ScheduleScreen.kt
    │   │       │                               ├── ScheduleUiState.kt
    │   │       │                               ├── ScheduleViewModel.kt
    │   │       │                               └── components/
    │   │       │                                   ├── AttendanceComposable.kt
    │   │       │                                   ├── CalendarComposable.kt
    │   │       │                                   ├── DayDateLayout.kt
    │   │       │                                   ├── PlusMinusBtn.kt
    │   │       │                                   ├── ScheduleItem.kt
    │   │       │                                   ├── ScheduleList.kt
    │   │       │                                   ├── TaskAttendance.kt
    │   │       │                                   ├── TaskComposable.kt
    │   │       │                                   └── TimePicker.kt
    │   │       └── test/
    │   ├── notice/
    │   │   ├── build.gradle.kts
    │   │   ├── consumer-rules.pro
    │   │   ├── proguard-rules.pro
    │   │   └── src/
    │   │       ├── androidTest/
    │   │       ├── main/
    │   │       │   └── java/
    │   │       │       └── com/
    │   │       │           └── reyaz/
    │   │       │               └── feature/
    │   │       │                   └── notice/
    │   │       │                       ├── data/
    │   │       │                       │   ├── NoticeRepository.kt
    │   │       │                       │   ├── local/
    │   │       │                       │   │   ├── NoticeDatabase.kt
    │   │       │                       │   │   ├── NoticeEntity.kt
    │   │       │                       │   │   └── dao/
    │   │       │                       │   │       └── NoticeDao.kt
    │   │       │                       │   ├── model/
    │   │       │                       │   │   ├── NoticeDto.kt
    │   │       │                       │   │   └── NoticeType.kt
    │   │       │                       │   └── remote/
    │   │       │                       │       ├── NoticeParser.kt
    │   │       │                       │       └── NoticeScraper.kt
    │   │       │                       ├── di/
    │   │       │                       │   └── NoticeModule.kt
    │   │       │                       ├── domain/
    │   │       │                       │   ├── model/
    │   │       │                       │   │   ├── Notice.kt
    │   │       │                       │   │   └── TabConfig.kt
    │   │       │                       │   └── usecase/
    │   │       │                       │       └── getNoticeFromNetworkUseCase.kt
    │   │       │                       ├── presentation/
    │   │       │                       │   ├── NoticeEvent.kt
    │   │       │                       │   ├── NoticeScreen.kt
    │   │       │                       │   ├── NoticeUiState.kt
    │   │       │                       │   ├── NoticeViewModel.kt
    │   │       │                       │   └── components/
    │   │       │                       │       ├── CustomTrailingIcon.kt
    │   │       │                       │       ├── EmptyState.kt
    │   │       │                       │       ├── LoadingBar.kt
    │   │       │                       │       ├── LoadingErrorBar.kt
    │   │       │                       │       └── NoticeTabs.kt
    │   │       │                       └── util/
    │   │       │                           ├── EntityToDomain.kt
    │   │       │                           └── ToNoticeEntity.kt
    │   │       └── test/
    │   │        
    │   ├── portal/
    │   │   ├── build.gradle.kts
    │   │   ├── consumer-rules.pro
    │   │   ├── proguard-rules.pro
    │   │   └── src/
    │   │       ├── main/
    │   │       │   └── java/
    │   │       │       └── com/
    │   │       │           └── reyaz/
    │   │       │               └── feature/
    │   │       │                   └── portal/
    │   │       │                       ├── data/
    │   │       │                       │   ├── local/
    │   │       │                       │   │   └── PortalDataStore.kt
    │   │       │                       │   ├── remote/
    │   │       │                       │   │   └── PortalScraper.kt
    │   │       │                       │   ├── repository/
    │   │       │                       │   │   └── PortalRepositoryImpl.kt
    │   │       │                       │   └── worker/
    │   │       │                       │       └── AutoLoginWorker.kt
    │   │       │                       ├── di/
    │   │       │                       │   └── PortalModule.kt
    │   │       │                       ├── domain/
    │   │       │                       │   ├── model/
    │   │       │                       │   │   └── ConnectRequest.kt
    │   │       │                       │   ├── repository/
    │   │       │                       │   │   └── PortalRepository.kt
    │   │       │                       │   └── usecase/
    │   │       │                       │       ├── GetCredentialUseCase.kt
    │   │       │                       │       ├── PortalLoginUseCase.kt
    │   │       │                       │       └── SaveCredentialUseCase.kt
    │   │       │                       └── presentation/
    │   │       │                           ├── PortalScreen.kt
    │   │       │                           ├── PortalUiState.kt
    │   │       │                           ├── PortalViewModel.kt
    │   │       │                           └── components/
    │   │       │                               ├── CaptivePortalDialogContent.kt
    │   │       │                               ├── ConnectedComposable.kt
    │   │       │                               ├── CustomTextField.kt
    │   │       │                               ├── gradientBrush.kt
    │   │       │                               ├── LoginFormComposable.kt
    │   │       │                               └── NotJmiWifiComposable.kt
    │   │       └── test/
    │   └── result/
    │       ├── build.gradle.kts
    │       ├── consumer-rules.pro
    │       ├── proguard-rules.pro
    │       └── src/
    │           ├── androidTest/
    │           │   └── java/
    │           │       └── com/
    │           │           └── reyaz/
    │           │               └── feature/
    │           │                   └── result/
    │           │                       └── ExampleInstrumentedTest.kt
    │           ├── main/
    │           │   └── java/
    │           │       └── com/
    │           │           └── reyaz/
    │           │               └── feature/
    │           │                   └── result/
    │           │                       ├── data/
    │           │                       │   ├── ResultFetchWorker.kt
    │           │                       │   ├── ResultRepositoryImpl.kt
    │           │                       │   ├── local/
    │           │                       │   │   ├── dao/
    │           │                       │   │   │   └── ResultDao.kt
    │           │                       │   │   ├── dto/
    │           │                       │   │   │   ├── CourseWithList.kt
    │           │                       │   │   │   ├── RemoteCourseResultDto.kt
    │           │                       │   │   │   └── RemoteResultListDto.kt
    │           │                       │   │   └── entities/
    │           │                       │   │       ├── CourseEntity.kt
    │           │                       │   │       └── ResultListEntity.kt
    │           │                       │   ├── mapper/
    │           │                       │   │   ├── DtoListItemToEntity.kt
    │           │                       │   │   ├── ResultHtmlParser.kt
    │           │                       │   │   └── ToResultHistory.kt
    │           │                       │   └── scraper/
    │           │                       │       ├── NoOpJavaScriptErrorListener.kt
    │           │                       │       └── ResultApiService.kt
    │           │                       ├── di/
    │           │                       │   └── ResultModule.kt
    │           │                       ├── domain/
    │           │                       │   ├── model/
    │           │                       │   │   ├── CourseName.kt
    │           │                       │   │   ├── CourseType.kt
    │           │                       │   │   ├── ResultHistory.kt
    │           │                       │   │   └── ResultItem.kt
    │           │                       │   ├── repository/
    │           │                       │   │   └── ResultRepository.kt
    │           │                       │   └── usecase/
    │           │                       │       └── RefreshResultsUseCase.kt
    │           │                       ├── presentation/
    │           │                       │   ├── ResultEvent.kt
    │           │                       │   ├── ResultScreen.kt
    │           │                       │   ├── ResultUiState.kt
    │           │                       │   ├── ResultViewModel.kt
    │           │                       │   └── components/
    │           │                       │       ├── AutoCompleteDropDown.kt
    │           │                       │       ├── CourseItemComposable.kt
    │           │                       │       ├── DownloadableListItemComposable.kt
    │           │                       │       ├── DropdownButtonComposable.kt
    │           │                       │       ├── DropDownComposable.kt
    │           │                       │       ├── DropdownInsteaComposable.kt
    │           │                       │       ├── DropDownWithLoader.kt
    │           │                       │       ├── ListContainerComposable.kt
    │           │                       │       ├── ResultFormComposable.kt
    │           │                       │       ├── ResultListDivider.kt
    │           │                       │       └── TextWithIndicator.kt
    │           │                       └── worker/
    │           │                           └── ResultSyncWorker.kt
    │           └── test/
    └── gradle/
        ├── libs.versions.toml
        └── wrapper/
            └── gradle-wrapper.properties

```

## My Role
#Integrated Firebase (Firestore, Storage) for backend services and real-time data sync.
#integrated google signin with credential manager
#implemented koin dependency injection
#integrated rent listing

## ✉️ Contact

**Md Sadique**  
📧 [mdsadique47@gmail.com](mailto:mdsadique47@gmail.com)  
🔗 [LinkedIn Profile](https://linkedin.com/in/sadiquereyaz)
