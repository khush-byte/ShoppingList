package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

//Implementation of the interface
class ShopListRepositoryImpl(
    application: Application
) : ShopListRepository {
    private val shopListDao = AppDatabase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()


    //Implementation of the add function
    override fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    //Implementation of the delete function
    override fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    //Implementation of the edit function
    override fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    //Implementation of the get item function
    override fun getShopItem(shopItemId: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    //Implementation of the get list function
//    override fun getShopList(): LiveData<List<ShopItem>> = MediatorLiveData<List<ShopItem>>().apply {
//        addSource(shopListDao.getShopList()){
////            if(it.size>10){
//    //                value = it
////            }
//            value = mapper.mapListDbModelToListEntity(it)
//        }
//    }

    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        shopListDao.getShopList()
    ) {
        mapper.mapListDbModelToListEntity(it)
    }

//    //Implementation of the list update function
//    override fun setShopList(shopList: List<ShopItem>) {
//        cleanShopList()
//        for (item in shopList) {
//            val shopItem = ShopItem(item.name, item.count, item.enabled)
//            addShopItem(shopItem)
//        }
//    }
//
//    //Passing the item's list to the live date
//    private fun updateList() {
//        shopListLiveData.value = shopList.toList()
//    }
//
//    override fun cleanShopList() {
//        shopList.clear()
//        updateList()
//    }
}