package com.group25.timebanking.utils

import android.content.Context
import android.content.SharedPreferences
import com.group25.timebanking.R

class SharedPrefs {
    fun get(context:Context, key:String): SharedPreferences? {
        return context.getSharedPreferences(
            key,
            Context.MODE_PRIVATE
        )
    }

    fun getProfileJson(context:Context): String? {
        return get(context,context.getString(R.string.preference_file_key))?.getString("profile", null)
    }
}