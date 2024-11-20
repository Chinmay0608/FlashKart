package com.example.flashkart.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InternetItem(
    @SerialName(value = "stringResourceId")
    val itemName: String,

    @SerialName(value = "itemCategoryId")
    val itemCategory: String,

    @SerialName(value = "itemQuantity")
    val itemQuantity: String,

    @SerialName("item_price")
    val itemPrice: Int,

    @SerialName("imageResourceId")
    val imageUrl: String
)
