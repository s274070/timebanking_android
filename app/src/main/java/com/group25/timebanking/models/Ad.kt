package com.group25.timebanking.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.text.SimpleDateFormat
import java.util.*

class Ad(
    var id: String,
    val title: String,
    val description:String?,
    val date: String,
    val time: String,
    val duration: Int,
    val location: String,
    val createdUser: String,
    var activeStatus: Boolean) {
    constructor(doc: QueryDocumentSnapshot) : this(
        doc.id,
        doc.getString("title")!!,
        doc.getString("description"),
        doc.getString("date")!!,
        doc.getString("time")!!,
        doc.getLong("duration")!!.toInt(),
        doc.getString("location")!!,
        doc.getString("createdUser")!!,
        doc.getBoolean("activeStatus")!!
    )
    constructor(doc: DocumentSnapshot) : this(
        doc.id,
        doc.getString("title")!!,
        doc.getString("description"),
        doc.getString("date")!!,
        doc.getString("time")!!,
        doc.getLong("duration")!!.toInt(),
        doc.getString("location")!!,
        doc.getString("createdUser")!!,
        doc.getBoolean("activeStatus")!!
    )
}