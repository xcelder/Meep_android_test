# Meep_android_test
App to check the resources for mobility in the city

**Given some issues with the billing accounts on Google Cloud, the map implementation was done using MapBox instead of GoogleMaps**

## Architecture MVP-ViewModel

I chose this architecture because it's what I'm most used to and I think it's a simple way to separate the logic in layers and be testable. Undoubtedly,  it's not as complete as Clean architecture, but it is faster, and so I thought more suitable for this technical test.

This architecture bases it's layers on MVP, but it takes advantage of the Android's `ViewModel` to store the `ViewState` and the `Presenter` through the Android components lifecycle.

### Structure

The architecture structure is simple:

* `View` (`Activity/Fragment`)
  * `ViewModel`
    * `ViewState`
    * `Presenter`
      * `Repository` ...
    
Like in MVP architecture, the events go to the `Presenter`, who manages the logic and comunicates with the view through the `ViewState`. In this case, using a `State` design pattern by `LiveData` observers, who are observed within the `View`.

## Project structure

The project is based on one simple `Activity` ([MainActivity][1]) and there are two screens with two fragments:

* [EndpointFragment][2]: a screen that shows a simple navigation to the Map screen.
* [ResourcesViewerMapFragment][3]: here is the kernel of the app. Here we can control the map and search for resources.

### ResourcesViewerMapFragment

When we reach this screen, there will be a Map situated in the center of the given bounds to search resources. When the camera stops, a button should appear to allow us to "Search in this area" the available resources. Also, if we tap on the "Search" button a `ProgressDialog` will appear in the screen and the button will disappear. In the meantime, the API call is being executed.
When the resources are loaded, it will be displayed as markers on the map. When we tap on a marker, a `BottomSheetDialog` will appear with the details of the resource.
I we move the map position, the "Search" button will appear again.

[1]: https://github.com/xcelder/Meep_android_test/blob/main/app/src/main/java/com/example/meep_android_test/features/MainActivity.kt
[2]: https://github.com/xcelder/Meep_android_test/blob/main/app/src/main/java/com/example/meep_android_test/features/entry_point/presentation/EntryPointFragment.kt
[3]: https://github.com/xcelder/Meep_android_test/blob/main/app/src/main/java/com/example/meep_android_test/features/resources_viewer_map/presentation/ResourcesViewerMapFragment.kt
