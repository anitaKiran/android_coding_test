package com.anita_coding_challenge.utils

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anita_coding_challenge.Item
import com.anita_coding_challenge.ItemsAdapter
import com.anita_coding_challenge.SearchViewModel


@BindingAdapter(value = ["items", "viewModel"])
fun setItems(view: RecyclerView, items: MutableList<Item>?, vm: SearchViewModel) {
    //Log.e("items", "setting items $items")
    view.adapter?.run {
        if (this is ItemsAdapter) {
            if (items != null) {
                this.items += items
                this.notifyDataSetChanged()
            }
        }
    } ?: run {
        if (items != null) {
            ItemsAdapter(items,vm).apply {
                view.adapter = this
            }
        }
    }
}









