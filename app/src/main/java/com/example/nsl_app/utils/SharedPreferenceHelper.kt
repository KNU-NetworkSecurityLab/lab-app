package com.example.nsl_app.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


object SharedPreferenceHelper {
    private val PREF_APP = "PREF_NSL"

    private const val authorization = "SP_AUTHORIZATION" // String
    private const val autoLogin = "SP_AUTO_LOGIN" // boolean
    private const val autoLoginID = "SP_AUTO_LOGIN_ID" // String
    private const val autoLoginPassword = "SP_AUTO_LOGIN_PASSWORD" // String


    // Authorization
    fun setAuthorizationToken(context: Context, token: String) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(authorization, token)
        editor.apply()
    }

    fun getAuthorizationToken(context: Context): String? {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getString(authorization, "")
    }


    // 자동 로그인 체크 유무
    fun setAutoLoginEnable(context: Context, isEnable: Boolean) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(autoLogin, isEnable)
        editor.apply()
    }

    fun isAutoLoginEnable(context: Context): Boolean {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getBoolean(autoLogin, false)
    }

    // 자동 로그인 아이디 저장
    fun setAutoLoginID(context: Context, ID: String?) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(autoLoginID, ID)
        editor.apply()
    }

    fun getAutoLoginID(context: Context): String? {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getString(autoLoginID, null)
    }

    // 자동 로그인 패스워드 저장
    fun setAutoLoginPassword(context: Context, pw: String?) {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(autoLoginPassword, pw)
        editor.apply()
    }

    fun getAutoLoginPassword(context: Context): String? {
        val pref: SharedPreferences = context.getSharedPreferences(PREF_APP, MODE_PRIVATE)
        return pref.getString(autoLoginPassword, null)
    }
}