# BigEditText

[![](https://jitpack.io/v/s6joui/BigEditText.svg)](https://jitpack.io/#s6joui/BigEditText)

Android EditText implementation that uses Recyclerview to be able to handle huge amounts of text. This is pre-release software,  **not ready for production** use.

<p align="center"> 
<img src="https://i.imgur.com/6qrK6Qb.gif">
</p>

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
implementation 'com.github.s6joui:BigEditText:1.1.0-alpha'
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
