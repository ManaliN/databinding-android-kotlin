# Databinding-Kotlin-Android
An MVP app example using Databinding  


## Steps to set up data binding
Add the following in the module level build.gradle file e.g. app/build.gradle,
 1. Add the following inside android section:

    ```
    dataBinding {
        enabled = true
    }
    ```
 2. Along with other plugins add the following in the top:
    ```
    apply plugin: 'kotlin-kapt'
    
    ```
 3. Add the following in the dependencies:
 
    ```
    kapt "com.android.databinding:compiler:$gradle_version"  

    ```
 4. Create a model class, e.g. SearchViewModel in this example.
 
 5. In the xml file of the activity where data binding will be used, create a layout tag.
    Place data tag inside this layout tag. If we have multiple data models which we want to bind to the Ui, 
    we can create different data variables. We use the name field to access parameters of Model class.
    
 6. Inside the activity, where we implement data binding, create a binding variable of ActivityMainBinding.
   
 7. Replace the defaule setContentView with the DataBindingUtil.setContentView:
    ```
    e.g.
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    ```
    
## Features included in the example:

#### Basic Data Binding:

* This is a basic example showing use of data binding to bridge the communication between data and UI.  

#### Network Communication 
* This app takes user input and makes a query to the Github public API to search repositories.
https://developer.github.com/v3/search/#search-repositories. 
* Remember to add android.permission.INTERNET in the AndroidManifest file as any network call will need this permission.

#### JSON Data Parsing 
* After receiving response from the API it parses the Json data to search for Repository Name, Description, Star count and URL of User's Avatar.

#### Communication between UI Thread and Background Thread
* In this example we briefly cover making network calls, parsing the response and then communicating the parsed data back to the UI thread.

#### Loading Image from an URL
* Use of Picasso Library has been included to highlight getting data from an URL.

#### Data Binding with List View
* The response is displayed in List View using data binding.

#### Preserving data on Runtime Configuration Changes

* The app preserves data on runtime configuration changes like Screen rotations, keyboard changes.