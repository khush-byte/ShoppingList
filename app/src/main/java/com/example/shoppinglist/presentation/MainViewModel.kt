package com.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //Variables init
    private val repository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val cleanShopListUseCase = CleanShopListUseCase(repository)

    //private val scope = CoroutineScope(Dispatchers.Main)

    var shopList = getShopListUseCase.getList()

    //Delete the item from the list
    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteItem(shopItem)
        }
    }

    //Item's state switch function
    fun changeEnableState(shopItem: ShopItem) {
        viewModelScope.launch {
            //Reverse the existing state
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            //Rewrite item
            editShopItemUseCase.editItem(newItem)
        }
    }

    //Delete all items in shop list
    fun cleanShopList() {
        viewModelScope.launch {
            cleanShopListUseCase.cleanShopList()
        }
    }

//    override fun onCleared() {
//        super.onCleared()
//        scope.cancel()
//    }
}

