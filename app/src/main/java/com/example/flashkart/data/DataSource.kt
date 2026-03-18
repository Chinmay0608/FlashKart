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

    fun loadAllItems(): List<Item> {
        return listOf(
            // Fresh Fruits
            Item(R.string.banana_robusta, R.string.fresh_fruits, "1 kg", 40, R.drawable.banana_robusta),
            Item(R.string.shimla_apple, R.string.fresh_fruits, "1 kg", 180, R.drawable.shimla_apple),
            Item(R.string.pineapple, R.string.fresh_fruits, "1 unit", 80, R.drawable.pineapple),
            Item(R.string.pomegranate, R.string.fresh_fruits, "500 g", 120, R.drawable.pomegranate),
            Item(R.string.papaya_semi_ripe, R.string.fresh_fruits, "1 kg", 60, R.drawable.papaya_semi_ripe),

            // Vegetables
            Item(R.string.onion, R.string.vegetables, "1 kg", 50, R.drawable.onion),
            Item(R.string.tomato, R.string.vegetables, "1 kg", 40, R.drawable.tomato),
            Item(R.string.potato, R.string.vegetables, "1 kg", 30, R.drawable.potato),
            Item(R.string.carrot, R.string.vegetables, "500 g", 40, R.drawable.carrot),

            // Munchies
            Item(R.string.lays, R.string.munchies, "50 g", 20, R.drawable.lays),
            Item(R.string.nachos, R.string.munchies, "150 g", 90, R.drawable.nachos),
            Item(R.string.popcorn, R.string.munchies, "100 g", 50, R.drawable.popcorn),

            // Packaged Food
            Item(R.string.noodles, R.string.packaged_food, "280 g", 60, R.drawable.noodles),
            Item(R.string.pasta, R.string.packaged_food, "500 g", 120, R.drawable.pasta),

            // Kitchen Essentials
            Item(R.string.salt, R.string.kitchen_essentials, "1 kg", 25, R.drawable.salt),
            Item(R.string.sugar, R.string.kitchen_essentials, "1 kg", 45, R.drawable.sugar),
            Item(R.string.cooking_oil, R.string.kitchen_essentials, "1 L", 150, R.drawable.cooking_oil),
            Item(R.string.wheat_flour, R.string.kitchen_essentials, "5 kg", 250, R.drawable.wheat_flour),

            // Bath & Body
            Item(R.string.soap, R.string.bath_body, "125 g", 45, R.drawable.dove_soap),
            Item(R.string.body_wash, R.string.bath_body, "250 ml", 180, R.drawable.body_wash),
            Item(R.string.body_lotion, R.string.bath_body, "200 ml", 220, R.drawable.body_lotion),
            Item(R.string.nivea_shower_gel, R.string.bath_body, "250 ml", 199, R.drawable.nivea_shower_gel),

            // Stationery
            Item(R.string.notebook, R.string.stationery, "1 unit", 60, R.drawable.notebook),
            Item(R.string.pencil, R.string.stationery, "10 units", 50, R.drawable.pencil),
            Item(R.string.eraser, R.string.stationery, "1 unit", 5, R.drawable.eraser),
            Item(R.string.ball_pen, R.string.stationery, "1 unit", 10, R.drawable.ball_pen),

            // Bread & Biscuits
            Item(R.string.white_bread, R.string.bread_biscuits, "400 g", 40, R.drawable.white_bread),
            Item(R.string.brown_bread, R.string.bread_biscuits, "400 g", 50, R.drawable.brown_bread),
            Item(R.string.marie_biscuit, R.string.bread_biscuits, "200 g", 30, R.drawable.marie_biscuit),

            // Sweet Tooth
            Item(R.string.chocolate, R.string.sweet_tooth, "100 g", 100, R.drawable.chocolate),
            Item(R.string.ice_cream, R.string.sweet_tooth, "500 ml", 150, R.drawable.ice_cream),
            Item(R.string.gulab_jamun, R.string.sweet_tooth, "500 g", 200, R.drawable.gulab_jamun),

            // Pet Food
            Item(R.string.pedigree, R.string.pet_food, "1 kg", 350, R.drawable.pedigree),
            Item(R.string.cat_food, R.string.pet_food, "1 kg", 380, R.drawable.cat_food),
            Item(R.string.dog_biscuit, R.string.pet_food, "500 g", 150, R.drawable.dog_biscuit),

            // Beverages
            Item(R.string.pepsi_can, R.string.beverages, "330 ml", 40, R.drawable.pepsi_can)
        )
    }

    fun loadItems(
        @StringRes categoryName:Int):List<Item>{
        return loadAllItems().filter {
            it.itemCategoryId == categoryName
        }
    }
}