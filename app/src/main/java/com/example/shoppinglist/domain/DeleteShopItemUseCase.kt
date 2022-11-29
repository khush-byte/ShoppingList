package com.example.shoppinglist.domain

class DeleteShopItemUseCase(private var shopListRepository: ShopListRepository) {
    suspend fun deleteItem(shopItem: ShopItem){
        shopListRepository.deleteShopItem(shopItem)
    }
}