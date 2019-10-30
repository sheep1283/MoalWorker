package com.example.moal_worker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.calender_activity.*

class CalenderActivity : AppCompatActivity(), BottomSheet.BottomSheetListener {
    override fun onOptionClick(text: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender_activity)

            button_open_bottom_sheet.setOnClickListener {
            val bottomSheet = BottomSheet()
            bottomSheet.show(supportFragmentManager, "BottomSheet")

        }


    }
}