package com.titaniocorp.pos.app.ui.purchase
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
import com.titaniocorp.pos.app.model.dto.PurchaseDTO
import com.titaniocorp.pos.app.viewmodel.model.PurchaseDtoViewModel
import com.titaniocorp.pos.databinding.ItemListPurchaseDashboardBinding

/**
 * Adaptador que maneja la lista de peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardPurchaseAdapter: ListAdapter<PurchaseDashboardItem, DashboardPurchaseAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_purchase_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { item ->
            with(holder) {
                itemView.tag = item
                bind(item, createOnClickListener(item))
            }
        }
    }

    private fun createOnClickListener(item: PurchaseDashboardItem): View.OnClickListener {
        return View.OnClickListener {
            val direction = DashboardPurchaseFragmentDirections.toDetailPurchaseFragment(item.purchaseId)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder (private val binding: ItemListPurchaseDashboardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: PurchaseDashboardItem, listener: View.OnClickListener){
            with(binding) {
                viewModel = PurchaseDtoViewModel(item)
                clickListener = listener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<PurchaseDashboardItem>() {
        override fun areItemsTheSame(oldItem: PurchaseDashboardItem, newItem: PurchaseDashboardItem): Boolean =
            oldItem.purchaseId == newItem.purchaseId

        override fun areContentsTheSame(oldItem: PurchaseDashboardItem, newItem: PurchaseDashboardItem): Boolean =
            oldItem == newItem
    }
}
