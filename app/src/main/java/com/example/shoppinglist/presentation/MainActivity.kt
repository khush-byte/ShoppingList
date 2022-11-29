package com.example.shoppinglist.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdaptor
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        //if (viewModel.checkShopListEmptiness()) viewModel.setShopListFromPreferences()
        setupRecyclerView()

        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
            if (it.isNotEmpty()) binding.btnDelete.visibility = View.VISIBLE
            else binding.btnDelete.visibility = View.GONE
        }

        binding.buttonAddShopItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }

        binding.btnDelete.setOnClickListener {
            viewModel.cleanShopList()
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }

    private fun setupRecyclerView() {
        //val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        with(binding.rvShopList) {
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
        setupSwipeListener(binding.rvShopList)
    }

    private fun isOnePaneMode(): Boolean {
        return binding.shopItemContainer == null
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

//    override fun onStop() {
//        super.onStop()
//        saveData()
//    }

//    private fun saveData() {
//        val pref = getSharedPreferences("root_data", MODE_PRIVATE)
//        val editor = pref.edit()
//        val jsonListItem: String = Gson().toJson(viewModel.shopList.value)
//        if (!pref.getString("jsonShopList", "").equals(jsonListItem)) {
//            editor.putString("jsonShopList", jsonListItem)
//            editor.apply()
//            //Log.d("MyLogInfo", "SaveData")
//        }
//    }
}