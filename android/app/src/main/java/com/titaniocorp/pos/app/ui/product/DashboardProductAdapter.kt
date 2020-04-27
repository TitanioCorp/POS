package com.titaniocorp.pos.app.ui.product
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Product
import com.titaniocorp.pos.app.viewmodel.model.ProductViewModel
import com.titaniocorp.pos.databinding.ItemListProductDashboardProductBinding

/**
 * Adaptador que maneja la lista de peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardProductAdapter: ListAdapter<Product, DashboardProductAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_product_dashboard_product, parent, false)
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

    private fun createOnClickListener(item: Product): View.OnClickListener {
        return View.OnClickListener {
            val direction = DashboardProductFragmentDirections.actionToDetailProductFragment(item.id ?: 0)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder (private val binding: ItemListProductDashboardProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Product, listener: View.OnClickListener){
            with(binding) {
                viewModel = ProductViewModel(item)
                clickListener = listener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }
}
