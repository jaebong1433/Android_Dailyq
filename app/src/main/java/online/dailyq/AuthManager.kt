package online.dailyq

import android.content.Context
import android.content.SharedPreferences

object AuthManager {
    const val UID = "uid"
    const val ACCESS_TOKEN = "access_token"
    const val REFRESH_TOKEN = "refresh_token"

    //앱이 종료된 후에도 보관할 수 있도록 SharedPreferences에 보관
    lateinit var prefs: SharedPreferences

    //SharedPreferences를 최초로 불러올 때 Context가 필요하기 때문에
    //사용하기 전에 최초 1회 init()메서드를 호출 context를 전달하여 초기화
    fun init(context: Context){
        prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }

    //AuthManager의 멤버변수에 getter와 setter를 정의해
    //사용자가 직접 SharedPreferences를 호출하다 실수하는 것을 방지함
    var uid: String?
    get() = prefs.getString(UID, null)
    set(value){
        prefs.edit().putString(UID, value).apply()
    }

    var accessToken: String?
    get() = prefs.getString(ACCESS_TOKEN, null)
    set(value){
        prefs.edit().putString(ACCESS_TOKEN, value).apply()
    }

    var refreshToken: String?
    get() = prefs.getString(REFRESH_TOKEN, null)
    set(value) {
        prefs.edit().putString(REFRESH_TOKEN, value).apply()
    }

    fun clear(){
        prefs.edit().clear().apply()
    }
}