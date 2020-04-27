package com.titaniocorp.pos.app.ui.product.detail
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Price
import com.titaniocorp.pos.app.viewmodel.model.PriceViewModel
import com.titaniocorp.pos.databinding.ItemListProductDetailBinding

/**
 * Adaptador que maneja la lista de peliculas
 * @author Juan Ortiz
 * @date 19/12/2019
 */
class DetailProductAdapter(private val listener: DetailProductItemListener): ListAdapter<Price, DetailProductAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_product_detail, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { price ->
            with(holder) {
                itemView.tag = price
                bind(price, createOnClickListener(position))
            }
        }
    }

    private fun createOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            when(it.id){
                R.id.card_content -> listener.onClickItem(position)
                R.id.button_remove -> listener.onRemoveItem(position)
            }
        }
    }

    class ViewHolder (private val binding: ItemListProductDetailBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(price: Price, listener: View.OnClickListener){
            with(binding) {
                if(price.active){
                    root.visibility = View.VISIBLE
                }else{
                    root.visibility = View.GONE
                }
                viewModel = PriceViewModel(price)
                clickListener = listener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Price>() {
        override fun areItemsTheSame(oldItem: Price, newItem: Price): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Price, newItem: Price): Boolean {
            return oldItem == newItem
        }
    }

    interface DetailProductItemListener{
        fun onClickItem(position: Int)
        fun onRemoveItem(position: Int)
    }
}
