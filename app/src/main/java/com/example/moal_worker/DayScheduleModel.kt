package com.example.moal_worker

import android.graphics.Color

class DayScheduleModel(Name: String?=null, fill: String?=null, var color: Int =Color.argb(0,1,1,1)){
//calendar에 들어가는 xml의 구성요소 내용을 담는 class
    var Name = Name
    var fill = fill
    /*
    <TextView
        android:id="@+id/gridfill"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"

        android:textColor="@android:color/black"
        android:textSize="12sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toBottomOf="@id/gridName"
        tools:text="Subtitle" />




        app:layout_constraintBottom_toTopOf="@id/gridfill" ->위에 xml

    * */


}