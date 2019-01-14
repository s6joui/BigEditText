# BigEditText
Android EditText implementation that uses Recyclerview to be able to handle huge amounts of text. This is pre-release software,  **not ready for production** use.

[![](https://jitpack.io/v/s6joui/BigEditText.svg)](https://jitpack.io/#s6joui/BigEditText)

## Download
Step 1. Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
  repositories {
    //...
    maven { url 'https://jitpack.io' }
  }
}
```
Step 2. Add the dependency

```groovy
compile 'com.rengwuxian.materialedittext:library:2.1.4'
```
## Usage
XML:
```xml
<tech.joeyck.bigedittext.BigEditText
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:text="Hello World!"
    app:textSize="24sp"
    app:textColor="#fff"/>
```
