package com.example.moal_worker

import android.app.Application
import com.kakao.auth.KakaoSDK

//kakao login을 위한 application
//이미지를 캐시를 앱 수준에서 관리하기 위한 어플리케이션 객체이다.
class GlobalApplication : Application() {
    override fun onCreate() {
        //이미지 로더, 이미지 캐시, 요청 큐를 초기화한다.
        super.onCreate()

        instance = this
        KakaoSDK.init(KakaoSDKAdapter())
    }

    override fun onTerminate() {
        //어플리케이션 종료시 singleton 어플리케이션 객체 초기
        super.onTerminate()
        instance = null
    }

    fun getGlobalApplicationContext(): GlobalApplication {
        //singleton 어플리케이션 객체를 얻는다.
        checkNotNull(instance) { "this application does not inherit com.kakao.GlobalApplication" }
        return instance!!
    }

    companion object {
        var instance: GlobalApplication? = null
    }
}