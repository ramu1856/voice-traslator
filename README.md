# RC Translator

A mobile application that provides real-time voice and text translation between multiple languages. The app helps users communicate easily by removing language barriers using a simple and user-friendly interface.

## Features

- **Voice-to-text translation** using microphone input
- **Text-to-text translation**
- **Support for multiple languages** (30+ languages)
- **Simple and clean user interface**
- **Fast response and lightweight performance**
- **Language swap functionality**
- **Real-time speech recognition**

## Technology Stack

- **Android** (Kotlin)
- **Speech Recognition API** (Android SpeechRecognizer)
- **Translation API** (LibreTranslate & MyMemory)
- **Material Design Components**
- **Coroutines** for asynchronous operations
- **Retrofit/OkHttp** for network requests

## Prerequisites

- Android Studio (latest version recommended)
- Android SDK (API 24 or higher)
- Kotlin plugin
- Internet connection for translation services

## Installation

1. Clone or download this repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build and run the app on an Android device or emulator

## Permissions

The app requires the following permissions:
- **INTERNET**: For translation API calls
- **RECORD_AUDIO**: For voice input functionality
- **ACCESS_NETWORK_STATE**: To check network connectivity

## How It Works

1. User selects source and target languages
2. User speaks or types the input text
3. The app processes the input using speech recognition (for voice)
4. The translated text is displayed instantly

## Supported Languages

English, Spanish, French, German, Italian, Portuguese, Russian, Japanese, Korean, Chinese, Arabic, Hindi, Turkish, Dutch, Polish, Swedish, Norwegian, Danish, Finnish, Greek, Czech, Romanian, Hungarian, Thai, Vietnamese, Indonesian, Malay, Hebrew, Ukrainian, and more.

## Use Cases

- Travelers communicating in foreign countries
- Students learning new languages
- Job interviews and professional communication
- Daily language practice

## Future Enhancements

- Offline translation support
- More language options
- Text-to-speech output
- Conversation mode
- Improved UI and performance optimizations
- History of translations
- Favorite translations

## API Services

The app currently uses:
- **LibreTranslate** (primary): Free and open-source translation service
- **MyMemory Translation API** (fallback): Free tier available

For production use, consider:
- Setting up your own LibreTranslate instance
- Using Google Cloud Translation API
- Using DeepL API
- Using Microsoft Translator API

## License

This project is open source and available for educational purposes.

## Contributing

Contributions, issues, and feature requests are welcome!

## Contact

For questions or support, please open an issue in the repository.
