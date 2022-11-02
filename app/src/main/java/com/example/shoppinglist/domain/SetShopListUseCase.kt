package com.example.shoppinglist.domain

class SetShopListUseCase(private var shopListRepository: ShopListRepository) {
    fun setList(shopList: List<ShopItem>){
        shopListRepository.setShopList(shopList)
    }
}