package com.fangpu.ilovezappos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fangpu.ilovezappos.data.Order
import com.fangpu.ilovezappos.databinding.OrderBookListItemBinding

class OrderBookAdapter: ListAdapter<Order, OrderBookAdapter.RowViewHolder>(OrderBookDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = OrderBookListItemBinding.inflate(layoutInflater, parent, false)
        return RowViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class RowViewHolder(val binding: OrderBookListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Order){
            binding.bidText.text = item.bid
            binding.bidAmountText.text = item.bid_amount
            binding.askText.text = item.ask
            binding.askAmountText.text = item.ask_amount
        }
    }

    companion object OrderBookDiffCallback :
        DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}
