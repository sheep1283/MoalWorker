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
    val dirFire : DatabaseReference = myRef.child("users")
    var number = 1

    private fun writeNewUser(userId : String, email : String, password : String){
        val user = User(email, password)
        database.child("users").child("jini").child("storelist").setValue("노랑통닭 홍대점")

    }

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

        register.setOnClickListener {
            val registerActivityintent = Intent(this, RegisterActivity::class.java)
            startActivity(registerActivityintent)
        }

        btn_complete.setOnClickListener {
            val userid = id_text.text.toString()
            val userpw = pw_text.text.toString()
            if (userid.isEmpty() || userpw.isEmpty()){
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(userid, userpw)
                .addOnCompleteListener {
                   if (it.isSuccessful){
                       val calenderActivityintent = Intent(this, CalenderActivity::class.java)
                       startActivity(calenderActivityintent)
                   }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "이메일 또는 비밀번호를 다시 입력하세요.", Toast.LENGTH_SHORT).show()
                }


        }
    }

}
