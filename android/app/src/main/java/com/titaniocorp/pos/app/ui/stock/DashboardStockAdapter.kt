package com.titaniocorp.pos.app.ui.stock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Stock
import com.titaniocorp.pos.app.viewmodel.model.StockViewModel
import com.titaniocorp.pos.databinding.ItemListStockDashboardBinding

/**
 * Adaptador que maneja la lista de stock
 * @version 1.0
 * @author Juan Ortiz
 * @date 14/01/2019
 */
class DashboardStockAdapter: ListAdapter<Stock, DashboardStockAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_stock_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { item ->
            with(holder) {
                itemView.tag = item
                bind(item, createOnClickListener(item.id))
            }
        }
    }

    private fun createOnClickListener(id: Long): View.OnClickListener {
        return View.OnClickListener {
            val direction = DashboardStockFragmentDirections.toDetailStockFragment(id)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder (private val binding: ItemListStockDashboardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Stock, listener: View.OnClickListener){
            with(binding) {
                viewModel = StockViewModel(item)
                clickListener = listener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean =
            oldItem == newItem
    }
}
