package com.example.shoppinglist.domain

class AddShopItemUseCase(private var shopListRepository: ShopListRepository) {
    suspend fun addItem(shopItem: ShopItem) {
        shopListRepository.addShopItem(shopItem)
    }
}