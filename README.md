# AndroidUtility
This project contain all android related utilities
- Check internet connection 
- Get device mac address 
- Get device name 
-etc.

# Gradle
Step 1. add in your root build.gradle at the end of repositories:
```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
Step 2. Add the dependency
```groovy
dependencies {
	        compile 'com.github.ak-devt:AndroidUtility:1.0.0'
	}
  ```
