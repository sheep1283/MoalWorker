package com.example.moal_worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class WorkingScheduleActivity : AppCompatActivity() {

    lateinit var store1fragment: Store1Fragment
    lateinit var store2fragment: Store2Fragment
    lateinit var store3fragment: Store3Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_working_schedule)
        val bottomnavigation : BottomNavigationView = findViewById(R.id.btm_storelist)


        bottomnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.store1 -> {

                    store1fragment = Store1Fragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, store1fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()


                }
                R.id.store2 -> {

                    store2fragment = Store2Fragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, store2fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }
                R.id.store3 -> {

                    store3fragment = Store3Fragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, store3fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }


            }
            true
        }
    }
}
