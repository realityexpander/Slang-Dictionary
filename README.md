# Slang-Dictionary
American Slang Dictionary app using CLEAN architecture, MVVM, Use Cases, GSON, Retrofit, Compose, Room with caching, Hilt Dependency Injection

API from [dictionaryapi.dev](https://dictionaryapi.dev)

[<img src="https://user-images.githubusercontent.com/5157474/172096461-0d6f8acb-7537-4f0e-98f1-5514d63c92ed.png" width="325"/>](https://user-images.githubusercontent.com/5157474/172096461-0d6f8acb-7537-4f0e-98f1-5514d63c92ed.png)
[<img src="https://user-images.githubusercontent.com/5157474/172085035-40eda1ce-4321-48ee-9fbe-41f02275d3ba.png" width="325"/>](https://user-images.githubusercontent.com/5157474/172085035-40eda1ce-4321-48ee-9fbe-41f02275d3ba.png)


- Allows user to search for American Slang and regular words
- Automatically caches words locally for use off-line
- Allows user to play audio clip of pronounciation (online only)

## Tech Employed
- List of words is cached locally in a `Room` database
- Uses custom text search query for `Room` database
- Uses custom Type Converters for `Room` that allow for simplified database schema
- Shows proper use of `Dagger-Hilt` & SOLID & CLEAN architecture
- Uses compose for view layer, `ViewModel` and `mutableState` to send UI events
- Uses Flow for API, UI & Database events
- Uses Resource sealed class to handle errors and messaging
- Custom tap event handling to hide keyboard\
- MediaPlayer to stream mp3 of audio pronounciation

To install the Apk:

1. Open this link on your Android device:
   https://github.com/realityexpander/Slang-Dicitonary/blob/master/SlangDictionary_1.0.apk
2. Tap the "skewer" menu and tap the "download"

   [![](https://user-images.githubusercontent.com/5157474/147434050-57102a30-af32-46ed-a90b-d94e0c4a4f35.jpg)]()
3. Allow the file to download (DO NOT click "show details")
4. After the file is downloaded, click "OK" to install
5. Click "OK" to install
6. Click "OK" to launch

If you have developer options turned on, you may need to turn off "USB Debugging" if the "Waiting for debugger" dialog is displayed.
