package com.example.catsadoption_shop.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserProfile(
    val id: Int,
    val name: String,
    val birthdate: String,          // "1970-01-01" etc.
    val role: String,               // "owner" or "customer"
    val email: String,
    val phone: String,
    val adoptedCats: List<AdoptedCat> = emptyList()
)
