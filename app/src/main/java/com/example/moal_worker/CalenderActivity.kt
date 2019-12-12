package com.example.moal_worker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.calender_activity.*
import kotlinx.android.synthetic.main.fragment_second.*

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
                        .beginTransaction() //전환 시작
                        .replace(R.id.frame_layout,homeFragment)//해당 fragment로 전환
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN) //전환 애니메이션 선택
                        .commit() //커밋


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                txtValue.text = result.contents
            } else {
                txtValue.text = "scan failed"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}