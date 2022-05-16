package com.group25.timebanking.models

import com.google.firebase.firestore.QueryDocumentSnapshot

class Users(
    var id: String,
    val email: String,
    val fullName: String,
    val nickName: String,
    val skills: String?,
    val description:String?,
    val location:String?) {
    constructor(doc: QueryDocumentSnapshot) : this(
        doc.id,
        doc.getString("Email")!!,
        doc.getString("FullName")!!,
        doc.getString("Nickname")!!,
        doc.getString("Skills"),
        doc.getString("Description"),
        doc.getString("Location")
    )
}