package com.anita_coding_challenge

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anita_coding_challenge.databinding.RowItemBinding

/**
 * Created by Anita Kiran on 6/1/2022.
 */
class ItemsAdapter() : RecyclerView.Adapter<ItemsAdapter.DataViewHolder>() {

    var list = arrayListOf<Item>()

    fun setContentList(list: ArrayList<Item>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataViewHolder {
        val binding =
            RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount() = list.size

    class DataViewHolder(val viewHolder: RowItemBinding): RecyclerView.ViewHolder(viewHolder.root)

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.viewHolder.items = this.list[position]
        holder.viewHolder.items?.has_wiki?.let { hasWiki ->
            if(hasWiki) {
                holder.viewHolder.cardLayout.setBackgroundColor(Color.parseColor("#D2FAF6"))
            }
        }
    }
}