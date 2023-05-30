package online.dailyq

import android.content.Context
import android.content.SharedPreferences

object Settings {
    lateinit var prefs: SharedPreferences

    fun init(context: Context){
        prefs = context.getSharedPreferences("settings",Context.MODE_PRIVATE)
    }

    fun clear(){
        AuthManager.prefs.edit().clear().apply()
    }
}