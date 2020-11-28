package com.titaniocorp.pos.app.ui.pos.purchase

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.titaniocorp.pos.R
import com.titaniocorp.pos.database.entity.CustomerEntity
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.app.ui.pos.PurchasePosViewModel
import com.titaniocorp.pos.databinding.FragmentPosPurchaseBinding
import com.titaniocorp.pos.util.addMoneyTextWatcher
import com.titaniocorp.pos.util.getValueMoney
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@ExperimentalCoroutinesApi
class PurchasePosFragment: BaseFragment(), View.OnClickListener{

    private lateinit var binding: FragmentPosPurchaseBinding
    val viewModel: PurchasePosViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pos_purchase, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mViewModel = viewModel
        binding.clickListener = this
        binding.inputAbono.addMoneyTextWatcher()

        binding.spinnerCustomer.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                viewModel.selectCustomer(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        subscribeUi()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_options_pos_transaction, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){

        R.id.action_save_purchase -> {
            if(validate()){
                binding.inputAbono.text.toString().let {
                    if(it.isNotEmpty()){
                        viewModel.addCreditPayment(it.getValueMoney())
                    }
                }
                saveTransaction()
            }
            true
        }

        R.id.action_open_calculator -> {
            true
        }

        else -> { item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item) }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_transaction -> {
            }

            R.id.button_add_adjustment -> {
                DialogHelper.addAdjustment(activity, {
                    viewModel.setAdjustment(it)
                }, total = viewModel.purchase.total)?.show()
            }
        }
    }

    private fun subscribeUi(){
        val toolbar = (activity as AppCompatActivity).appbar
        binding.nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                val shouldShowToolbar = scrollY > toolbar.height
                toolbar.isActivated = shouldShowToolbar
            }
        )

        viewModel.customers.observe(viewLifecycleOwner, Observer {response ->
            response.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    response?.data?.let {listResponse ->
                        context?.let {

                            val list = listResponse.toMutableList().apply{
                                add(0,
                                    CustomerEntity()
                                )
                            }

                            binding.spinnerCustomer.adapter =
                                ArrayAdapter(
                                    it,
                                    android.R.layout.simple_list_item_1,
                                    list.map {item ->
                                        if(item.id == 0L){
                                            "Ninguno"
                                        }else{
                                            "${item.name} - ${item.dni}"
                                        }
                                    }
                                )
                        }
                    }
                }
            )
        })
    }

    private fun validate(): Boolean{
        return viewModel.validatePurchase()
    }

    private fun saveTransaction(){
        DialogHelper.normal(
            activity,
            "Esta acción no podrá deshacerse.",
            "Guardar transacción",
            "Facturar",
            "Cancelar",
            {
                viewModel.addPurchase().observe(viewLifecycleOwner, Observer {resource ->
                    resource.process(
                        onLoading = {boolean -> setLoading(boolean)},
                        onSuccess = {
                            DialogHelper.normal(
                                activity,
                                "El número de la factura generada es: #${resource.data?.first}.",
                                "Transacción exitosa",
                                "Aceptar",
                                positiveCallback = {
                                    val direction = PurchasePosFragmentDirections.toDashboardPosFragment()
                                    findNavController().navigate(direction)

                                    viewModel.resetPurchase()
                                }
                            )?.show()
                        },
                        onError = {
                            viewModel.setError(resource.code, resource.message)
                        }
                    )
                })
            },
            {})?.show()
    }
}