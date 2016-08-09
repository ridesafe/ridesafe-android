<p align="center"><img src="https://raw.githubusercontent.com/ridesafe/project/gh-pages/ridesafe_256.jpg"></p>

## RideSafe
[RideSafe](http://www.ridesafe.io) is an open source project which detects bikers' falls. This is possible thanks to intelligent algorithms and data collection.

Our smartphones have accelerometers to measure acceleration gyroscope forces of individuals, these data can be used to analyse the behaviour: when walking, running, biking and even falling !
The self learning algorithms are able to improve the detection of a fall by analysing such data.

Go to:
* [RideSafe Backend](#ridesafe-backend)
* [How it works](#how-it-works)
* [How to install](#how-to-install)
* [How to use](#how-to-use)
* [Contribute](#contribute)
* [Demo](#demo)
* [Who are we](#who-are-we)
* [Partners](#partners)
* [More](#more)
* [Contact](#contact)

## RideSafe Android lib
The RideSafe lib is in charge of getting accelerometer and gyroscope data (timestamp, acc_x², acc_y², acc_z², gyr_x, gyr_y, gyr_z) from your smartphone and then ship them to the [RideSafe Backend](https://github.com/ridesafe/ridesafe-backend).

## How it works
RideSafe needs accelerometer data to detect bikers' falls. Once the lib installed and configured on your app, this will listen for new accelerometer events, storing them in memory (very tiny footprint) before shipping them to the backend.

## How to install

Included dependencies are:
* [Kotlin](https://kotlinlang.org/)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Retrofit 2](http://square.github.io/retrofit/)
* [Okhttp 3](http://square.github.io/okhttp/)
* [GSON](https://github.com/google/gson)


Into your build.gradle, add this repository

```
repositories {
    ...
    maven { url "https://jitpack.io" }
}
```

Then add the Android RideSafe lib dependency
```
dependencies {
    compile 'com.github.ridesafe:ridesafe-android:0.1'
}
```

## How to use

It's straightforward:
```java
RideSafeBackend rsb = new RideSafeBackend("https://api.ridesafe.io")

RideSafe rs = RideSafe.Builder()
                .setContext(getContext())
                .setBackend(rsb)
                .setPushToBackendBatchSize(1000)
                .addReadyCallback(readyCallback)
                .addActivityObserver(activityObserver)
                .build()

# start recording rider activity
rs.startRecordingActivity()

# Push activity report
rsb.getDataForm().post(af)
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread()
    .subscribe()

# stop recording rider activity
rs.stopRecordingActivity

# end RideSafe session
rs.close()

```

## Contribute
There are many ways to contribute to RideSafe.
* **You are developer**: You have an idea to make RideSafe better ? You want to clean the code ?.. Just send us a Pull Request :)
* **You are data scientist**: Take a look at our activity recognition algorithms and tell us what do you think and how to improve them.
* **You are motorcycling professional**: More we can test, more accurate are algorithms and fall detection. Contact us to see how you can contribute !

## Demo
RideSafe is used into our [Nousmotards](https://play.google.com/store/apps/details?id=com.nousmotards.android) app.

## Who are we ?
RideSafe has been launched by [Nousmotards](https://www.nousmotards.com).
It is a service platform for bikers, created by [4 engineers and bikers](http://blog.nousmotards.com/2015/04/24/ouverture-du-blog-nousmotards/):
* [Romaric Philogène](https://fr.linkedin.com/in/romaricphilogene)
* [Rémi Demol](https://www.linkedin.com/in/demolremi/fr)
* [Pierre Mavro](https://fr.linkedin.com/in/pmavro/fr)
* [Ludwine Probst](https://www.linkedin.com/in/ludwineprobst)

We are creating a set of services tailored exclusively to the world of motorcycling!
Nousmotards app is available on Mobile ([Android](https://play.google.com/store/apps/details?id=com.nousmotards.android), iOS) and on your [browser](https://www.nousmotards.com).

## Partners
* [FFMC - Fédération Française des Motards en Colère](http://ffmc.fr/)
* [Motomag](http://www.motomag.com/)

We are looking for worldwide partners specialised in motorbike and/or technology.

## More
The "Activity Recognition" project is based on the work of [Ludwine Probst](https://github.com/nivdul) and [Amira Lakhal](https://github.com/MiraLak), which detects one type of activity from the accelerometer integrated in smartphones and a self-learning algorithm called [machine learning](https://en.wikipedia.org/wiki/Machine_learning).

The data and algorithms are available under the Apache license, this means that the changes and improvements made will be communicated to the community.
Commercial use is unrestricted.

## Contact

Contact us at [contact@ridesafe.io](mailto:contact@ridesafe.io) or [contact@nousmotards.com](mailto:contact@nousmotards.com)

Follow us: [Twitter](https://twitter.com/Nousmotards) [Facebook](https://www.facebook.com/nousmotardsapp)
