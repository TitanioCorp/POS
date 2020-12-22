package com.titaniocorp.pos.app.ui.product.detail.price

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.snackbar.Snackbar
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.app.ui.base.adapter.CategorySpinnerAdapter
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.app.ui.product.detail.DetailProductViewModel
import com.titaniocorp.pos.app.ui.profit.initial.dialog.showInitialProfitDialog
import com.titaniocorp.pos.databinding.FragmentInitialProfitDashboardBinding
import com.titaniocorp.pos.databinding.FragmentProductAddPriceBinding
import com.titaniocorp.pos.util.*
import com.titaniocorp.pos.util.ui.DialogHelper
import com.titaniocorp.pos.util.ui.showDefaultDialog
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.ValidateUtil
import com.titaniocorp.pos.util.validations.toValidate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

/**
 * Fragmento para agregar nuevo producto
 * @author Juan Ortiz
 * @date 20/11/2020
 */

@ExperimentalCoroutinesApi
class AddPriceProductFragment: BaseFragment(), View.OnClickListener {
    val args: AddPriceProductFragmentArgs by navArgs()

    private lateinit var binding: FragmentProductAddPriceBinding
    val viewModel: AddPriceProductViewModel by viewModels { viewModelFactory }
    private val detailProductViewModel: DetailProductViewModel by navGraphViewModels(R.id.nav_graph_detail_product) { viewModelFactory }

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

            inputCost.addMoneyTextWatcher{cost ->
                viewModel.setCost(cost)
            }

            subscribeUi()
        }

        if(args.position >= 0){
            detailProductViewModel.getPrice(args.position).let{
                viewModel.updatePrice(it)
                binding.inputName.setText(it.name)
                binding.inputSku.setText(it.sku)
                binding.inputStock.setText(it.stock.toString())
                binding.inputCost.setText(it.cost.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_save_price -> {
            if(validate()){
                with(viewModel.price){
                    name = binding.inputName.text.toString()
                    sku = binding.inputSku.text.toString()
                }

                if(args.position >= 0){
                    detailProductViewModel.updatePrice(viewModel.price).runLiveData({
                        findNavController().navigateUp()
                    })
                }else{
                    detailProductViewModel.addPrice(viewModel.price)
                    findNavController().navigateUp()
                }
            }
            true
        }
        else -> { item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item) }
    }

    override fun onClick(v: View?) {
        when(v?.id){
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

                if((viewModel.price.initialProfitId ?: 0) > 0){
                    it.data?.forEachIndexed { index, initialProfit ->
                        if(initialProfit.id == viewModel.price.initialProfitId){
                            binding.spinnerInitialProfits.setSelection(index)
                        }
                    }
                } else {
                    binding.spinnerInitialProfits.setSelection(0)
                }

                binding.spinnerInitialProfits.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                        viewModel.selectInitialProfit(position)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        })
    }

    private fun validate(): Boolean{
        val isInputsSuccess = ValidateUtil.inputs(
            binding.inputName.toValidate(),
            binding.inputSku.toValidate(),
            binding.inputCost.toValidate(ValidateType.MONEY)
        )

        return isInputsSuccess && (viewModel.price.initialProfitId ?: 0) > 0
    }
}