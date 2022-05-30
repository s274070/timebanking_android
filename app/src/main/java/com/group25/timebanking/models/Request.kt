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
    val RequestUserName: String,
    val Status: Int) {
    constructor(doc: QueryDocumentSnapshot) : this(
        doc.id,
        doc.getString("AdId")!!,
        doc.getString("AdTitle")!!,
        doc.getString("AdDateTime")!!,
        doc.getString("AdLocation")!!,
        doc.getLong("AdDuration")!!.toInt(),
        doc.getString("RequestDescription"),
        doc.getString("AdUser")!!,
        doc.getString("RequestUser")!!,
        doc.getString("RequestUserName")!!,
        doc.getLong("Status")!!.toInt()
    )
    constructor(doc: DocumentSnapshot) : this(
        doc.id,
        doc.getString("AdId")!!,
        doc.getString("AdTitle")!!,
        doc.getString("AdDateTime")!!,
        doc.getString("AdLocation")!!,
        doc.getLong("AdDuration")!!.toInt(),
        doc.getString("RequestDescription"),
        doc.getString("AdUser")!!,
        doc.getString("RequestUser")!!,
        doc.getString("RequestUserName")!!,
        doc.getLong("Status")!!.toInt()
    )
}