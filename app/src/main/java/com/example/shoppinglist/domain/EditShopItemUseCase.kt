package com.example.shoppinglist.domain

class EditShopItemUseCase(private var shopListRepository: ShopListRepository) {
    suspend fun editItem(shopItem: ShopItem){
        shopListRepository.editShopItem(shopItem)
    }
}