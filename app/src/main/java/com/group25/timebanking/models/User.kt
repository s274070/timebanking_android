package com.group25.timebanking.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class User(
    var id: String,
    val email: String,
    val fullName: String,
    val nickName: String,
    val skills: String?,
    val description:String?,
    val location:String?,
    var credit: Long,
    val imageUrl:String?) {
    constructor(doc: QueryDocumentSnapshot) : this(
        doc.id,
        doc.getString("email")!!,
        doc.getString("fullName")!!,
        doc.getString("nickName")!!,
        doc.getString("skills"),
        doc.getString("description"),
        doc.getString("location"),
        doc.getLong("credit")!!,
        doc.getString("imageUrl")
    )
    constructor(doc: DocumentSnapshot) : this(
        doc.id,
        doc.getString("email")!!,
        doc.getString("fullName")!!,
        doc.getString("nickName")!!,
        doc.getString("skills"),
        doc.getString("description"),
        doc.getString("location"),
        doc.getLong("credit")!!,
        doc.getString("imageUrl")
    )
}