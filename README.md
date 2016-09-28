[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxPermissionsResult-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/4376)

# RxPermissionsResult

Following the same approach that [RxActivityResult](https://github.com/VictorAlbertos/RxActivityResult) does, RxPermissionsResult is a reactive wrapper around the permission's Android Api, which allows to get the data without breaking the observable chain. 

## Features:
* Request permissions at runtime from any class, as long as you supply a valid `Activity` or `Fragment` instance.
* Get the data back encapsulated in an `observable` and keep chaining. 
* Survive to configuration changes. 

## SetUp
Add to top level *gradle.build* file

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Add to app module *gradle.build* file
```gradle
dependencies {
	//RxJava1
    compile 'com.github.VictorAlbertos.RxPermissionsResult:rx1:0.0.3'
    compile 'io.reactivex:rxjava:1.1.10'

	//RxJava2
    compile 'com.github.VictorAlbertos.RxPermissionsResult:rx2:0.0.3'
    compile 'io.reactivex.rxjava2:rxjava:2.0.0-RC2'
}
```

## Usage

Call `RxPermissionsResult.register` in your Android `Application`.
        
```java
public class SampleApp extends Application {

    @Override public void onCreate() {
        super.onCreate();
        RxPermissionsResult.register(this);
    }
}
```

You can call `RxPermissionsResult.on(this).requestPermissions(permissions)` supplying both, an `Activity` instance or a `Fragment` instance.

**Limitation:**: Your fragments need to extend from `android.support.v4.app.Fragment` instead of `android.app.Fragment`, otherwise they won't be notified. 

```java
String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

RxPermissionsResult.on(this).requestPermissions(permissions)
  .subscribe(result ->
      result.targetUI()
          .showPermissionStatus(result.permissions(), result.grantResults())
  );

void showPermissionStatus(String[] permissions, int[] grantResults) {
    boolean granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
    if (granted) {
		textView.setText(permissions[0] + " Granted")
	} else {
		textView.setText(permissions[0] + "Not granted")
	}
}
```

Please pay attention to the `targetUI()` method in the `Result` object emitted. 

This method returns a safety instance of the current `Activity`/`Fragment`. Because the original one may be recreated (due to configuration changes or some other system events) it would be unsafe calling it. 

Instead, you must call any method/variable of your `Activity`/`Fragment` from this instance encapsulated in the `Result` object.

For a complete example about RxPermissionsResult, refer to this [module](https://github.com/VictorAlbertos/RxPermissionsResult/tree/master/sample-rx1) if you are using RxJava1, or to [this](https://github.com/VictorAlbertos/RxPermissionsResult/tree/master/sample-rx2) other one if using RxJava2. 

## Another author's libraries:
* [ReactiveCache](https://github.com/VictorAlbertos/ReactiveCache): A reactive cache for Android and Java which honors the Observable chain.
* [RxActivityResult](https://github.com/VictorAlbertos/RxActivityResult): A reactive-tiny-badass-vindictive library to break with the OnActivityResult implementation as it breaks the observables chain. 
* [RxSocialConnect](https://github.com/VictorAlbertos/RxSocialConnect-Android): OAuth RxJava extension for Android.
