package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ItemDisabledBinding
import com.example.shoppinglist.databinding.ItemEnabledBinding
import com.example.shoppinglist.domain.ShopItem

//Set items by adapter to recyclerView.
//A list adapter has been used to improve performance.
class ShopListAdaptor : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding = viewHolder.binding
        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }

        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        when (binding) {
            is ItemDisabledBinding -> {
                binding.shopItem = shopItem
            }
            is ItemEnabledBinding -> {
                binding.shopItem = shopItem
            }
        }
    }

    //Override to assign different layouts
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    //Set constant for view state and for view holder size of elements
    companion object {
        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101

        //View holder pool sizer
        const val MAX_POOL_SIZE = 16
    }
}