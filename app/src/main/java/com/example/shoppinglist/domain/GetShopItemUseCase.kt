package com.example.shoppinglist.domain

class GetShopItemUseCase(private var shopListRepository: ShopListRepository) {
    suspend fun getItem(shopItemId: Int):ShopItem{
        return shopListRepository.getShopItem(shopItemId)
    }
}