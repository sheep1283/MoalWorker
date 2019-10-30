package com.example.moal_worker

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.calender_activity.*

class CalenderActivity : AppCompatActivity(), BottomSheet.BottomSheetListener {
    override fun onOptionClick(text: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender_activity)

            button_open_button_sheet.setOnClickListener {
            val bottomSheet = BottomSheet()
            bottomSheet.show(supportFragmentManager, "BottomSheet")

        }


    }
}