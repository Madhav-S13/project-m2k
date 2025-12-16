package com.example.blood_donation.data.model

import com.google.firebase.firestore.GeoPoint

data class Camp(
    val name: String = "",
    val organizer: String = "",
    val phone: String = "",
    val location: GeoPoint? = null
)