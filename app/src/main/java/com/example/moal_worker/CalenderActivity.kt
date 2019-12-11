package com.example.moal_worker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.calender_activity.*

class CalenderActivity : AppCompatActivity(){
    lateinit var homeFragment: HomeFragment
    lateinit var secondFragment: SecondFragment
    lateinit var thirdFragment: ThirdFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender_activity)

        val bottomnavigation : BottomNavigationView = btm_nav
        val user = FirebaseAuth.getInstance().currentUser
        Toast.makeText(this, user!!.displayName.toString(), Toast.LENGTH_LONG).show() //로그인된거 확인용


        homeFragment = HomeFragment() //시작화면 홈프래그먼트로 해주게
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout,homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()


        bottomnavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.home->{

                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout,homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()


                }
                R.id.second->{

                    secondFragment = SecondFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout,secondFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }
                R.id.third->{

                    thirdFragment = ThirdFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout,thirdFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()

                }
            }
            true
        }






    }
}