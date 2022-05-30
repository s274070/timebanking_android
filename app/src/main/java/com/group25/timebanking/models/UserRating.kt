package com.group25.timebanking.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class UserRating(
    var id: String?,
    val UserId:String,
    val SessionId: String,
    val Rating: Int,
    val IsOrganiser: Boolean,
    val Description: String?) {
    constructor(doc: QueryDocumentSnapshot) : this(
        doc.id,
        doc.getString("UserId")!!,
        doc.getString("SessionId")!!,
        doc.getLong("Rating")!!.toInt(),
        doc.getBoolean("IsOrganiser")!!,
        doc.getString("Description")
    )
    constructor(doc: DocumentSnapshot) : this(
        doc.id,
        doc.getString("UserId")!!,
        doc.getString("SessionId")!!,
        doc.getLong("Rating")!!.toInt(),
        doc.getBoolean("IsOrganiser")!!,
        doc.getString("Description")
    )
}