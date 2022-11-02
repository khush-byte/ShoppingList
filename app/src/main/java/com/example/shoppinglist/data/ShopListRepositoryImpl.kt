package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

//Implementation of the interface
object ShopListRepositoryImpl : ShopListRepository {
    //Creation of the live date variable to store a list of items
    private var shopListLiveData = MutableLiveData<List<ShopItem>>()

    //Item sorting
    private var shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })

    //Variable for counting id number
    var autoIncrementId = 0

    //Implementation of the add function
    override fun addShopItem(shopItem: ShopItem) {
        //If item has default value than set auto increment
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        //Add a new item to the list
        shopList.add(shopItem)
        //Add the list to the live data
        updateList()
    }

    //Implementation of the edit function
    override fun editShopItem(shopItem: ShopItem) {
        //Deleting the old version of element
        val oldElement = getShopItem(shopItem.id)
        deleteShopItem(oldElement)
        //Adding the new version of element
        addShopItem(shopItem)
    }

    //Implementation of the delete function
    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    //Implementation of the get item function
    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
            //Throw exception if id does not exist
        } ?: throw java.lang.RuntimeException("Element with id $shopItemId not found")
    }

    //Implementation of the get list function
    override fun getShopList(): LiveData<List<ShopItem>> {
        shopListLiveData.value = shopList.toList()
        return shopListLiveData
    }

    //Implementation of the list update function
    override fun setShopList(shopList: List<ShopItem>) {
        cleanShopList()
        for (item in shopList){
            val shopItem = ShopItem(item.name, item.count, item.enabled)
            addShopItem(shopItem)
        }
    }

    //Passing the item's list to the live date
    private fun updateList() {
        shopListLiveData.value = shopList.toList()
    }

    override fun cleanShopList(){
        shopList.clear()
        updateList()
    }
}