# 🛠️ TapCraft Pro - Developer Setup Guide

## 🚀 **Complete Development Environment Setup**

This guide will help you set up a complete development environment for TapCraft Pro, including all necessary tools, dependencies, and configurations.

## 📋 **Table of Contents**

1. [Prerequisites](#prerequisites)
2. [Development Environment Setup](#development-environment-setup)
3. [Project Configuration](#project-configuration)
4. [API Keys & Credentials Setup](#api-keys--credentials-setup)
5. [Database Setup](#database-setup)
6. [Testing Environment](#testing-environment)
7. [Build & Deployment](#build--deployment)
8. [Debugging & Troubleshooting](#debugging--troubleshooting)

---

## ✅ **Prerequisites**

### **System Requirements**
- **Operating System**: Windows 10/11, macOS 10.15+, or Ubuntu 18.04+
- **RAM**: Minimum 8GB, Recommended 16GB+
- **Storage**: 20GB free space for Android SDK and dependencies
- **Java**: JDK 11 or higher
- **Git**: Latest version

### **Required Software**

#### **1. Android Studio**
```bash
# Download from: https://developer.android.com/studio
# Version: Arctic Fox (2020.3.1) or newer
# Include Android SDK, Android SDK Platform-Tools, Android Emulator
```

#### **2. Java Development Kit (JDK)**
```bash
# Option 1: Oracle JDK 11+
# Download from: https://www.oracle.com/java/technologies/downloads/

# Option 2: OpenJDK 11+
# Windows (using Chocolatey):
choco install openjdk11

# macOS (using Homebrew):
brew install openjdk@11

# Ubuntu:
sudo apt update
sudo apt install openjdk-11-jdk
```

#### **3. Git**
```bash
# Windows: Download from https://git-scm.com/
# macOS: brew install git
# Ubuntu: sudo apt install git
```

#### **4. Node.js (for build tools)**
```bash
# Download from: https://nodejs.org/
# Version: 16.x or higher

# Verify installation:
node --version
npm --version
```

---

## 🔧 **Development Environment Setup**

### **1. Clone the Repository**
```bash
# Clone the main repository
git clone https://github.com/tapcraft/tapcraft-pro.git
cd tapcraft-pro

# Or if you have SSH access:
git clone git@github.com:tapcraft/tapcraft-pro.git
cd tapcraft-pro
```

### **2. Android Studio Configuration**

#### **Install Required Plugins**
1. Open Android Studio
2. Go to **File** → **Settings** → **Plugins**
3. Install these plugins:
   ```
   - Kotlin (should be pre-installed)
   - Android Gradle Plugin
   - Jetpack Compose
   - Hilt Android
   - Detekt (for code quality)
   - SonarLint (for code analysis)
   ```

#### **SDK Configuration**
1. Go to **File** → **Settings** → **Appearance & Behavior** → **System Settings** → **Android SDK**
2. Install these SDK platforms:
   ```
   - Android 14 (API level 34) - Target SDK
   - Android 13 (API level 33)
   - Android 12 (API level 31)
   - Android 7.0 (API level 24) - Minimum SDK
   ```
3. Install these SDK tools:
   ```
   - Android SDK Build-Tools 34.0.0
   - Android Emulator
   - Android SDK Platform-Tools
   - Intel x86 Emulator Accelerator (HAXM installer)
   ```

### **3. Environment Variables**
```bash
# Add to your shell profile (.bashrc, .zshrc, etc.)

# Android SDK
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Java
export JAVA_HOME=/path/to/your/jdk
export PATH=$PATH:$JAVA_HOME/bin

# Reload your shell
source ~/.bashrc  # or ~/.zshrc
```

### **4. Gradle Configuration**

#### **Global Gradle Properties**
Create `~/.gradle/gradle.properties`:
```properties
# Performance optimizations
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true

# Android optimizations
android.useAndroidX=true
android.enableJetifier=true
android.enableR8.fullMode=true
android.enableBuildCache=true

# Kotlin optimizations
kotlin.incremental=true
kotlin.incremental.android=true
kotlin.incremental.java=true
```

---

## ⚙️ **Project Configuration**

### **1. Local Properties Setup**
Create `local.properties` in the project root:
```properties
# Android SDK location
sdk.dir=/path/to/Android/Sdk
ndk.dir=/path/to/Android/Sdk/ndk/25.1.8937393

# Development API endpoints
DEV_API_ENDPOINT=http://localhost:8080
STAGING_API_ENDPOINT=https://api-staging.tapcraft.pro
PRODUCTION_API_ENDPOINT=https://api.tapcraft.pro

# Development database
DEV_DATABASE_URL=postgresql://localhost:5432/tapcraft_dev
DEV_REDIS_URL=redis://localhost:6379

# Development keys (use test keys for development)
GOOGLE_MAPS_API_KEY=AIzaSyC4YourDevelopmentGoogleMapsAPIKey
FIREBASE_API_KEY=AIzaSyC4YourDevelopmentFirebaseAPIKey

# AdMob Test IDs (for development)
ADMOB_APP_ID=ca-app-pub-3940256099942544~3347511713
ADMOB_BANNER_ID=ca-app-pub-3940256099942544/6300978111
ADMOB_INTERSTITIAL_ID=ca-app-pub-3940256099942544/1033173712
ADMOB_REWARDED_ID=ca-app-pub-3940256099942544/5224354917

# Development cloud storage (use test buckets)
AWS_ACCESS_KEY_ID=AKIAIOSFODNN7EXAMPLE
AWS_SECRET_ACCESS_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
AWS_REGION=us-east-1
AWS_S3_BUCKET=tapcraft-dev-storage

# Development analytics (use test tokens)
MIXPANEL_TOKEN=development_token_here
AMPLITUDE_API_KEY=development_key_here

# Development payment (use test keys)
STRIPE_PUBLISHABLE_KEY=pk_test_abcdef1234567890
STRIPE_SECRET_KEY=sk_test_abcdef1234567890
```

### **2. Firebase Development Setup**

#### **Create Development Firebase Project**
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project: "TapCraft Pro Dev"
3. Add Android app with package: `com.tapcraft.pro.automation.debug`
4. Download `google-services.json` for debug variant
5. Place in `app/src/debug/google-services.json`

#### **Firebase Configuration**
```json
// app/src/debug/google-services.json
{
  "project_info": {
    "project_number": "123456789012",
    "firebase_url": "https://tapcraft-pro-dev-default-rtdb.firebaseio.com",
    "project_id": "tapcraft-pro-dev",
    "storage_bucket": "tapcraft-pro-dev.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:123456789012:android:abcdef1234567890",
        "android_client_info": {
          "package_name": "com.tapcraft.pro.automation.debug"
        }
      }
    }
  ]
}
```

---

## 🔑 **API Keys & Credentials Setup**

### **1. Google Services Setup**

#### **Google Cloud Console**
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create new project: "TapCraft Pro Development"
3. Enable these APIs:
   ```
   - Maps SDK for Android
   - Places API
   - Firebase APIs
   - Google Drive API
   - Gmail API
   ```
4. Create credentials:
   ```
   - API Key (for Maps)
   - OAuth 2.0 Client ID (for Drive/Gmail)
   - Service Account (for backend services)
   ```

#### **Google Play Console (for testing)**
1. Create a developer account
2. Create internal testing track
3. Upload signed APK for testing
4. Configure in-app products for testing

### **2. Third-party Service Setup**

#### **Development Slack Workspace**
```bash
# Create development Slack workspace
1. Go to https://slack.com/create
2. Create workspace: "TapCraft Dev Team"
3. Create Slack app: https://api.slack.com/apps
4. Get OAuth tokens for development
5. Add to local.properties
```

#### **Development Database**
```bash
# PostgreSQL setup (using Docker)
docker run --name tapcraft-dev-db \
  -e POSTGRES_DB=tapcraft_dev \
  -e POSTGRES_USER=tapcraft \
  -e POSTGRES_PASSWORD=dev_password \
  -p 5432:5432 \
  -d postgres:13

# Redis setup (using Docker)
docker run --name tapcraft-dev-redis \
  -p 6379:6379 \
  -d redis:6-alpine
```

### **3. Development Certificates**

#### **Debug Keystore**
```bash
# Generate debug keystore (if not exists)
keytool -genkey -v -keystore debug.keystore \
  -storepass android -alias androiddebugkey \
  -keypass android -keyalg RSA -keysize 2048 \
  -validity 10000 \
  -dname "CN=Android Debug,O=Android,C=US"

# Get SHA-1 fingerprint for Firebase
keytool -list -v -keystore debug.keystore \
  -alias androiddebugkey -storepass android
```

#### **Development SSL Certificate**
```bash
# For local HTTPS development
openssl req -x509 -newkey rsa:4096 -keyout dev-key.pem \
  -out dev-cert.pem -days 365 -nodes \
  -subj "/C=US/ST=CA/L=SF/O=TapCraft/CN=localhost"
```

---

## 🗄️ **Database Setup**

### **1. Local Development Database**

#### **PostgreSQL with Docker**
```bash
# Create docker-compose.yml for development
version: '3.8'
services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: tapcraft_dev
      POSTGRES_USER: tapcraft
      POSTGRES_PASSWORD: dev_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./scripts/init.sql:/docker-entrypoint-initdb.d/init.sql

  redis:
    image: redis:6-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

volumes:
  postgres_data:
  redis_data:

# Start services
docker-compose up -d
```

#### **Database Schema Setup**
```sql
-- scripts/init.sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    subscription_tier VARCHAR(50) DEFAULT 'free',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Scripts table
CREATE TABLE scripts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    nodes JSONB NOT NULL,
    connections JSONB NOT NULL,
    variables JSONB DEFAULT '{}',
    settings JSONB DEFAULT '{}',
    status VARCHAR(50) DEFAULT 'draft',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Executions table
CREATE TABLE executions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    script_id UUID REFERENCES scripts(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    duration INTEGER,
    result JSONB,
    logs JSONB DEFAULT '[]',
    error_message TEXT
);

-- Create indexes
CREATE INDEX idx_scripts_user_id ON scripts(user_id);
CREATE INDEX idx_scripts_category ON scripts(category);
CREATE INDEX idx_executions_script_id ON executions(script_id);
CREATE INDEX idx_executions_user_id ON executions(user_id);
CREATE INDEX idx_executions_status ON executions(status);
```

### **2. Database Migration Setup**
```kotlin
// app/src/main/java/com/tapcraft/pro/automation/database/migrations/Migration1to2.kt
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            ALTER TABLE scripts 
            ADD COLUMN tags TEXT DEFAULT '[]'
        """)
        
        database.execSQL("""
            CREATE INDEX idx_scripts_tags ON scripts(tags)
        """)
    }
}
```

---

## 🧪 **Testing Environment**

### **1. Unit Testing Setup**

#### **Test Dependencies**
```kotlin
// app/build.gradle.kts
dependencies {
    // Unit testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.6.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("com.google.truth:truth:1.1.4")
    
    // Android testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    
    // Hilt testing
    testImplementation("com.google.dagger:hilt-android-testing:2.55")
    kaptTest("com.google.dagger:hilt-android-compiler:2.55")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.55")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.55")
}
```

#### **Test Configuration**
```kotlin
// app/src/test/java/com/tapcraft/pro/automation/TestApplication.kt
@HiltAndroidApp
class TestApplication : Application()

// app/src/test/java/com/tapcraft/pro/automation/di/TestDatabaseModule.kt
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {
    
    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): TapCraftDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            TapCraftDatabase::class.java
        ).allowMainThreadQueries().build()
    }
}
```

### **2. Integration Testing**

#### **API Testing Setup**
```kotlin
// app/src/test/java/com/tapcraft/pro/automation/network/MockWebServerTest.kt
class ApiIntegrationTest {
    
    @get:Rule
    val mockWebServer = MockWebServerRule()
    
    @Before
    fun setup() {
        // Configure mock responses
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""
                    {
                        "success": true,
                        "data": {
                            "scripts": []
                        }
                    }
                """.trimIndent())
        )
    }
    
    @Test
    fun `test script list API`() = runTest {
        val apiService = ApiService(mockWebServer.baseUrl)
        val response = apiService.getScripts()
        
        assertThat(response.success).isTrue()
        assertThat(response.data.scripts).isEmpty()
    }
}
```

### **3. UI Testing**

#### **Compose Testing**
```kotlin
// app/src/androidTest/java/com/tapcraft/pro/automation/ui/ScriptEditorTest.kt
@HiltAndroidTest
class ScriptEditorTest {
    
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun testCreateNewScript() {
        composeTestRule.setContent {
            TapCraftTheme {
                ScriptEditorScreen(
                    onNavigateBack = {},
                    onSaveScript = {}
                )
            }
        }
        
        // Test script creation flow
        composeTestRule
            .onNodeWithText("Add Node")
            .performClick()
        
        composeTestRule
            .onNodeWithText("Click Action")
            .performClick()
        
        composeTestRule
            .onNodeWithContentDescription("Script Canvas")
            .assertExists()
    }
}
```

---

## 🏗️ **Build & Deployment**

### **1. Build Variants Configuration**

#### **Build Types**
```kotlin
// app/build.gradle.kts
android {
    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8080\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            
            resValue("string", "app_name", "TapCraft Pro Debug")
        }
        
        create("staging") {
            initWith(getByName("release"))
            isDebuggable = false
            isMinifyEnabled = true
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            
            buildConfigField("String", "API_BASE_URL", "\"https://api-staging.tapcraft.pro\"")
            
            resValue("string", "app_name", "TapCraft Pro Staging")
        }
        
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            
            buildConfigField("String", "API_BASE_URL", "\"https://api.tapcraft.pro\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
            
            resValue("string", "app_name", "TapCraft Pro")
        }
    }
}
```

### **2. Signing Configuration**

#### **Debug Signing**
```kotlin
// app/build.gradle.kts
android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        
        create("release") {
            storeFile = file(System.getenv("KEYSTORE_FILE") ?: "release.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### **3. Gradle Tasks**

#### **Custom Build Tasks**
```kotlin
// app/build.gradle.kts
tasks.register("generateDebugApk") {
    dependsOn("assembleDebug")
    doLast {
        println("Debug APK generated successfully!")
    }
}

tasks.register("runUnitTests") {
    dependsOn("testDebugUnitTest")
    doLast {
        println("Unit tests completed!")
    }
}

tasks.register("runInstrumentedTests") {
    dependsOn("connectedDebugAndroidTest")
    doLast {
        println("Instrumented tests completed!")
    }
}

tasks.register("generateTestReport") {
    dependsOn("runUnitTests", "runInstrumentedTests")
    doLast {
        println("Test reports generated!")
    }
}
```

### **4. CI/CD Setup**

#### **GitHub Actions Workflow**
```yaml
# .github/workflows/android.yml
name: Android CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Cache Gradle packages
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
    
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    
    - name: Run unit tests
      run: ./gradlew testDebugUnitTest
    
    - name: Generate test report
      uses: dorny/test-reporter@v1
      if: success() || failure()
      with:
        name: Unit Test Results
        path: '**/TEST-*.xml'
        reporter: java-junit
    
    - name: Build debug APK
      run: ./gradlew assembleDebug
    
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: debug-apk
        path: app/build/outputs/apk/debug/*.apk

  build:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Build release APK
      run: ./gradlew assembleRelease
      env:
        KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
        KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
        KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
    
    - name: Upload release APK
      uses: actions/upload-artifact@v3
      with:
        name: release-apk
        path: app/build/outputs/apk/release/*.apk
```

---

## 🐛 **Debugging & Troubleshooting**

### **1. Common Issues**

#### **Build Issues**
```bash
# Clear Gradle cache
./gradlew clean
rm -rf ~/.gradle/caches/

# Reset Android Studio
File → Invalidate Caches and Restart

# Update Gradle wrapper
./gradlew wrapper --gradle-version=8.11.1

# Fix dependency conflicts
./gradlew app:dependencies --configuration debugRuntimeClasspath
```

#### **Emulator Issues**
```bash
# List available AVDs
emulator -list-avds

# Start emulator from command line
emulator -avd Pixel_4_API_34 -no-snapshot-load

# Wipe emulator data
emulator -avd Pixel_4_API_34 -wipe-data

# Enable hardware acceleration
# Windows: Enable Hyper-V or HAXM
# macOS: Enable Hypervisor.framework
# Linux: Enable KVM
```

### **2. Debugging Tools**

#### **Android Studio Debugger**
```kotlin
// Set breakpoints in code
fun executeScript(script: Script) {
    debugger() // Breakpoint here
    
    val result = scriptEngine.execute(script)
    
    Log.d("ScriptExecution", "Result: $result") // Log output
}
```

#### **Network Debugging**
```kotlin
// Enable network logging in debug builds
if (BuildConfig.DEBUG) {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
}
```

#### **Database Debugging**
```kotlin
// Enable Room query logging
@Database(
    entities = [Script::class, Execution::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TapCraftDatabase : RoomDatabase() {
    
    companion object {
        fun create(context: Context): TapCraftDatabase {
            return Room.databaseBuilder(
                context,
                TapCraftDatabase::class.java,
                "tapcraft.db"
            ).apply {
                if (BuildConfig.DEBUG) {
                    setQueryCallback(
                        RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
                            Log.d("RoomQuery", "SQL: $sqlQuery, Args: $bindArgs")
                        },
                        Executors.newSingleThreadExecutor()
                    )
                }
            }.build()
        }
    }
}
```

### **3. Performance Profiling**

#### **Memory Profiling**
```kotlin
// Memory leak detection with LeakCanary (debug builds only)
// Add to debug build.gradle
debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.12'

// Custom memory monitoring
class MemoryMonitor {
    fun logMemoryUsage() {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        val maxMemory = runtime.maxMemory()
        
        Log.d("Memory", "Used: ${usedMemory / 1024 / 1024}MB, Max: ${maxMemory / 1024 / 1024}MB")
    }
}
```

#### **Performance Monitoring**
```kotlin
// Custom performance monitoring
class PerformanceMonitor {
    fun measureExecutionTime(operation: String, block: () -> Unit) {
        val startTime = System.currentTimeMillis()
        block()
        val endTime = System.currentTimeMillis()
        
        Log.d("Performance", "$operation took ${endTime - startTime}ms")
    }
}
```

---

## 📚 **Development Resources**

### **Documentation**
- **Android Developer Docs**: https://developer.android.com/
- **Kotlin Documentation**: https://kotlinlang.org/docs/
- **Jetpack Compose**: https://developer.android.com/jetpack/compose
- **Hilt Documentation**: https://dagger.dev/hilt/

### **Code Quality Tools**
```kotlin
// app/build.gradle.kts
apply plugin: 'io.gitlab.arturbosch.detekt'

detekt {
    config = files("$projectDir/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.4"
}
```

### **Useful Commands**
```bash
# Build commands
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK
./gradlew installDebug           # Install debug APK
./gradlew uninstallAll           # Uninstall all variants

# Testing commands
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumented tests
./gradlew jacocoTestReport       # Generate test coverage

# Code quality
./gradlew detekt                 # Run static analysis
./gradlew ktlintCheck           # Check code formatting
./gradlew ktlintFormat          # Format code

# Dependency management
./gradlew dependencies          # Show dependency tree
./gradlew dependencyUpdates     # Check for updates
```

---

## ✅ **Development Checklist**

### **Initial Setup**
- [ ] Android Studio installed and configured
- [ ] JDK 11+ installed
- [ ] Android SDK and tools installed
- [ ] Git repository cloned
- [ ] Environment variables configured

### **Project Configuration**
- [ ] `local.properties` file created with API keys
- [ ] Firebase project created and configured
- [ ] Debug keystore generated
- [ ] Database setup completed
- [ ] Test environment configured

### **Development Workflow**
- [ ] Code formatting rules applied
- [ ] Unit tests written and passing
- [ ] Integration tests configured
- [ ] Debug builds working
- [ ] Emulator/device testing completed

### **Before Committing**
- [ ] Code formatted with ktlint
- [ ] Static analysis passed (Detekt)
- [ ] Unit tests passing
- [ ] No lint errors
- [ ] Debug build successful

---

*This developer setup guide provides everything needed to start developing TapCraft Pro. For additional help, visit our developer documentation at docs.tapcraft.pro*