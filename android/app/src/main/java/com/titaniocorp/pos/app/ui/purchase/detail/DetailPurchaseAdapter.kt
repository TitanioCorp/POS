package com.titaniocorp.pos.app.ui.purchase.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto
import com.titaniocorp.pos.app.viewmodel.model.DetailPurchaseAdapterDtoViewModel
import com.titaniocorp.pos.databinding.ItemListPurchaseDetailProductBinding

/**
 * Adaptador que maneja la lista de DetailPurchaseAdapterDto
 * @version 1.0
 * @author Juan Ortiz
 * @date 15/01/2020
 */
class DetailPurchaseAdapter(private val listener: View.OnClickListener ?= null): ListAdapter<DetailPurchaseAdapterDto, DetailPurchaseAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_purchase_detail_product, parent, false)
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

    class ViewHolder (private val binding: ItemListPurchaseDetailProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: DetailPurchaseAdapterDto, viewListener: View.OnClickListener?){
            with(binding) {
                viewModel = DetailPurchaseAdapterDtoViewModel(item)
                clickListener = viewListener

                if(item.refund){
                    textDate.visibility = View.VISIBLE
                }

                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<DetailPurchaseAdapterDto>() {

        override fun areItemsTheSame(oldItem: DetailPurchaseAdapterDto, newItem: DetailPurchaseAdapterDto): Boolean {
            return oldItem.namePrice == newItem.namePrice
        }

        override fun areContentsTheSame(oldItem: DetailPurchaseAdapterDto, newItem: DetailPurchaseAdapterDto): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener{
        fun onClickItem(position: Int)
    }
}
