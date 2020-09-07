package com.titaniocorp.pos.app.ui.warehouse.add

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentWarehousePaymentAddBinding
import com.titaniocorp.pos.util.addMoneyTextWatcher
import com.titaniocorp.pos.util.getValueMoney
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import com.titaniocorp.pos.util.validations.ValidateType
import com.titaniocorp.pos.util.validations.ValidateUtil
import com.titaniocorp.pos.util.validations.toValidate
import javax.inject.Inject

/**
 * Fragmento para agregar un nuevo pago
 * @author Juan Ortiz
 * @date 03/02/2020
 */
class AddPaymentWarehouseFragment: BaseFragment(),
    View.OnClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentWarehousePaymentAddBinding
    val viewModel: AddPaymentWarehouseViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_warehouse_payment_add, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(viewModel)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@AddPaymentWarehouseFragment
            mViewModel = viewModel

            inputValue.addMoneyTextWatcher()
            spinnerTypePayment.adapter = ArrayAdapter(binding.root.context, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.array_type_payments))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_options_warehouse_payment_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_save_payment -> {
            if(validate()){
                DialogHelper.normal(
                    activity,
                    "Esta acción no podrá deshacerse.",
                    "Guardar movimiento",
                    "Guardar",
                    "Cancelar",
                    {
                        viewModel.payment.value = when(binding.spinnerTypePayment.selectedItem as String){
                            "Egreso" -> { binding.inputValue.text.toString().getValueMoney() * -1 }
                            else -> { binding.inputValue.text.toString().getValueMoney() }
                        }
                        viewModel.payment.observation = binding.inputObservation.text.toString()

                        viewModel.addStock().observe(viewLifecycleOwner, Observer {resource ->
                            resource.process(
                                { navigateUp() },
                                { viewModel.setError(resource.code, resource.message) },
                                {boolean -> setLoading(boolean)}
                            )
                        })
                    },
                    {}
                )?.show()
            }
            true
        }
        else -> { item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item) }
    }

    override fun onClick(v: View?) {}

    private fun validate(): Boolean{
        return ValidateUtil.inputs(
            binding.inputValue.toValidate(ValidateType.MONEY),
            binding.inputObservation.toValidate()
        )
    }

    private fun navigateUp(){
        DialogHelper.normal(
            activity,
            "Acepte para volver atrás",
            "Guardado correctamente",
            "Aceptar",
            positiveCallback = {
               findNavController().navigateUp()
            }
        )?.also {
            it.setCancelable(false)
            it.show()
        }
    }
}