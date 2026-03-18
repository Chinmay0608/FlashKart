package com.example.flashkart.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Item(
    @StringRes val stringResourceId: Int = 0,
    @StringRes val itemCategoryId: Int = 0,
    val itemQuantityId: String = "",
    val itemPrice: Int = 0,
    @DrawableRes val imageResourceId: Int = 0,
    val itemName: String? = null,
    val imageUrl: String? = null
)