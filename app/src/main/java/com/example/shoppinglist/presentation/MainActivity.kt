package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

class MainActivity: AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener{
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdaptor
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.shop_item_container)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        if (viewModel.checkShopListEmptiness()) viewModel.setShopListFromPreferences()
        setupRecyclerView()

        val buttonDelete = findViewById<ImageButton>(R.id.btn_delete)

        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
            if(it.isNotEmpty()) buttonDelete.visibility = View.VISIBLE
            else buttonDelete.visibility = View.GONE
        }

        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        buttonAddItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }

        buttonDelete.setOnClickListener{
            viewModel.cleanShopList()
            Log.d("MyLogInfo", "Clean the list")
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(rvShopList) {
            shopListAdapter = ShopListAdaptor()
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdaptor.VIEW_TYPE_ENABLED,
                ShopListAdaptor.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdaptor.VIEW_TYPE_DISABLED,
                ShopListAdaptor.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun isOnePaneMode(): Boolean {
        return shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .add(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupSwipeListener(rvShopList: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,

                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    private fun saveData(){
        val pref =  getSharedPreferences("root_data", MODE_PRIVATE)
        val editor = pref.edit()
        val jsonListItem: String = Gson().toJson(viewModel.shopList.value);
        if(!pref.getString("jsonShopList","").equals(jsonListItem)) {
            Log.d("Debug", jsonListItem)
            editor.putString("jsonShopList", jsonListItem)
            editor.apply()
            Log.d("MyLogInfo", "SaveData")
        }
    }
}