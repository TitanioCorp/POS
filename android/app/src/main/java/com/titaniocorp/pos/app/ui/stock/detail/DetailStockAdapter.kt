package com.titaniocorp.pos.app.ui.stock.detail
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.PriceStock
import com.titaniocorp.pos.app.viewmodel.model.PriceStockViewModel
import com.titaniocorp.pos.databinding.ItemListStockDetailBinding
import com.titaniocorp.pos.databinding.ItemListStockPriceBinding

/**
 * Adaptador que maneja la lista de stock
 * @version 1.0
 * @author Juan Ortiz
 * @date 14/01/2019
 */
class DetailStockAdapter(val clickListener: View.OnClickListener ?= null): ListAdapter<PriceStock, DetailStockAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_stock_detail, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            itemView.tag = position
            bind(getItem(position), clickListener)
        }
    }

    class ViewHolder (private val binding: ItemListStockDetailBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: PriceStock, listener: View.OnClickListener ?= null){
            with(binding) {
                viewModel = PriceStockViewModel(item)
                clickListener = listener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<PriceStock>() {
        override fun areItemsTheSame(oldItem: PriceStock, newItem: PriceStock): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PriceStock, newItem: PriceStock): Boolean =
            oldItem == newItem
    }
}
