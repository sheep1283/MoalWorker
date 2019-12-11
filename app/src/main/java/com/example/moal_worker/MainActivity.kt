package com.example.moal_worker

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance().reference
    var myRef : DatabaseReference = FirebaseDatabase.getInstance().getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {     // 해시키 "nlZtw5R/zALjj6HQQ2UiopGbT7k="
            val info = this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val sign = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.e("test", "hash key : $sign")
                //Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("test", "hash key1 : $e")
        } catch (e: NoSuchAlgorithmException) {
            Log.e("test", "hash key2 : $e")
        }

        register.setOnClickListener { //등록하기 화면으로 이동
            val registerActivityintent = Intent(this, RegisterActivity::class.java)
            startActivity(registerActivityintent)
        }

        btn_complete.setOnClickListener { //로그인 버튼
            val userid = id_text.text.toString() //유저가 친 아뒤, 비번
            val userpw = pw_text.text.toString()
            if (userid.isEmpty() || userpw.isEmpty()){ //암것도 안치면
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(userid, userpw) //auth에서 제공하는 함수. auth에 등록되어있는 아뒤비번 맞으면 액티비티 이동
                .addOnCompleteListener {
                   if (it.isSuccessful){
                       val calenderActivityintent = Intent(this, CalenderActivity::class.java)
                       startActivity(calenderActivityintent)
                       return@addOnCompleteListener
                   }
                }
                .addOnFailureListener { //아뒤 비번 안맞으면 다시해
                    Toast.makeText(this, "이메일 또는 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show()
                    return@addOnFailureListener //다시 호출 가능
                }


        }
    }

}
