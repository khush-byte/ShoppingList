package com.example.shoppinglist.domain

class CleanShopListUseCase(private var shopListRepository: ShopListRepository) {
    suspend fun cleanShopList(){
        shopListRepository.cleanShopList()
    }
}