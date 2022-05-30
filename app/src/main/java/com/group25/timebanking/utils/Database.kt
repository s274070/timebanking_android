package com.group25.timebanking.utils

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.group25.timebanking.models.*

class Database private constructor (context: Context?) {

    companion object {
        private var INSTANCE: Database? = null
        private const val TAG = "Database"
        private const val COLLECTION_PATH_ADS = "Ads"
        private const val COLLECTION_PATH_REQUESTS = "Requests"
        private const val COLLECTION_PATH_USERS = "Users"
        private const val COLLECTION_PATH_SESSIONS = "Sessions"
        private const val COLLECTION_PATH_USERRATINGS = "UserRatings"

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

    fun getAdsList(skill: String, onFinishCallback: (ArrayList<Ad>) -> Unit = {}) {
        var adList: ArrayList<Ad> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        FirebaseFirestore.getInstance().collection(COLLECTION_PATH_ADS)
            .addSnapshotListener { value, error ->
                if (error != null) throw error
                for (doc in value!!) {
                    val ad = Ad(doc)
                    if (ad.title.equals(
                            skill,
                            ignoreCase = true
                        ) && ad.createdUser != mAuth.currentUser!!.email!!
                    ) {
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
                if (task.result?.documents.isNullOrEmpty())
                    Log.e(TAG, "No document found, getAdById($id)")
                else {
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
        if (ad.id.isEmpty()) { // new
            ad.id = doc.id
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
                var mAuth = FirebaseAuth.getInstance()
                val doc: DocumentReference = db.document()
                user = User(
                    doc.id,
                    email,
                    mAuth.currentUser!!.displayName ?: "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    ""
                )
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
        if (user.id.isEmpty()) { // new
            user.id = doc.id
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
            .whereEqualTo("adUser", mAuth.currentUser!!.email!!)
            .whereEqualTo("status", 0)
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

        val db = FirebaseFirestore.getInstance()

        val queryAttendingUser = db.collection(COLLECTION_PATH_USERS)
            .whereEqualTo("email", request.RequestUser)
        queryAttendingUser.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if(!task.result?.documents.isNullOrEmpty()) {
                    val user = User(task.result?.documents!![0])
                    request.RequestUserName = user.fullName
                    val doc: DocumentReference = db.collection(COLLECTION_PATH_REQUESTS).document()
                    request.id = doc.id
                    doc.set(request).addOnSuccessListener {
                        onFinishCallback()
                    }
                }else
                    Log.e(TAG, "Document not found, saveUserRequest - AttendingUser", task.exception)
            } else {
                Log.e(TAG, "Error getting documents, saveUserRequest - AttendingUser", task.exception)

            }
        }
    }

    fun acceptRequest(request: Request, onFinishCallback: () -> Unit = {}) {
        var mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        var OrganiserUser: String =""
        var OrganiserUserName: String=""
        var AttendingUser: String=""
        var AttendingUserName: String=""

        // Accept
        request.Status = 1
        val docAccept: DocumentReference = db.collection(COLLECTION_PATH_REQUESTS).document(request.id!!)
        docAccept.set(request).addOnCompleteListener {

            // Reject
            val queryReject = db.collection(COLLECTION_PATH_REQUESTS)
                .whereEqualTo("adId", request.AdId)
                .whereNotEqualTo(FieldPath.documentId(), request.id)
            queryReject.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (doc in task.result?.documents!!) {
                        var tmp = Request(doc)
                        tmp.Status = 2
                        val docTmp: DocumentReference = db.collection(COLLECTION_PATH_REQUESTS).document(tmp.id!!)
                        docTmp.set(tmp)
                    }
                } else {
                    Log.e(TAG, "Error getting documents, acceptRequest - reject part", task.exception)

                }
            }

            // Add credit to current user (organiser)
            val queryCurrentUser = db.collection(COLLECTION_PATH_USERS)
                .whereEqualTo("email", mAuth.currentUser!!.email!!)
            queryCurrentUser.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(!task.result?.documents.isNullOrEmpty()) {
                        val doc = task.result?.documents!![0]
                        var tmp = User(doc)
                        OrganiserUser = tmp.email
                        OrganiserUserName = tmp.fullName
                        tmp.credit = tmp.credit + request.AdDuration
                        val docTmp: DocumentReference = db.collection(COLLECTION_PATH_USERS).document(tmp.id!!)
                        docTmp.set(tmp)
                    }else
                        Log.e(TAG, "Document not found, acceptRequest - Add credit to current user", task.exception)
                } else {
                    Log.e(TAG, "Error getting documents, acceptRequest - Add credit to current user", task.exception)

                }
            }

            // Reduce credit from attending user
            val queryAttendingUser = db.collection(COLLECTION_PATH_USERS)
                .whereEqualTo("email", request.RequestUser)
            queryAttendingUser.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(!task.result?.documents.isNullOrEmpty()) {
                        val doc = task.result?.documents!![0]
                        var tmp = User(doc)
                        AttendingUser = tmp.email
                        AttendingUserName = tmp.fullName
                        tmp.credit = tmp.credit - request.AdDuration
                        val docTmp: DocumentReference = db.collection(COLLECTION_PATH_USERS).document(tmp.id!!)
                        docTmp.set(tmp)
                    }else
                        Log.e(TAG, "Document not found, acceptRequest - Reduce credit from attending user", task.exception)
                } else {
                    Log.e(TAG, "Error getting documents, acceptRequest - Reduce credit from attending user", task.exception)

                }
            }

            // Deactivate Ad
            val queryAd = db.collection(COLLECTION_PATH_ADS)
                .whereEqualTo(FieldPath.documentId(), request.AdId)
            queryAd.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(!task.result?.documents.isNullOrEmpty()) {
                        val doc = task.result?.documents!![0]
                        var tmp = Ad(doc)
                        tmp.activeStatus = false
                        val docTmp: DocumentReference = db.collection(COLLECTION_PATH_ADS).document(tmp.id!!)
                        docTmp.set(tmp)
                    }else
                        Log.e(TAG, "Document not found, acceptRequest - Deactivate Ad", task.exception)
                } else {
                    Log.e(TAG, "Error getting documents, acceptRequest - Deactivate Ad", task.exception)

                }
            }

            //Create session
            val docSession: DocumentReference = db.collection(COLLECTION_PATH_SESSIONS).document()
            val session = Session(
                docSession.id,
                request.AdId,
                request.AdTitle,
                request.AdDateTime,
                request.AdLocation,
                request.AdDuration,
                OrganiserUser,
                OrganiserUserName,
                true,
                AttendingUser,
                AttendingUserName,
                true
            )
            docSession.set(session).addOnSuccessListener {
                onFinishCallback()
            }
        }
    }

    fun rejectRequest(request: Request, onFinishCallback: () -> Unit = {}) {
        val db = FirebaseFirestore.getInstance()
        val queryReject = db.collection(COLLECTION_PATH_REQUESTS)
            .whereEqualTo(FieldPath.documentId(), request.id)
        queryReject.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if(!task.result?.documents.isNullOrEmpty()) {
                    val doc = task.result?.documents!![0]
                    var tmp = Request(doc)
                    tmp.Status = 2
                    val docTmp: DocumentReference = db.collection(COLLECTION_PATH_REQUESTS).document(tmp.id!!)
                    docTmp.set(tmp).addOnCompleteListener {
                        onFinishCallback()
                    }
                }else
                    Log.e(TAG, "Document not found, rejectRequest", task.exception)
            } else {
                Log.e(TAG, "Error getting documents, rejectRequest", task.exception)

            }
        }

    }

    fun getUserSessionList(onFinishCallback: (ArrayList<Session>) -> Unit = {}) {
        var sessionList: ArrayList<Session> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val query = db.collection(COLLECTION_PATH_SESSIONS)
            .whereEqualTo("organiserUser", mAuth.currentUser!!.email!!)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (doc in task.result?.documents!!) {
                    val req = Session(doc)
                    sessionList.add(req)
                }
                val query2 = db.collection(COLLECTION_PATH_SESSIONS)
                    .whereEqualTo("attendingUser", mAuth.currentUser!!.email!!)
                query2.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result?.documents!!) {
                            val req = Session(doc)
                            sessionList.add(req)
                        }


                        onFinishCallback(sessionList)

                    } else {
                        Log.e(TAG, "Error getting documents, getUserSessionList", task.exception)

                    }
                }

            } else {
                Log.e(TAG, "Error getting documents, getUserSessionList", task.exception)

            }
        }
    }

    fun saveUserRating(rating: UserRating, onFinishCallback: () -> Unit = {}) {

        val db = FirebaseFirestore.getInstance()
        val doc: DocumentReference = db.collection(COLLECTION_PATH_USERRATINGS).document()
        rating.id = doc.id
        doc.set(rating).addOnSuccessListener {
            onFinishCallback()
        }
    }

    fun getUserRatingsAsOrganiser(onFinishCallback: (ArrayList<UserRating>) -> Unit = {}) {
        var rateList: ArrayList<UserRating> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val query = db.collection(COLLECTION_PATH_USERRATINGS)
            .whereEqualTo("userId", mAuth.currentUser!!.email!!)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (doc in task.result?.documents!!) {
                    val rate = UserRating(doc)
                    if(rate.IsOrganiser) {
                        rateList.add(rate)
                    }
                }
                onFinishCallback(rateList)
            } else {
                Log.e(TAG, "Error getting documents, getUserRatingsAsOrganiser", task.exception)

            }
        }
    }

    fun getUserRatingsAsAttendee(onFinishCallback: (ArrayList<UserRating>) -> Unit = {}) {
        var rateList: ArrayList<UserRating> = ArrayList()
        var mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val query = db.collection(COLLECTION_PATH_USERRATINGS)
            .whereEqualTo("userId", mAuth.currentUser!!.email!!)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (doc in task.result?.documents!!) {
                    val rate = UserRating(doc)
                    if(rate.IsOrganiser) {
                        rateList.add(rate)
                    }
                }
                onFinishCallback(rateList)
            } else {
                Log.e(TAG, "Error getting documents, getUserRatingsAsAttendee", task.exception)

            }
        }
    }
}
