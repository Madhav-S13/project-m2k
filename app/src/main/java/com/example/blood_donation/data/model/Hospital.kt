package com.example.blood_donation.data.model

data class Hospital(
    val name: String = "",
    val location: String = "",
    val phone: String = "",
    val bloodStock: Map<String, String> = emptyMap()
)
