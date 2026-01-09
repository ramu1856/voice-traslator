# RC Translator - Setup Guide

## Quick Start

1. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to this directory and select it
   - Wait for Gradle sync to complete

2. **Sync Gradle**
   - If Gradle doesn't sync automatically, click "Sync Now" when prompted
   - Or go to: File → Sync Project with Gradle Files

3. **Add Launcher Icons** (Optional)
   - The project includes placeholder launcher icon configurations
   - For production, create proper launcher icons:
     - Right-click `app/src/main/res` → New → Image Asset
     - Or use Android Asset Studio online

4. **Run the App**
   - Connect an Android device (API 24+) or start an emulator
   - Click the "Run" button (green play icon) or press Shift+F10
   - The app will install and launch on your device

## Requirements

- **Android Studio**: Arctic Fox or later (recommended: latest version)
- **Android SDK**: API 24 (Android 7.0) or higher
- **JDK**: 8 or higher
- **Internet Connection**: Required for translation API calls

## Permissions

The app will automatically request microphone permission when you first use the voice input feature. Make sure to grant this permission for voice translation to work.

## Testing

1. **Text Translation**:
   - Select source and target languages
   - Type text in the input field
   - Tap "Translate" button

2. **Voice Translation**:
   - Tap "Tap to Speak" button
   - Grant microphone permission if prompted
   - Speak clearly in the selected source language
   - The app will automatically translate after recognition

3. **Language Swap**:
   - Tap the "⇅" button to swap source and target languages
   - Input and translated text will also swap

## Troubleshooting

### Gradle Sync Issues
- Make sure you have a stable internet connection
- Try: File → Invalidate Caches / Restart
- Check that your Android SDK is properly configured

### Speech Recognition Not Working
- Ensure microphone permission is granted
- Check that your device supports speech recognition
- Try speaking more clearly or in a quieter environment

### Translation Not Working
- Check your internet connection
- The free translation APIs may have rate limits
- Try again after a few moments

### Build Errors
- Clean and rebuild: Build → Clean Project, then Build → Rebuild Project
- Make sure all dependencies are downloaded

## API Configuration

The app currently uses free translation APIs:
- **LibreTranslate** (primary)
- **MyMemory Translation API** (fallback)

For production use, consider:
- Setting up your own LibreTranslate instance
- Using Google Cloud Translation API (requires API key)
- Using DeepL API (requires API key)
- Using Microsoft Translator API (requires API key)

To use a different API, modify `TranslationService.kt` in:
`app/src/main/java/com/rctranslator/app/service/TranslationService.kt`

## Project Structure

```
RC Translator/
├── app/
│   ├── src/main/
│   │   ├── java/com/rctranslator/app/
│   │   │   ├── MainActivity.kt          # Main activity
│   │   │   ├── model/
│   │   │   │   └── Language.kt         # Language enum
│   │   │   ├── service/
│   │   │   │   └── TranslationService.kt # Translation API
│   │   │   └── utils/
│   │   │       └── SpeechRecognitionHelper.kt # Speech recognition
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml   # Main UI layout
│   │   │   ├── values/
│   │   │   │   ├── strings.xml         # String resources
│   │   │   │   ├── colors.xml          # Color resources
│   │   │   │   └── themes.xml          # App theme
│   │   │   └── drawable/               # Icons and drawables
│   │   └── AndroidManifest.xml         # App manifest
│   └── build.gradle                    # App-level build config
├── build.gradle                        # Project-level build config
├── settings.gradle                     # Project settings
└── README.md                           # Project documentation
```

## Next Steps

1. Test all features thoroughly
2. Customize the UI colors and theme if desired
3. Add your own launcher icons
4. Consider adding more languages if needed
5. For production: Set up a reliable translation API service

## Support

For issues or questions, please refer to the README.md file or open an issue in the project repository.
