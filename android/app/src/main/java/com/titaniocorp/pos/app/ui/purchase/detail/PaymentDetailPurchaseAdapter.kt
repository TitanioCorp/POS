package com.titaniocorp.pos.app.ui.purchase.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.PaymentPurchase
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto
import com.titaniocorp.pos.app.viewmodel.model.DetailPurchaseAdapterDtoViewModel
import com.titaniocorp.pos.app.viewmodel.model.PaymentPurchaseViewModel
import com.titaniocorp.pos.databinding.ItemListPurchaseDetailPaymentBinding
import com.titaniocorp.pos.databinding.ItemListPurchaseDetailProductBinding

/**
 * Adaptador que maneja la lista de DetailPurchaseAdapterDto
 * @version 1.0
 * @author Juan Ortiz
 * @date 15/01/2020
 */
class PaymentDetailPurchaseAdapter: ListAdapter<PaymentPurchase, PaymentDetailPurchaseAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_purchase_detail_payment, parent, false)
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

    class ViewHolder (private val binding: ItemListPurchaseDetailPaymentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: PaymentPurchase){
            with(binding) {
                viewModel = PaymentPurchaseViewModel(item)
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<PaymentPurchase>() {

        override fun areItemsTheSame(oldItem: PaymentPurchase, newItem: PaymentPurchase): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PaymentPurchase, newItem: PaymentPurchase): Boolean {
            return oldItem == newItem
        }
    }
}
