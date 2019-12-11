package com.example.moal_worker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.StringSet.*
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger
import kotlinx.android.synthetic.main.activity_register.*
import java.util.ArrayList

class RegisterActivity : AppCompatActivity() {
    private var callback: SessionCallback = SessionCallback()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Session.getCurrentSession().addCallback(callback)

        registerbutton.setOnClickListener {
            val emailIs = email_register.text.toString()
            val passwordIs = password_register.text.toString()
            val name = name.text.toString()
            //email은 이메일 형식으로, password는 길이 6 이상으로. 아니면 failure됨.
            if (emailIs.isEmpty() || passwordIs.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "이메일, 비밀번호, 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailIs, passwordIs) //아뒤 비번 형식 맞으면 생성해주는 함수. auth 제공함수
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    else {
                        val uid = FirebaseAuth.getInstance().uid ?: "" //null처리 근데 널 될일 읎다 구글이 잘못되지않는이상 위에서 이미 auth에 문제 없는거 확인(.addOnCompleteListener)
                        val ref = FirebaseDatabase.getInstance().reference.child("users")
                            .child("/workers/$uid")
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build() //displayname은 auth에서 제공하여 기본 호출 가능. 이걸 name으로 변경해주겟슴
                        val userupdate = FirebaseAuth.getInstance().currentUser //생성했으니 현재 유저정보로 넘어올 수 있음
                        val user = UserRegister(uid, name)

                        ref.setValue(user) //uid와 name을 auth뿐만아니라 realtimeDB에도 저장. 해당 uid밑에 uid, name생성
                        userupdate?.updateProfile(profileUpdate) //displayname 변경한거 적용

                        finish() //이제 로그인으로 넘어감
                    }
                }
                .addOnFailureListener { //비번6자리 이하, 이메일 형식 틀림 등등의 이유로 등록 실패시
                    Toast.makeText(this, "외않돼지?.", Toast.LENGTH_SHORT).show()

                }

        }
    }

    class UserRegister(val uid: String, val name: String)

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.d("TAG", "session get current session")
            return
        }

        super.onActivityResult(requestCode, resultCode, data)



    }

    private class SessionCallback : ISessionCallback { //private
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("TAG", "Session Call back :: onSessionOpenFailed: ${exception?.message}")
        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {
                override fun onFailure(errorResult: ErrorResult?) {
                    Log.d("TAG", "Session Call back :: on failed ${errorResult?.errorMessage}")
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.e(
                        "TAG",
                        "Session Call back :: onSessionClosed ${errorResult?.errorMessage}"
                    )
                }

                override fun onSuccess(result: MeV2Response?) {//웹뷰가 켜지고 login에 성공 시 작동
                    checkNotNull(result) { "session response null" }
                    Log.d("jooankim", "onSuccess is here")

                    Log.d("kakaodebug", "userd : " + result.id)
                    Log.d("kakaodebug", "nickname : " + result.nickname)
                    Log.d("kakaodebug", "email: " + result.kakaoAccount.email)
                    Log.d("kakaodebug", "birthday: " + result.kakaoAccount.birthday)

                    val emailIs = result.kakaoAccount.email.toString()
                    val passwordIs = result.id.toString()
                    val name = result.nickname.toString()
                    //로그인으로부터 가져온 email 등 인적 사항을 firebase로 넘기는 함수
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailIs, passwordIs)
                        .addOnCompleteListener {
                            if (!it.isSuccessful) return@addOnCompleteListener
                            else {
                                Log.d("kakaodebug", "Error while register")
                                val uid = FirebaseAuth.getInstance().uid ?: ""
                                val ref = FirebaseDatabase.getInstance().reference.child("users")
                                    .child("/workers/$uid")
                                val setdisplayname = FirebaseAuth.getInstance()
                                val profileUpdate = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()
                                val userupdate = FirebaseAuth.getInstance().currentUser
                                val registedStore = FirebaseDatabase.getInstance().reference
                                val user = UserRegister(uid, name)
                                ref.setValue(user)
                                userupdate?.updateProfile(profileUpdate)
                            }
                        }
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailIs,passwordIs).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(RegisterActivity(), CalenderActivity::class.java)

                        }
                    }
                }

                private fun requestMe() { //회원 정보를 가져오는 함수
                    val keys: MutableList<String> = ArrayList()
                    keys.add("properties.nickname")
                    keys.add("properties.profile_image")
                    keys.add("kakao_account.email")
                    UserManagement.getInstance().me(keys, object : MeV2ResponseCallback() {
                        override fun onFailure(errorResult: ErrorResult) {
                            val message = "failed to get user info. msg=$errorResult"
                            Logger.d(message)
                        }

                        override fun onSessionClosed(errorResult: ErrorResult) {
                            RegisterActivity()
                        }

                        override fun onSuccess(response: MeV2Response) {
                            Logger.d("user id : " + response.id)
                            Logger.d("email: " + response.kakaoAccount.email)

                        }

                    })

                }
            })
        }
    }


}