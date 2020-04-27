package com.titaniocorp.pos.app.ui.profit
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.app.viewmodel.model.ProfitViewModel
import com.titaniocorp.pos.databinding.ItemListProfitDashboardBinding

/**
 * Adaptador que maneja la lista de peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardProfitAdapter(private val listener: OnItemClickListener): ListAdapter<Profit, DashboardProfitAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_profit_dashboard, parent, false)
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

    class ViewHolder (private val binding: ItemListProfitDashboardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Profit, listener: View.OnClickListener){
            with(binding) {
                viewModel = ProfitViewModel(item)
                clickListener = listener
                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Profit>() {
        override fun areItemsTheSame(oldItem: Profit, newItem: Profit): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Profit, newItem: Profit): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener{
        fun onClickItem(position: Int)
        fun onClickRemoveItem(position: Int)
    }
}
