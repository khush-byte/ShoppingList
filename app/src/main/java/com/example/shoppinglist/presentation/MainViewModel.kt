package com.example.shoppinglist.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.*
import com.google.gson.Gson

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //Variables init
    private val repository = ShopListRepositoryImpl
    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val setShopListUseCase = SetShopListUseCase(repository)
    private val cleanShopListUseCase = CleanShopListUseCase(repository)
    var shopList = getShopListUseCase.getList()
    private val _application = application

    //Item's state switch function
    fun changeEnableState(shopItem: ShopItem) {
        //Reverse the existing state
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        //Rewrite item
        editShopItemUseCase.editItem(newItem)
    }

    //Delete the item from the list
    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteItem(shopItem)
    }

    //Rewrite the livedata
    fun setShopListFromPreferences() {
        var shopList: List<ShopItem> = emptyList()
        val pref = _application.getSharedPreferences("root_data", Context.MODE_PRIVATE)
        val jsonList: String? = pref.getString("jsonShopList", "")
        if (jsonList != null) {
            if (jsonList.isNotEmpty()) {
                shopList = Gson().fromJson(jsonList, Array<ShopItem>::class.java).asList()
            }
        }
        setShopListUseCase.setList(shopList)
    }

    //Get shop list
    fun checkShopListEmptiness(): Boolean {
        return getShopListUseCase.getList().value?.isEmpty() == true
    }

    //Delete all items in shop list
    fun cleanShopList() {
        cleanShopListUseCase.cleanShopList()
    }
}

