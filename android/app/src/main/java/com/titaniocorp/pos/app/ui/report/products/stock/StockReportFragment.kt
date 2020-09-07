package com.titaniocorp.pos.app.ui.report.products.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentPurchaseDashboardBinding
import com.titaniocorp.pos.databinding.FragmentStockReportBinding
import com.titaniocorp.pos.util.process
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@ExperimentalCoroutinesApi
class StockReportFragment: BaseFragment(),
    View.OnClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentStockReportBinding
    val mViewModel: StockReportViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_report, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(mViewModel)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@StockReportFragment
            viewModel = mViewModel
            val adapter = StockReportAdapter()
            recycler.adapter = adapter

            subscribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }


    private fun subscribeUi(adapter: StockReportAdapter){
        mViewModel.prices.observe(viewLifecycleOwner, Observer {
            it.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    binding.recycler.visibility = View.VISIBLE
                    binding.viewEmptyList.visibility = View.GONE
                    adapter.submitList(it.data)
                    mViewModel.calculateTotal()
                },
                onError = {
                    binding.recycler.visibility = View.GONE
                    binding.viewEmptyList.visibility = View.VISIBLE
                    mViewModel.calculateTotal()
                }
            )
        })
    }
}