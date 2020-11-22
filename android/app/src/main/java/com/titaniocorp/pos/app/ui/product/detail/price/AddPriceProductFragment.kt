package com.titaniocorp.pos.app.ui.product.detail.price

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.app.ui.base.adapter.CategorySpinnerAdapter
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.app.ui.profit.initial.dialog.showInitialProfitDialog
import com.titaniocorp.pos.databinding.FragmentInitialProfitDashboardBinding
import com.titaniocorp.pos.databinding.FragmentProductAddPriceBinding
import com.titaniocorp.pos.util.*
import com.titaniocorp.pos.util.ui.DialogHelper
import com.titaniocorp.pos.util.ui.showDefaultDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

/**
 * Fragmento para agregar nuevo producto
 * @author Juan Ortiz
 * @date 20/11/2020
 */

@ExperimentalCoroutinesApi
class AddPriceProductFragment: BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentProductAddPriceBinding
    val viewModel: AddPriceProductViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_options_product_add_price, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_add_price, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = this@AddPriceProductFragment
            clickListener = this@AddPriceProductFragment
            mViewModel = viewModel
            subscribeUi()
        }
    }

    private fun subscribeUi(){
        viewModel.initialProfits.runLiveData({
            context?.let{context ->
                binding.spinnerInitialProfits.adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    it.data?.map {item -> "${item.percent}% | ${item.name}" } ?: listOf()
                )

                binding.spinnerInitialProfits.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                        viewModel.selectInitialProfit(position)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        })

        binding.inputCost.addMoneyTextWatcher{cost ->
            viewModel.setCost(cost)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_item -> {

            }
        }
    }
}