package com.titaniocorp.pos.app.ui.warehouse
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Payment
import com.titaniocorp.pos.app.model.Stock
import com.titaniocorp.pos.app.viewmodel.model.PaymentViewModel
import com.titaniocorp.pos.app.viewmodel.model.StockViewModel
import com.titaniocorp.pos.databinding.ItemListStockDashboardBinding
import com.titaniocorp.pos.databinding.ItemListWarehouseDashboardBinding

/**
 * Adaptador que maneja la lista de pagos
 * @author Juan Ortiz
 * @date 03/02/2020
 */
class DashboardWarehouseAdapter(private val listener: View.OnClickListener): ListAdapter<Payment, DashboardWarehouseAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_warehouse_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { item ->
            with(holder) {
                itemView.tag = item
                bind(item, listener)
            }
        }
    }

    class ViewHolder (private val binding: ItemListWarehouseDashboardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Payment, listener: View.OnClickListener){
            with(binding) {
                viewModel = PaymentViewModel(item)
                clickListener = listener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Payment>() {
        override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean =
            oldItem == newItem
    }
}
