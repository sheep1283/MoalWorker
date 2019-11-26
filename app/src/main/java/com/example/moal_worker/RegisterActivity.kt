package com.example.moal_worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerbutton.setOnClickListener {
            val emailIs = email_register.text.toString()
            val passwordIs = password_register.text.toString()
            //email은 이메일 형식으로, password는 길이 6 이상으로. 아니면 failure됨.
            if (emailIs.isEmpty() || passwordIs.isEmpty()){
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailIs, passwordIs)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    else{
                        val uid = FirebaseAuth.getInstance().uid ?:""
                        val name = name.text.toString()
                        val ref = FirebaseDatabase.getInstance().reference.child("users").child("/workers/$uid")
                        val setdisplayname = FirebaseAuth.getInstance()
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                        val userupdate = FirebaseAuth.getInstance().currentUser
                        val registedStore = FirebaseDatabase.getInstance().reference
                        val user = UserRegister(uid, name)

                        ref.setValue(user)
                        userupdate?.updateProfile(profileUpdate)

                        finish()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "외않돼지?.", Toast.LENGTH_SHORT).show()

                }

        }
    }

    class UserRegister(val uid : String, val name : String)
}
