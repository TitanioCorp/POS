package com.titaniocorp.pos.app.ui.customer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Customer
import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.app.viewmodel.model.CustomerViewModel
import com.titaniocorp.pos.app.viewmodel.model.ProfitViewModel
import com.titaniocorp.pos.databinding.ItemListCustomerDashboardBinding
import com.titaniocorp.pos.databinding.ItemListProfitDashboardBinding

/**
 * Adaptador que maneja la lista de clientes(customers)
 * @author Juan Ortiz
 * @date 09/01/2020
 */
class DashboardCustomerAdapter(private val listener: OnItemClickListener): ListAdapter<Customer, DashboardCustomerAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_customer_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { item ->
            with(holder) {
                itemView.tag = item
                bind(item, createOnClickListener(position))
            }
        }
    }

    private fun createOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            when(it.id){
                R.id.button_remove -> {
                    listener.onClickRemoveItem(position)
                }
                R.id.view_content -> {
                    listener.onClickItem(position)
                }
            }
        }
    }

    class ViewHolder (private val binding: ItemListCustomerDashboardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Customer, listener: View.OnClickListener){
            with(binding) {
                viewModel = CustomerViewModel(item)
                clickListener = listener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener{
        fun onClickItem(position: Int)
        fun onClickRemoveItem(position: Int)
    }
}
