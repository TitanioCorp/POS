package com.titaniocorp.pos.app.ui.pos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.PricePurchase
import com.titaniocorp.pos.app.model.dto.DashboardPOSAdapterDto
import com.titaniocorp.pos.app.viewmodel.model.DashboardPosAdapterViewModel
import com.titaniocorp.pos.databinding.ItemListPosDashboardProductBinding

/**
 * Adaptador que maneja la lista de peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardPosAdapter(private val listener: OnItemClickListener): ListAdapter<PricePurchase, DashboardPosAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_pos_dashboard_product, parent, false)
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

    class ViewHolder (private val binding: ItemListPosDashboardProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: PricePurchase, viewListener: View.OnClickListener){
            with(binding) {
                viewModel = DashboardPosAdapterViewModel(item)
                clickListener = viewListener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<PricePurchase>() {

        override fun areItemsTheSame(oldItem: PricePurchase, newItem: PricePurchase): Boolean {
            return oldItem.priceId == newItem.priceId
        }

        override fun areContentsTheSame(oldItem: PricePurchase, newItem: PricePurchase): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener{
        fun onClickItem(position: Int)
        fun onClickRemoveItem(position: Int)
    }
}
