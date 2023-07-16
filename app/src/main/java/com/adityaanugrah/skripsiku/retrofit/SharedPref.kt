package com.adityaanugrah.skripsiku.retrofit

import android.content.Context

class SharedPref(private val context: Context) {
    private val pref = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    var isLogin: Boolean
        get() {
            return pref.getBoolean("login", false)
        }
        set(value) {
            pref.edit().putBoolean("login", value).apply()
        }

    var favorit: MutableSet<String>
        get() {
            return pref.getStringSet("f", mutableSetOf()) ?: mutableSetOf()
        }
        set(value) {
            pref.edit().putStringSet("f", value).apply()
        }
}