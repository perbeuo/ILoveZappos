package com.fangpu.ilovezappos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fangpu.ilovezappos.data.Order
import com.fangpu.ilovezappos.databinding.OrderBookListItemBinding

class OrderBookAdapter: ListAdapter<Order, OrderBookAdapter.RowViewHolder>(OrderBookDiffCallback) {
//    var data = listOf<Order>()
//    set(value) {
//        field = value
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
//            .inflate(R.layout.order_book_list_item, parent, false)
        val binding = OrderBookListItemBinding.inflate(layoutInflater, parent, false)
        return RowViewHolder(binding)
    }

//    override fun getItemCount(): Int {
//        return data.size
//    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
//        holder.itemView.apply {
//            bid_text.text = getItem(position).bid.toString()
//            amount_text.text = getItem(position).bid_amount.toString()
//        }
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