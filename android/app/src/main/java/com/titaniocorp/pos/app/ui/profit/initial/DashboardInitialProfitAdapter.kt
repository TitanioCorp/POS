package com.titaniocorp.pos.app.ui.profit.initial
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.databinding.ItemInitialProfitDashboardBinding
import com.titaniocorp.pos.util.Logger
import com.titaniocorp.pos.util.toJson
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Adaptador que maneja la lista de geanancias iniciales
 * @author Juan Ortiz
 * @date 20/11/2020
 */
@ExperimentalCoroutinesApi
class DashboardInitialProfitAdapter(private val listener: OnItemClickListener): ListAdapter<InitialProfit, DashboardInitialProfitAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_initial_profit_dashboard, parent, false)
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

    class ViewHolder (private val binding: ItemInitialProfitDashboardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: InitialProfit, listener: OnItemClickListener){
            with(binding) {
                this.item = item
                clickListener = View.OnClickListener {
                    when(it.id){
                        R.id.view_content -> {
                            listener.onClickItem(item)
                        }

                        R.id.button_remove -> {
                            listener.onClickRemoveItem(item)
                        }
                    }
                }
                executePendingBindings()
            }
        }
    }
    private class DiffCallback : DiffUtil.ItemCallback<InitialProfit>() {
        override fun areItemsTheSame(oldItem: InitialProfit, newItem: InitialProfit): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: InitialProfit, newItem: InitialProfit): Boolean =
            oldItem == newItem
    }


    interface OnItemClickListener{
        fun onClickItem(item: InitialProfit)
        fun onClickRemoveItem(item: InitialProfit)
    }
}
