package com.group25.timebanking.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.group25.timebanking.models.Ads

class Database private constructor (context: Context?) {

    companion object {
        private var INSTANCE: Database? = null

        fun getInstance(context: Context?) =
            INSTANCE
                ?: Database(context)
                    .also { INSTANCE = it }
    }

    private val sharedPreferences = context?.getSharedPreferences("database", Context.MODE_PRIVATE)

    var adsList: ArrayList<Ads> = ArrayList()

    /*init {
        val json = sharedPreferences.getString(KEY_JSON_PREF, null)
        if (json == null) {
            // initialize your list contents for the first time
        } else {
            // convert your json and fill the data into your lists
        }
    }*/
    fun load(){
        val data = sharedPreferences?.getString("database", null)
        if (data != null){
            val sType = object : TypeToken<List<Ads>>() { }.type
            adsList = Gson().fromJson<ArrayList<Ads>>(data, sType)
        }
    }

    fun save(){
        with(sharedPreferences?.edit()) {
            this?.putString("database", Gson().toJson(adsList))
            this?.apply()
        }
    }
}
