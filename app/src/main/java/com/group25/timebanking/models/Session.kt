package com.group25.timebanking.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class Session(
    var id: String?,
    val AdId:String,
    val AdTitle: String,
    val AdDateTime: String,
    val AdLocation: String,
    val AdDuration: Int,
    val OrganiserUser: String,
    val OrganiserUserName: String,
    val OrganiserUserFeedbackActive: Boolean,
    val AttendingUser: String,
    val AttendingUserName: String,
    val AttendingUserFeedbackActive: Boolean) {
    constructor(doc: QueryDocumentSnapshot) : this(
        doc.id,
        doc.getString("adId")!!,
        doc.getString("adTitle")!!,
        doc.getString("adDateTime")!!,
        doc.getString("adLocation")!!,
        doc.getLong("adDuration")!!.toInt(),
        doc.getString("organiserUser")!!,
        doc.getString("organiserUserName")!!,
        doc.getBoolean("organiserUserFeedbackActive")!!,
        doc.getString("attendingUser")!!,
        doc.getString("attendingUserName")!!,
        doc.getBoolean("attendingUserFeedbackActive")!!
    )
    constructor(doc: DocumentSnapshot) : this(
        doc.id,
        doc.getString("adId")!!,
        doc.getString("adTitle")!!,
        doc.getString("adDateTime")!!,
        doc.getString("adLocation")!!,
        doc.getLong("adDuration")!!.toInt(),
        doc.getString("organiserUser")!!,
        doc.getString("organiserUserName")!!,
        doc.getBoolean("organiserUserFeedbackActive")!!,
        doc.getString("attendingUser")!!,
        doc.getString("attendingUserName")!!,
        doc.getBoolean("attendingUserFeedbackActive")!!
    )
}