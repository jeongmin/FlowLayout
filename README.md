# FlowLayout
Android viewgroup which automatically breaks line when there is not enough place to put its child view on a current line. The height of each line depends on the most heightest child.(Margins of view are also taken account into calculation.) 

And you can control the alignment of children in each line by using **line_gravity** attribute. 

## Supporting version
From Android 4.0.3, 4.0.4(API level 15), ICE_CREAM_SANDWICH_MR1

## Gradle
Add the following to your build.gradle:

```
dependencies {
    implementation 'com.jeongmin.library:flowlayout:1.0.0'
}
```

## Usage
Here is an example.

```
<com.jeongmin.flowlayout.FlowLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@android:color/darker_gray"
    app:line_gravity="center"
    >
    <TextView
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:background="#ffccff"
        android:text="#ffccff"
        android:gravity="center"
        />

    <TextView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:background="#ccffcc"
        android:text="#ccffcc"
        android:gravity="center"
        />

    <TextView
        android:layout_width="100dp"
        android:layout_height="50dp"
        android:background="#ffffcc"
        android:layout_marginHorizontal="10dp"
        android:text="#ffffcc"
        android:gravity="center"
        />

    <TextView
        android:layout_width="100dp"
        android:layout_height="150dp"
        android:background="#ccffff"
        android:text="#ccffff"
        android:gravity="center"
        />

    <TextView
        android:layout_width="200dp"
        android:layout_height="30dp"
        android:layout_margin="20dp"
        android:background="#ffffff"
        android:text="#ffffff"
        android:gravity="center"
        />

</com.jeongmin.flowlayout.FlowLayout>

```
## Attributes
### line_gravity

Constant          | Value | Description
-------------------|-------|-----------
center            | 1     | Place object to the center of its line.            
center_vertical   | 2 | Place object to the vertical center of its container.  
center_horizontal | 3 | Place object to the horizontal center of its container.

center | center_vertical | center_horizontal
-------|-----------------|--------------------
![center](https://github.com/jeongmin/FlowLayout/blob/master/screenshot/line_gravity_center.png) | ![center_vertical](https://github.com/jeongmin/FlowLayout/blob/master/screenshot/line_gravity_center_vertical.png) | ![center_horizontal](https://github.com/jeongmin/FlowLayout/blob/master/screenshot/line_gravity_center_horizontal.png)


## License
This project covered by Apache License 2.0. See [LICENSE](LICENSE) for the full text.
