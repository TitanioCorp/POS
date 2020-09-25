package com.titaniocorp.pos.app.ui.warehouse

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
import com.titaniocorp.pos.app.model.Payment
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentWarehouseDashboardBinding
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import javax.inject.Inject

/**
 * Fragmento que lista los pagos realizados
 * @author Juan Ortiz
 * @date 03/02/2020
 */
class DashboardWarehouseFragment: BaseFragment(), View.OnClickListener{

    private lateinit var binding: FragmentWarehouseDashboardBinding
    val viewModel: DashboardWarehouseViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_warehouse_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardWarehouseFragment

            val adapter = DashboardWarehouseAdapter(this@DashboardWarehouseFragment)
            recycler.adapter = adapter
            subcribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_item -> {
                val direction = DashboardWarehouseFragmentDirections.toAddPaymentWarehouseFragment()
                findNavController().navigate(direction)
            }

            R.id.view_content -> {
                val item = v.tag as Payment

                DialogHelper.normal(
                    activity,
                    item.observation,
                    "Observación del movimiento: ${item.id}",
                    "Aceptar",
                    positiveCallback = {}
                )?.show()
            }
        }
    }

    private fun subcribeUi(adapter: DashboardWarehouseAdapter){
        viewModel.payments.observe(viewLifecycleOwner, Observer {
            it.process(
                { adapter.submitList(it.data) },
                onLoading = {boolean -> setLoading(boolean)}
            )
        })
    }
}