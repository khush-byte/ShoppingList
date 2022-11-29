package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

//Создание интерфейса с основнымы функциями бизнес логики приложения
interface ShopListRepository {
    fun addShopItem(shopItem: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun getShopItem(shopItemId: Int): ShopItem
    fun getShopList(): LiveData<List<ShopItem>>
    //fun setShopList(shopList: List<ShopItem>)
    fun cleanShopList()
}