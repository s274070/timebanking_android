package com.group25.timebanking.models

import com.google.firebase.firestore.QueryDocumentSnapshot

class User(
    var id: String,
    val email: String,
    val fullName: String,
    val nickName: String,
    val skills: String?,
    val description:String?,
    val location:String?,
    val credit: Long,
    val imageUrl:String?) {
    constructor(doc: QueryDocumentSnapshot) : this(
        doc.id,
        doc.getString("Email")!!,
        doc.getString("FullName")!!,
        doc.getString("Nickname")!!,
        doc.getString("Skills"),
        doc.getString("Description"),
        doc.getString("Location"),
        doc.getLong("Credit")!!,
        doc.getString("ImageUrl")
    )
}