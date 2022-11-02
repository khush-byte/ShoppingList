package com.example.shoppinglist.domain

class AddShopItemUseCase(private var shopListRepository: ShopListRepository) {
    fun addItem(shopItem: ShopItem) {
        shopListRepository.addShopItem(shopItem)
    }
}