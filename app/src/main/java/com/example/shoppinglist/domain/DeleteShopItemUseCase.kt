package com.example.shoppinglist.domain

class DeleteShopItemUseCase(private var shopListRepository: ShopListRepository) {
    fun deleteItem(shopItem: ShopItem){
        shopListRepository.deleteShopItem(shopItem)
    }
}