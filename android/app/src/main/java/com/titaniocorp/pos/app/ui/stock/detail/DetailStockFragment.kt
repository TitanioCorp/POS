package com.titaniocorp.pos.app.ui.stock.detail

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentStockDetailBinding
import com.titaniocorp.pos.util.process
import javax.inject.Inject

/**
 * Fragmento que lista las compras de inventario
 * @author Juan Ortiz
 * @date 28/01/2019
 */
class DetailStockFragment: BaseFragment(){

    private lateinit var binding: FragmentStockDetailBinding
    val viewModel: DetailStockViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = viewLifecycleOwner

            val adapter = DetailStockAdapter()
            recycler.adapter = adapter

            subscribeUi(adapter)

            viewModel.setId(arguments?.getLong(ARG_STOCK_ID) ?: 0)
        }
    }

    private fun subscribeUi(adapter: DetailStockAdapter){
        viewModel.stock.observe(viewLifecycleOwner, Observer {
            it.process(
                {
                    binding.mStock = it.data
                    adapter.submitList(it.data?.prices)
                }
            )
        })
    }

    companion object{
        const val ARG_STOCK_ID = "ARG_STOCK_ID"
    }
}