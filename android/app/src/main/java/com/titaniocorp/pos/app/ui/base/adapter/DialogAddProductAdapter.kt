package com.titaniocorp.pos.app.ui.base.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.PriceAddProductAdapter
import com.titaniocorp.pos.app.viewmodel.model.PriceViewModel
import com.titaniocorp.pos.databinding.ItemListDialogAddProductBinding
import timber.log.Timber

/**
 * Adaptador que maneja la lista de peliculas
 * @author Juan Ortiz
 * @date 26/12/2019
 */
class DialogAddProductAdapter(private val selectedPriceId: Long?, val listener: DialogAddProductListener):
    ListAdapter<PriceAddProductAdapter, DialogAddProductAdapter.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_dialog_add_product,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { item ->

            currentList.find { it.price.id == selectedPriceId }?.let{
                if(!isSelectedItem()){
                    it.isSelected = true
                    listener.selectedPrice(it.price.id, it.price.cost, it.price.stock, it.price.initialProfitId)
                }
            } ?: run{
                if(position == 0){
                    if(!isSelectedItem()){
                        item.isSelected = true
                        listener.selectedPrice(item.price.id, item.price.cost, item.price.stock, item.price.initialProfitId)
                    }
                }
            }

            with(holder) {
                itemView.tag = item.price.id
                bind(
                    item,
                    createOnClickListener(position)
                )
            }
        }
    }

    private fun isSelectedItem():Boolean{
        var isSelected = false
        for(item in currentList){
            if(item.isSelected){
                isSelected = true
            }
        }

        return isSelected
    }

    private fun createOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
            when(it.id){
                R.id.check_box -> {
                    Timber.i("Position: $position - isChecked: true")
                    for((index, item) in currentList.withIndex()){
                        if (item.isSelected && index != position){
                            item.isSelected = false
                        }

                        if(item.price.id == currentList[position].price.id){
                            item.isSelected = !item.isSelected
                            if(item.isSelected){
                                listener.selectedPrice(item.price.id, item.price.cost, item.price.stock, item.price.initialProfitId)
                            }
                        }
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    class ViewHolder (private val binding: ItemListDialogAddProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: PriceAddProductAdapter, listener: View.OnClickListener){
            with(binding) {
                viewModel = PriceViewModel(data.price)
                isSelected = data.isSelected
                //checkBox.setOnCheckedChangeListener(checkboxListener)
                clickListener = listener

                executePendingBindings()
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<PriceAddProductAdapter>() {
        override fun areItemsTheSame(oldItem: PriceAddProductAdapter, newItem: PriceAddProductAdapter): Boolean {
            return oldItem.price.id == newItem.price.id
        }

        override fun areContentsTheSame(oldItem: PriceAddProductAdapter, newItem: PriceAddProductAdapter): Boolean {
            return oldItem.price == newItem.price
        }
    }

    interface DialogAddProductListener{
        fun selectedPrice(priceId: Long, cost: Double, stock: Int, initialProfitId: Long?)
    }
}
