package com.example.moal_worker

import com.kakao.auth.*

//kakao login을 위한 kakao adapter
class KakaoSDKAdapter: KakaoAdapter() {
    //로그인 시 사용될 session의 옵션 설정 인터페이스
    override fun getSessionConfig(): ISessionConfig {
        return object : ISessionConfig {

            override fun getAuthTypes(): Array<AuthType> {
                return arrayOf(AuthType.KAKAO_ACCOUNT)
                //웹 뷰 다이얼로그를 통한 계정 연결 타입
                // KAKAO_TALK  : 카카오톡 로그인 타입
                //KAKAO_ACCOUNT
            } //로그인 시 인증 타입

            override fun isUsingWebviewTimer(): Boolean {
                return false
            }//로그인 웹뷰에서 타이머 설정 여부

            override fun getApprovalType(): ApprovalType? {
                return ApprovalType.INDIVIDUAL
            }//카카오 제휴 앱에서 사용되는 값

            override fun isSaveFormData(): Boolean {
                return true
            }//로그인 웹 뷰에서 email 입력 폼 데이터 저장여부

            override fun isSecureMode(): Boolean {
                return true
            }
        }
    }

    override fun getApplicationConfig(): IApplicationConfig {
        return IApplicationConfig {
            GlobalApplication.instance?.getGlobalApplicationContext()
        }//application이 가진 정보를 얻기 위한 인터페이스
    }
}