# AndroidUtility
This project contain all android related utilities
- NetworkUtils 
- DeviceUtils
- ShellUtils
- CloseUtils (For resources like IO)
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
	        compile 'com.github.ak-devt:AndroidUtility:1.0.2'
	}
  ```
Step 3. Add the in your Application class
```groovy
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
	...
        com.ak_devt.androidutility.Utils.init(this);
    }
}
  ```
  # Example
  ```groovy
  //Check network connection
        com.ak_devt.androidutility.NetworkUtils.isNetworkConnected();
        //Check network operator name
        com.ak_devt.androidutility.NetworkUtils.getNetworkOperatorName();
        //Check network type
        com.ak_devt.androidutility.NetworkUtils.getNetworkType();

        //Get Android id
        com.ak_devt.androidutility.DeviceUtils.getAndroidID();
	
	//etc.
  ```
