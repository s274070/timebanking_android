package com.group25.timebanking.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class UserRating(
    var id: String?,
    val UserId:String,
    val SessionId: String,
    val Rating: Int,
    val IsOrganiser: Boolean,
    val Feedback: String?) {
    constructor(doc: QueryDocumentSnapshot) : this(
        doc.id,
        doc.getString("userId")!!,
        doc.getString("sessionId")!!,
        doc.getLong("rating")!!.toInt(),
        doc.getBoolean("isOrganiser")!!,
        doc.getString("feedback")
    )
    constructor(doc: DocumentSnapshot) : this(
        doc.id,
        doc.getString("userId")!!,
        doc.getString("sessionId")!!,
        doc.getLong("rating")!!.toInt(),
        doc.getBoolean("isOrganiser")!!,
        doc.getString("feedback")
    )
}