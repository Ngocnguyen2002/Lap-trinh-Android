package com.example.renthouse


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.view.View
import android.view.inputmethod.InputMethodManager

object SharedPreferencesUtil {
    private const val PREF_NAME = "MyPrefs"
    private val gson = Gson()

    fun saveData(context: Context, key: String, value: Any) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val json = gson.toJson(value)
        editor.putString(key, json)
        editor.apply()
    }

    fun Context.getValueFromSharedPreferences(key: String): UserInfo? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val jsonUserInfo = sharedPreferences.getString(key, null)
        return Gson().fromJson(jsonUserInfo, object : TypeToken<UserInfo?>() {}.type)
    }

    fun Context.removeValueFromSharedPreferences(key: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
    fun openKeyboard(view: View) {
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}
