package com.example.flashkart.data

import androidx.annotation.StringRes
import com.example.flashkart.R

object DataSource {
    fun loadCategories():List<Categories>{
        return listOf<Categories>(
            Categories(stringResourceId = R.string.fresh_fruits, imageResourceId = R.drawable.fruits),
            Categories(R.string.bath_body, R.drawable.bathbody),
            Categories(R.string.bread_biscuits, R.drawable.bread),
            Categories(R.string.kitchen_essentials, R.drawable.kitchen),
            Categories(R.string.munchies, R.drawable.munchies),
            Categories(R.string.packaged_food, R.drawable.pacakaged),
            Categories(R.string.stationery, R.drawable.stationery),
            Categories(R.string.pet_food, R.drawable.pet),
            Categories(R.string.sweet_tooth, R.drawable.sweet),
            Categories(R.string.vegetables, R.drawable.vegetables),
            Categories(R.string.beverages, R.drawable.beverages)


        )
    }
    fun loadItems(
        @StringRes categoryName:Int):List<Item>{
        return listOf(
            Item(R.string.banana_robusta,R.string.fresh_fruits,"1 kg",100,R.drawable.banana_robusta),
            Item(R.string.shimla_apple,R.string.fresh_fruits,"1 kg",100,R.drawable.shimla_apple),
            Item(R.string.pineapple,R.string.fresh_fruits,"1 kg",100,R.drawable.pineapple),
            Item(R.string.pomegranate,R.string.fresh_fruits,"1 kg",100,R.drawable.pomegranate),
            Item(R.string.pepsi_can,R.string.beverages,"1",100,R.drawable.pepsi_can),
            Item(R.string.papaya_semi_ripe,R.string.fresh_fruits,"1 kg",100,R.drawable.papaya_semi_ripe),
            Item(R.string.nivea_shower_gel,R.string.bath_body,"1",100,R.drawable.nivea_shower_gel)
        ).filter {
            it.itemCategoryId == categoryName
        }
    }
}