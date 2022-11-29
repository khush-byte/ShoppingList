package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

//Создание интерфейса с основнымы функциями бизнес логики приложения
interface ShopListRepository {
    suspend fun addShopItem(shopItem: ShopItem)
    suspend fun editShopItem(shopItem: ShopItem)
    suspend fun deleteShopItem(shopItem: ShopItem)
    suspend fun getShopItem(shopItemId: Int): ShopItem
    fun getShopList(): LiveData<List<ShopItem>>
    suspend fun cleanShopList()
}