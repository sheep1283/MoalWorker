package com.example.moal_worker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

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
