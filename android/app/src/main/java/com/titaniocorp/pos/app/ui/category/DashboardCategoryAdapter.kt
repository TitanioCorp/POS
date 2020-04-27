package com.titaniocorp.pos.app.ui.category
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Category
import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.app.viewmodel.model.CategoryViewModel
import com.titaniocorp.pos.app.viewmodel.model.ProfitViewModel
import com.titaniocorp.pos.databinding.ItemListCategoryDashboardBinding
import com.titaniocorp.pos.databinding.ItemListProfitDashboardBinding

/**
 * Adaptador que maneja la lista de categorias
 * @version 1.0
 * @author Juan Ortiz
 * @date 14/01/2019
 */
class DashboardCategoryAdapter(private val listener: OnItemClickListener): ListAdapter<Category, DashboardCategoryAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_category_dashboard, parent, false)
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

    class ViewHolder (private val binding: ItemListCategoryDashboardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Category, listener: View.OnClickListener){
            with(binding) {
                viewModel = CategoryViewModel(item)
                clickListener = listener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener{
        fun onClickItem(position: Int)
        fun onClickRemoveItem(position: Int)
    }
}
