Smart Places
======================

Welcome to Smart Places repository.

I am a Master student from Computer Science and in my thesis I want to revolutionize the way developers create applications using iBeacons.

And you may ask, why the hell do you want that?
Check the following link:
* [7 cool uses](http://www.infoworld.com/article/2606357/mobile-technology/160948-7-cool-uses-of-beacons-you-may-not-expect.html)

What is iBeacon?
[iBeacon guide](http://www.ibeacon.com/what-is-ibeacon-a-guide-to-beacons/)

For more information check the
[Wiki](https://github.com/samfcmc/master-thesis/wiki)

## Deadline
A count down timer, telling me, how much time is left to deliver this thesis.
[It's the final countdown](http://tinyurl.com/om96bfz)

## Requirements:
* An Android device
* Android Studio
* BLE Beacons or a Bluetooth USB dongle (if you use this you need a script to make your dongle act like an ibeacon.
* An account in [Parse.com](https://parse.com/). That's right, we didn't implement the backend ourselves ;)

## Use a Bluetooth USB dongle to simulate an ibeacon
### Linux
In linux you can use the following python script:
* [linux-ibeacon](https://github.com/samfcmc/linux-ibeacon)

### Mac OS
There are solutions for Mavericks, but if you are using Yosemite, this can be tricky.
If you have XCode installed, you can use the following solution:
* [yosemite-as-an-ibeacon-swift](https://updatemycode.com/2014/11/29/yosemite-as-an-ibeacon-swift/)

## First steps:
* Import the Android project (SmartPlacesClientApp) to Android studio
* Copy the parse json template file to a new one and change appId, clientKey and facebookAppId according to what you have for your app in Parse.com
```
cp SmartPlacesClientApp/app/src/main/res/raw/parse_template.json SmartPlacesClientApp/app/src/main/res/raw/parse.json
```

TODO: This should be easier...
* Populate your Parse.com backend with some data

TODO: This should be also easier to do... With some script or some s*** like that

* Connect your Android device through USB (Android emulator does not emulate bluetooth, that's why you need a real device)
* In the Android Studio, press <kbd>Shift</kbd> + <kbd>F10</kbd> to run the app
* Have fun ;)
