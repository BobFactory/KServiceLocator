# KServiceLocator

[![](https://jitpack.io/v/BobFactory/KServiceLocator.svg)](https://jitpack.io/#BobFactory/KServiceLocator)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


![KServiceLocator](https://user-images.githubusercontent.com/26218176/216244327-875e554b-7611-446b-baf3-09fdd1adbb1e.png)

KServiceLocator is a lightweight, efficient service locator for Android written in Kotlin. It allows you to easily manage your dependencies and make them accessible anywhere in your application. This is a simpler and more robust version of KOIN and can be used on simple projects and also complex projects based on needs. 

<br>

### Why another DI library when HILT and KOIN already exsit ?

Hilt is a good DI library but is overly complex and feels like an overkill for a simpler that don't have a ton of complexity in them. All we want to do is get the dependencies that we declare. Koin seems like a good alternative, but in their quest to become a full fledged DI I think they made the solution a bit complicated. Loading and unloading dependencies can be unpredictable and after seeing a few crashes because of this, I decided to write my own implementation. 

Moreover this follows a service locator pattern rather than standard DI approach. 


<br>

## Installation
Distribution is done through jitpack.

**Step 1.** 
Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
  repositories {
   ...
   maven { url 'https://jitpack.io' }
  }
}
```

**Step 2.** 
Add the dependency

```groovy
dependencies {
  implementation 'com.github.BobFactory:KServiceLocator:LATEST_VERSION_TAG'
}
```

## Usage
Usage of this library is fairly similar to how Koin dependency declaration is done but with some caveats. 

### Declaring dependencies
The library is preshipped with a `ServiceLocator` object, which acts as a container for all the dependencies. 
Use the object to declare dependencies. 

```kotlin

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        
        with(ServiceLocator) {
        
            //Context object
            androidContext(this@MainApp)
            
            single { Dependency1() }
            factory { Dependency1() }
            viewModel { SomeViewModel() }

        }
    }
}
```

### Accessing Dependencies 

You can access the dependency anywhere in the application by simply calling `get<Type>()` function provided in the package. If you want to invoke the dependency in a lazy fashion then use the `by inject()` delegate. 

```kotlin
val dep1: SomeClass = get()
val dep2: SomeClass = get()

val dep3: SomeClass by inject()
val dep4: SomeClass by inject()

```

### Declaring Dependencies Which Have Other Dependencies

Most likely you are going to have dependencies that involve other dependencies. Simple use the `get<Type>()` function to invoke them. 

```kotlin

  class Universe(order: Order, chaos: Chaos)
  class Order
  class Chaos
  
  with(ServiceLocator){
    
    factory { Order() }
    factory { Chaos() }
    factory { Universe(get(), get()) } // invokes the value present in the locator object for the depdendency.
  }
```

**Note:**
As you might have guessed it this is not a lazy generation of dependencies. When you call the `get()` function in the above example it is mandatory to register the `Order` and `Chaos` classes before the `Universe` class for the `get()` function to look them up. The dependency is immediately registered in a sequential fashion into the ServiceLocator. 

### ViewModel & Compose Support 

You can also register the viewmodels and access them in your composable functions too. You can also pass a savedStateHandle to the viewmodel. 

To register a viewmodel use

```kotlin

with(ServiceLocator) {

  viewModel { SomeViewModel() }
  viewModel { savedStateHandle -> OtherViewModel(savedStateHandle) }
  
}
```

To access the viewmodel using the delegate:

```kotlin
class MainActivity : ComponentActivity() {
  
  val vm : MainViewModel by serviceViewModel()
  
}
```

For immediately accessing the viewmodel use the getViewModel function: 

```kotlin
class MainActivity : ComponentActivity() {
  
  val vm : MainViewModel = getViewModel()
  
}
```

For invoking viewmodel inside a compose

```kotlin

  Surface(
   modifier = Modifier.fillMaxSize(),
   color = MaterialTheme.colors.background
  ) {
  
     val vm: MainViewModel = serviceViewModel()
     ...
  }
 ```  
 

                    
### Android Context Support 
You can also store the android context into the ServiceLocator object to be used in any part of the application. **Please use this functionality carefully**, do not place the activity context into the ServiceLocator as it will be considered a memory leak. You can however save the MainActivity context if your app follows the single activity architecture pattern or the context saved is application contex.

```kotlin
class MainApp: Application {
  
  fun onCreate() {
    with(ServiceLocator) {
      androidContext(this@MainApp)
    }
  }
  
}

//Access the context object using the regular get function later
class SomeClass(context: Context = get())
```

                   
## Dependency Example
Since there are no module support and the dependencies are immediately loaded into the locator you will have to use comments to seperate the different dependencies, just make sure they follow a serial linear declaration fashion.

```kotlin

with(ServiceLocator) {

  //Context
  androidContext(context)

  //Database
  single { Database() }
  
  //Services
  factory { UserService(database = get()) }
  factory { FirebaseService() }
  factory { UserRepository(userService = get(), firebaseService = get()) }
  
  //ViewModels
  viewModel { handle -> EditHabitViewModel(handle, get(), get()) }
  viewModel { AddHabitViewModel(get()) }

}
```


## License

```
MIT License

Copyright (c) 2023 Bawender Yandra

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```



