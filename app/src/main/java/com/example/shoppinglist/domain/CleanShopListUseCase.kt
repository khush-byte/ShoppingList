package com.example.shoppinglist.domain

class CleanShopListUseCase(private var shopListRepository: ShopListRepository) {
    fun cleanShopList(){
        shopListRepository.cleanShopList()
    }
}