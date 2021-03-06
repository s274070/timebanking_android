package com.group25.timebanking.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class Request(
    var id: String?,
    val AdId:String,
    val AdTitle: String,
    val AdDateTime: String,
    val AdLocation: String,
    val AdDuration: Int,
    val RequestDescription: String?,
    val AdUser: String,
    val RequestUser: String,
    var RequestUserName: String,
    var Status: Int) {
    constructor(doc: QueryDocumentSnapshot) : this(
        doc.id,
        doc.getString("adId")!!,
        doc.getString("adTitle")!!,
        doc.getString("adDateTime")!!,
        doc.getString("adLocation")!!,
        doc.getLong("adDuration")!!.toInt(),
        doc.getString("requestDescription"),
        doc.getString("adUser")!!,
        doc.getString("requestUser")!!,
        doc.getString("requestUserName")!!,
        doc.getLong("status")!!.toInt()
    )
    constructor(doc: DocumentSnapshot) : this(
        doc.id,
        doc.getString("adId")!!,
        doc.getString("adTitle")!!,
        doc.getString("adDateTime")!!,
        doc.getString("adLocation")!!,
        doc.getLong("adDuration")!!.toInt(),
        doc.getString("requestDescription"),
        doc.getString("adUser")!!,
        doc.getString("requestUser")!!,
        doc.getString("requestUserName")!!,
        doc.getLong("status")!!.toInt()
    )
}