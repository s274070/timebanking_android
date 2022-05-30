package com.group25.timebanking.utils

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.group25.timebanking.models.Ad
import com.group25.timebanking.models.Request
import com.group25.timebanking.models.User
import com.group25.timebanking.models.UserRating

class Database private constructor (context: Context?) {

    companion object {
        private var INSTANCE: Database? = null
        private const val TAG = "Database"
        private const val COLLECTION_PATH_ADS = "Ads"
        private const val COLLECTION_PATH_REQUESTS = "Requests"
        private const val COLLECTION_PATH_USERS = "Users"

        fun getInstance(context: Context?) =
            INSTANCE
                ?: Database(context)
                    .also { INSTANCE = it }

    }

    fun getAdsSkillsList2(onFinishCallback: (List<String>) -> Unit = {}) {
        var adsSkillsList: ArrayList<String> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_ADS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val ad = Ad(doc)
                    if (ad.createdUser != mAuth.currentUser!!.email!!) {
                        adsSkillsList.add(ad.title.uppercase())
                    }
                }
                onFinishCallback(adsSkillsList.distinct())
            }
    }

    fun getAdsSkillsList(onFinishCallback: (List<String>) -> Unit = {}) {
        var adsSkillsList: ArrayList<String> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_ADS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val ad = Ad(doc)
                    if (ad.createdUser != mAuth.currentUser!!.email!!) {
                        adsSkillsList.add(ad.title.uppercase())
                    }
                }
                onFinishCallback(adsSkillsList.distinct())
            }
    }

    fun getAdsList(skill:String, onFinishCallback: (ArrayList<Ad>) -> Unit = {}) {
        var adList: ArrayList<Ad> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_ADS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val ad = Ad(doc)
                    if (ad.title.equals(skill, ignoreCase = true) && ad.createdUser != mAuth.currentUser!!.email!!) {
                        adList.add(ad)
                    }
                }
                onFinishCallback(adList)
            }
    }

    fun getMyAdsList(onFinishCallback: (ArrayList<Ad>) -> Unit = {}) {
        var adList: ArrayList<Ad> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_ADS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val ad = Ad(doc)
                    if (ad.createdUser == mAuth.currentUser!!.email!!) {
                        adList.add(ad)
                    }
                }
                onFinishCallback(adList)
            }
    }

    fun getAdById(id: String, onFinishCallback: (Ad?) -> Unit = {}) {
        val db = FirebaseFirestore.getInstance()
        val query = db.collection(COLLECTION_PATH_ADS)
            .whereEqualTo(FieldPath.documentId(), id)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if(task.result?.documents.isNullOrEmpty())
                    Log.e(TAG, "No document found, getAdById($id)")
                else{
                    val item = Ad(task.result?.documents!![0])
                    onFinishCallback(item)
                }

            } else {
                Log.e(TAG, "Error getting documents, getAdById($id)", task.exception)

            }
        }
    }

    fun saveAd(ad: Ad, onFinishCallback: () -> Unit = {}) {
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


    fun getUserByEmail(id: String, onFinishCallback: (User?) -> Unit = {}) {
        var user: User? = null
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_USERS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val item = User(doc)
                    if (item.email == id) {
                        user = item
                        break
                    }
                }
                onFinishCallback(user)
            }
    }


    fun getOrCreateUserByEmail(email: String, onFinishCallback: (User?) -> Unit = {}) {
        var user: User? = null
        val db = FirebaseFirestore.getInstance().collection(COLLECTION_PATH_USERS)
        db.addSnapshotListener { value, error ->
            if (error != null) throw error
            for (doc in value!!) {
                val item = User(doc)
                if (item.email.equals(email, ignoreCase = true)) {
                    user = item
                    break
                }
            }
            if (user == null) {
                user = User(
                    "",
                    email,
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    ""
                )
                val doc: DocumentReference = db.document()
                doc.set(user!!).addOnSuccessListener {
                    onFinishCallback(user!!)
                }
            } else
                onFinishCallback(user)
        }
    }


    fun saveUser(user: User, onFinishCallback: () -> Unit = {}) {
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

    fun getUserRequestList(onFinishCallback: (ArrayList<Request>) -> Unit = {}) {
        var requestList: ArrayList<Request> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val query = db.collection(COLLECTION_PATH_REQUESTS)
            .whereEqualTo("AdUser", mAuth.currentUser!!.email!!)
            .whereEqualTo("Status", 0)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (doc in task.result?.documents!!) {
                    val req = Request(doc)
                    requestList.add(req)
                }
                onFinishCallback(requestList)

            } else {
                Log.e(TAG, "Error getting documents, getUserRequestList", task.exception)

            }
        }
    }

    fun saveUserRequest(request: Request, onFinishCallback: () -> Unit = {}) {
        val db = FirebaseFirestore.getInstance().collection(COLLECTION_PATH_REQUESTS)
        val doc: DocumentReference = db.document()
        doc.set(request).addOnSuccessListener {
            onFinishCallback()
        }
    }

    fun getUserRatings(onFinishCallback: (ArrayList<UserRating>) -> Unit = {}) {
        var ratingList: ArrayList<UserRating> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val query = db.collection(COLLECTION_PATH_REQUESTS)
            .whereEqualTo("UserId", mAuth.currentUser!!.email!!)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (doc in task.result?.documents!!) {
                    val rate = UserRating(doc)
                    ratingList.add(rate)
                }
                onFinishCallback(ratingList)
            } else {
                Log.e(TAG, "Error getting documents, getUserRatings", task.exception)

            }
        }
    }
}
