package com.example.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R

//Class for the view holder to hold the generated views
class ShopItemViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
    val tvName: TextView = view.findViewById<TextView>(R.id.p_name)
    val tvCount: TextView = view.findViewById<TextView>(R.id.p_count)
}