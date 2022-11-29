package com.example.shoppinglist.domain

//Setting of the item's data type class
data class ShopItem(
    val name: String,
    val count: Int,
    val enabled: Boolean,
    //Assigning as default value of UNDEFINED_ID
    var id: Int = UNDEFINED_ID
) {
    //Creation of the constant for default value
    companion object {
        const val UNDEFINED_ID = 0
    }
}