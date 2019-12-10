package com.example.moal_worker

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
import com.kakao.usermgmt.StringSet.nickname
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.Utility.getPackageInfo
import com.kakao.util.helper.log.Logger
import kotlinx.android.synthetic.main.activity_register.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
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

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailIs, passwordIs)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    else {
                        val uid = FirebaseAuth.getInstance().uid ?: ""
                        val ref = FirebaseDatabase.getInstance().reference.child("users")
                            .child("/workers/$uid")
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                        val userupdate = FirebaseAuth.getInstance().currentUser
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

    private class SessionCallback : ISessionCallback {
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

                override fun onSuccess(result: MeV2Response?) {
                    checkNotNull(result) { "session response null" }

                    // register or login
                }
                private fun requestMe() {
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

                            //CalenderActivity()
                        }

                        override fun onSuccess(response: MeV2Response) {
                            Logger.d("user id : " + response.id)
                            Logger.d("email: " + response.kakaoAccount.email)

                            Log.d("what","userd : " + response.id)
                            Log.d("TAG","nickname : " + response.nickname)
                            Log.d("TAG","email: " + response.kakaoAccount.email)

                            FirebaseAuth.getInstance().signInWithEmailAndPassword(response.kakaoAccount.email, response.nickname)
                                .addOnCompleteListener {
                                    if (it.isSuccessful){
                                        val calActivityintent = Intent(RegisterActivity(), CalenderActivity::class.java)
                                        startActivity(RegisterActivity(),calActivityintent , null)
                                    }
                                }


                            RegisterActivity()
                        }

                        /* override fun onNotSignedUp() {
                             showSignup()
                         }*/
                    }

                    )
                }

            })

        }

    }
    fun getHashKey(context: Context): String? {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                val packageInfo = getPackageInfo(context, PackageManager.GET_SIGNING_CERTIFICATES)
                val signatures = packageInfo.signingInfo.apkContentsSigners
                val md = MessageDigest.getInstance("SHA")
                for (signature in signatures) {
                    md.update(signature.toByteArray())
                    return String(Base64.encode(md.digest(), NO_WRAP))
                }
            } else {
                val packageInfo =
                    getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null

                for (signature in packageInfo!!.signatures) {
                    try {
                        val md = MessageDigest.getInstance("SHA")
                        md.update(signature.toByteArray())
                        return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                    } catch (e: NoSuchAlgorithmException) {
                        Log.w("TAG",
                            "Unable to get MessageDigest. signature=$signature"
                        )
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return null
    }


}