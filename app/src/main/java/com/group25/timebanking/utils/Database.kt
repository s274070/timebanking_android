package com.group25.timebanking.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.group25.timebanking.models.Ads
import com.group25.timebanking.models.Users

class Database private constructor (context: Context?) {

    companion object {
        private var INSTANCE: Database? = null
        private const val COLLECTION_PATH_ADS = "Ads"
        private const val COLLECTION_PATH_USERS = "Users"

        fun getInstance(context: Context?) =
            INSTANCE
                ?: Database(context)
                    .also { INSTANCE = it }

    }

    //private val sharedPreferences = context?.getSharedPreferences("database", Context.MODE_PRIVATE)

    //private var adsList: ArrayList<Ads> = ArrayList()

    /*init {
        val json = sharedPreferences.getString(KEY_JSON_PREF, null)
        if (json == null) {
            // initialize your list contents for the first time
        } else {
            // convert your json and fill the data into your lists
        }
    }*/
    /*
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
     */

    fun getAdsList(onFinishCallback: (ArrayList<Ads>) -> Unit = {}) {
        var adsList: ArrayList<Ads> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_ADS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val ad = Ads(doc)
                    if (ad.createdUser != mAuth.currentUser!!.email!!) {
                        adsList.add(ad)
                    }
                }
                onFinishCallback(adsList)
            }
    }

    fun getMyAdsList(onFinishCallback: (ArrayList<Ads>) -> Unit = {}) {
        var adsList: ArrayList<Ads> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_ADS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val ad = Ads(doc)
                    if (ad.createdUser == mAuth.currentUser!!.email!!) {
                        adsList.add(ad)
                    }
                }
                onFinishCallback(adsList)
            }
    }

    fun getAdById(id: String, onFinishCallback: (Ads?) -> Unit = {}) {
        var ad: Ads? = null
        var mAuth = FirebaseAuth.getInstance()
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_ADS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val item = Ads(doc)
                    if (item.id == id && item.createdUser == mAuth.currentUser!!.email!!) {
                        ad = item
                        break
                    }
                }
                onFinishCallback(ad)
            }
    }

    fun saveAd(ad: Ads, onFinishCallback: () -> Unit = {}) {
        val db = FirebaseFirestore.getInstance().collection(COLLECTION_PATH_ADS)
        val doc: DocumentReference =
            if (ad.id.isEmpty()) { // new
                db.document()
            } else { // edit
                db.document(ad.id)
            }
        doc.set(ad).addOnSuccessListener {
            onFinishCallback()
        }
    }


    fun getUserByEmail(id: String, onFinishCallback: (Users?) -> Unit = {}) {
        var user: Users? = null
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_USERS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val item = Users(doc)
                    if (item.email == id) {
                        user = item
                        break
                    }
                }
                onFinishCallback(user)
            }
    }


    fun saveUser(user: Users, onFinishCallback: () -> Unit = {}) {
        val db = FirebaseFirestore.getInstance().collection(COLLECTION_PATH_USERS)
        val doc: DocumentReference =
            if (user.id.isEmpty()) { // new
                db.document()
            } else { // edit
                db.document(user.id)
            }
        doc.set(user).addOnSuccessListener {
            onFinishCallback()
        }
    }
}
