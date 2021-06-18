# Android interview project
This repo represents a solution for an Android task I've received as a part of a technical interview.

#### Description
This is an interview project. The Android app should be like the Instagram
application but simpler.

#### Requirements
- Home activity that displays all the photos.
- The photos should be loaded from a webservice that returns a json
document. You should parse it to have a photo object in the project. The
photo object should have the id, title, comment, published date and the
url of the photo. The url of the endpoint is:
http://www.json-generator.com/api/json/get/cftPFNNHsi
- When the user taps into a photo you should open another activity with
the photo description.
- The photo detail view should support **landscape and portrait** orientation.
- In the home activity you should have a button to take a photo with the
camera and display it in the photo list along with the other photos.
- We need the project to **support Android 4.1+**.
- The project should be on Android Studio. Use the dependencies you
want.

#### Tech stack
- Architecture components
- Kotlin
- Kotlin coroutines
- Room
- Retrofit
- Flow
- Repository pattern
- MVVM pattern
- LiveData
- Dependency Injection with Hilt
- Data class
- Sealed classes
- ViewBinding
- RecyclerView ListAdapter with DiffUtils
- Glide
- ConstraintLayout
