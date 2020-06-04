package com.titaniocorp.pos.app.ui.report.stock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.domain.PurchaseDashboardItem
import com.titaniocorp.pos.app.model.domain.StockReportItem
import com.titaniocorp.pos.app.model.dto.PurchaseDTO
import com.titaniocorp.pos.app.viewmodel.model.PurchaseDtoViewModel
import com.titaniocorp.pos.databinding.ItemListPurchaseDashboardBinding
import com.titaniocorp.pos.databinding.ItemStockReportBinding

/**
 * Adaptador que maneja la lista de prices
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class StockReportAdapter: ListAdapter<StockReportItem, StockReportAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_stock_report, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { item ->
            with(holder) {
                itemView.tag = item
                bind(item)
            }
        }
    }

    class ViewHolder (private val binding: ItemStockReportBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: StockReportItem){
            with(binding) {
                viewModel = StockReportItemViewModel(item)
                clickListener = View.OnClickListener {
                    when(it.id){
                        R.id.view_content -> {
                            viewModel?.setSelected()
                        }
                    }
                }
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<StockReportItem>() {
        override fun areItemsTheSame(oldItem: StockReportItem, newItem: StockReportItem): Boolean =
            oldItem.priceId == newItem.priceId

        override fun areContentsTheSame(oldItem: StockReportItem, newItem: StockReportItem): Boolean =
            oldItem == newItem
    }
}
