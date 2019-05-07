package com.idhub.shop.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SPUtil(var context: Context) {

    companion object {
        const val NAME = "NAME"
        const val MOBILE = "MOBILE"
        const val EMAIL = "EMAIL"
        const val ADDRESS = "ADDRESS"
        const val PWD = "PWD"
        const val WALLET_PATH = "WALLET_PATH"
        const val AVATAR_URI = "AVATAR_URI"
    }

    private var preference: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private var editor: SharedPreferences.Editor

    init {
        editor = preference.edit()
    }

    fun putString(name: String, value: String) {
        editor.putString(name, value).apply()
    }

    fun getString(name: String): String {
        return preference.getString(name, "")
    }

    fun putInt(name: String, value: Int) {
        editor.putInt(name, value).apply()
    }

    fun getInt(name: String): Int {
        return preference.getInt(name, -1)
    }

    fun putBoolean(name: String, value: Boolean) {
        editor.putBoolean(name, value).apply()
    }

    fun getBoolean(name: String): Boolean {
        return preference.getBoolean(name, false)
    }

}