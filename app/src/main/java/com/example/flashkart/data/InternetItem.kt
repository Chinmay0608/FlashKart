package com.example.flashkart.data

import kotlinx.serialization.Serializable

@Serializable
data class InternetItem(
    val itemName: String,
    val itemCategoryId: String,
    val itemQuantity: String,
    val itemPrice: Int,
    val imageUrl: String
)