# AFM
[ ![Download](https://api.bintray.com/packages/massivedisaster/maven/afm/images/download.svg) ](https://bintray.com/massivedisaster/maven/afm/_latestVersion)
[![Build Status](https://travis-ci.org/massivedisaster/AFM.svg?branch=master)](https://travis-ci.org/massivedisaster/AFM)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

An library to help android developer working easly with activities and fragments (Kotlin)

## Motivation

* Accelerate the process and abstract the logic of opening, adding and replacing fragments in an activity;
* Reduce the number of activities declared in the project;
* Get access to ```Activity.onBackPressed()``` inside of the fragments.
* Add animated transitions between fragments in an easy way;
* Easy way to work with shared elements;

<div align="center">
  <img src="extra/navigation.gif" width="280" alt="An animated GIF showing navigation flow" />
  <img src="extra/shared_elements.gif" width="280" alt="An animated GIF showing shared elements working" />
  <img src="extra/onbackpressed.gif" width="280" alt="An animated GIF showing onbackpressed working" />
  <br />
</div>

## Download

To use the AFM, add the compile dependency with the latest version.

### Gradle

Add the AFM to your `build.gradle`:
```gradle
dependencies {
    compile 'com.massivedisaster:afm:0.0.1'
}
```

### Maven

In the `pom.xml` file:
```xml
<dependency>
    <groupId>com.massivedisaster</groupId>
    <artifactId>afm</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Usage

### 1. Create your Activity

Create a new activity and extends the ```BaseActivity```.

```Kotlin
class ActivityPrimaryTheme : BaseActivity() {

    // The layout resource you want to find the FrameLayout.
    override fun layoutToInflate(): Int {
        return R.layout.activity_fullscreen
    }

    // The FrameLayout id you want to inject the fragments.
    override fun getContainerViewId(): Int {
        return R.id.frmContainer
    }
}

```

Create the layout to be used by your ```AbstractFragmentActivity```.
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/frmContainer"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

### 2. Opening, adding or replacing fragments in your AbstractFragmentActivity.

#### Open a new AbstractFragmentActivity with a fragment.
```Kotlin
ActivityCall.init(context, ActivityPrimaryTheme.class, FragmentExample.class)
                .build()
```

#### Add a new Fragment in the actual AbstractFragmentActivity.
```Kotlin
FragmentCall.init(activity, FragmentExample.class)
                .build()
```

#### Replace a new Fragment in the actual AbstractFragmentActivity.
```Kotlin
FragmentCall.init(activity as BaseActivity, FragmentExample.class)
                .setTransitionType(FragmentCall.TransitionType.REPLACE)
                .build()
```

### 3. Default Fragment

You can set a default fragment in you ```BaseActivity```.
An example, if your ```BaseActivity``` is started by an external intent you need to define a default fragment.

```Kotlin
class ActivityPrimaryTheme : BaseActivity() {
    ...

    override fun getDefaultFragment(): KClass<out Fragment>? {
        return FragmentSplash::class
    }
}
```

### 4. Fragment Transaction Animations.
When you add or replace fragments in the old way you can set a custom animations for the transactions. So, you can set custom animation in easly way using this library.

#### Single Transaction Animation

If you want to add a single animation only for one transaction you can do this:
```Kotlin
FragmentCall.init(activity as BaseActivity, FragmentAddReplace::class)
    .setTransitionType(FragmentCall.TransitionType.ADD)
    .setTransactionAnimation(object : TransactionAnimation {
        override val animationEnter: Int
            get() = R.anim.enter_from_right

        override val animationExit: Int
            get() = R.anim.exit_from_left

        override val animationPopEnter: Int
            get() = R.anim.pop_enter

        override val animationPopExit: Int
            get() = R.anim.pop_exit
    }).build()
```
**Attention:** This only works in transactions between fragments, i.e. ```add``` and ```replace```

#### Custom animation for all transactions.

If you want to add a custom animation for all transactions inside of a ```AbstractFragmentActivity``` you can override the follow methods:
```Kotlin
abstract class ActivityPrimaryTheme : BaseActivity() {

    ...

    override val animationEnter: Int
        get() = android.R.anim.fade_in

    override val animationExit: Int
        get() = android.R.anim.fade_out

    override val animationPopEnter: Int
        get() = android.R.anim.fade_in

    override val animationPopExit: Int
        get() = android.R.anim.fade_out

}
```

### 5. Shared Elements
If you want to make your app beautiful you need to put some cool animation on it!
Shared elements are introduce in API 21 and makes the transactions so great and sweet.
So, now it's very easy to share elements between fragments or activities.
Let's take a look:

**Activity A**
```Kotlin
...
.addSharedElement(view, "sharedElement")
...
.build()
```

**Activity B**
```Kotlin
ViewCompat.setTransitionName(view, "sharedElement")
```
or

```xml
<View
  ...
  android:transitionName="sharedElement" />
```

**Attention:** Shared elements doesn't work when you use ```add```!
Well if you remove the first fragment it's possible, i.e. a replace :)

### 6. Custom Intents
Sometimes you want to add more information to the ```Intent``` or set some flags. You can use the follow method to open a new ```BaseActivity```:

```Kotlin
.setFlags(flags)
```

### 7. Fragment#OnBackPressed
Allows to have back pressed events in `Fragments`.

```Kotlin
class FragmentOnBackPressed : Fragment(), OnBackPressedListener {

    ...

    @Override
    override fun onBackPressed(): Boolean {
      // Do what you want here! If you return true the activity will not process the OnBackPressed
    }

}
```

## Goodies

* You can pass a tag to be applied in the ```Fragment```.
* You can pass ```REQUEST_CODE``` to the ```startActivityForResult```.
* You can ```addToBackStack```.
* You can pass data between fragments using a ```Bundle```.
* You can get access to the original ```FragmentTransaction```.
* You can use ```DataBinding``` in your ```DataBindingBaseActivity```, all you need is override ```initializeDataBinding()``` and bind the view!

## Sample

Sample app can be found in the [sample module](sample). 
Alternatively, you can use [dryrun](https://github.com/cesarferreira/dryrun) to run the sample.

The Sample app don't require any configuration to interact.

## Contributing
[CONTRIBUTING](CONTRIBUTING.md)

## License
[MIT LICENSE](LICENSE.md)
