package com.titaniocorp.pos.app.ui.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentStockDashboardBinding
import com.titaniocorp.pos.util.process
import javax.inject.Inject

/**
 * Fragmento que lista las compras de inventario
 * @author Juan Ortiz
 * @date 28/01/2019
 */
class DashboardStockFragment: BaseFragment(), View.OnClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentStockDashboardBinding
    val viewModel: DashboardStockViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(viewModel)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardStockFragment

            val adapter = DashboardStockAdapter()
            recycler.adapter = adapter
            subcribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_item -> {
                val direction = DashboardStockFragmentDirections.toAddtockFragment()
                findNavController().navigate(direction)
            }
        }
    }

    private fun subcribeUi(adapter: DashboardStockAdapter){
        viewModel.stocks.observe(viewLifecycleOwner, Observer {
            it.process(
                { adapter.submitList(it.data) },
                onLoading = {boolean -> setLoading(boolean)}
            )
        })
    }
}