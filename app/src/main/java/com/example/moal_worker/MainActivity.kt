package com.example.moal_worker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        btn_complete.setOnClickListener {
            writeNewUser(userId = "userNo-"+number ,email = id_text.text.toString(), password = pw_text.toString())
            number++
            val calenderActivityintent = Intent(this, CalenderActivity::class.java)

            startActivity(calenderActivityintent)
        }
    }

}
