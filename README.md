# This is a dark horse demo project

## Prepare Work
1. Install AndroidStudio latest version
2. Install Android SDK (selected default options to install)
3. Install JDK 11
4. Better to set the java home to the system path
5. Has a Android mobile phone or install emulator

## How to run this project
1. open the project using AndroidStudio
2. Click the `run` button to install the app (will open the app automatically)

or

1. cd the project root directory
2. run command `./gradlew installDebug` or `./gradlew installRelease`
3. open your test device to open the app

## How to run unit tests

### Unit tests (only include java/kotlin test)
1. cd the project root directory
2. run command `./gradlew test`
   
### Android Unit test (should connect a Android device)
1. cd the project root directory
2. run command `./gradlew connectedAndroidTest`
