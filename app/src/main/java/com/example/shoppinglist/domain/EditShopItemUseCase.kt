package com.example.shoppinglist.domain

class EditShopItemUseCase(private var shopListRepository: ShopListRepository) {
    fun editItem(shopItem: ShopItem){
        shopListRepository.editShopItem(shopItem)
    }
}