package com.titaniocorp.pos.app.ui.pos.addProduct

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.PriceAddProductAdapter
import com.titaniocorp.pos.app.ui.base.adapter.DialogAddProductAdapter
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.app.ui.pos.POSViewModel
import com.titaniocorp.pos.databinding.FragmentPosAddProductBinding
import com.titaniocorp.pos.util.AppCode
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_EMPTY_PROFIT_LIST
import com.titaniocorp.pos.util.process
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class AddProductPOSFragment: BaseFragment(), View.OnClickListener{

    private lateinit var binding: FragmentPosAddProductBinding
    val viewModel: POSViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pos_add_product, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val positionSelected = it.getInt(ARG_POSITION)

            with(binding) {
                lifecycleOwner = viewLifecycleOwner
                clickListener = this@AddProductPOSFragment
                viewModel = this@AddProductPOSFragment.viewModel

                spinnerProfits.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                        this@AddProductPOSFragment.viewModel.profits.value?.data?.get(position)?.let {profit ->
                            this@AddProductPOSFragment.viewModel.selectProfit(profit)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                val adapter = DialogAddProductAdapter(
                    this@AddProductPOSFragment.viewModel.searchedList.value?.data?.get(positionSelected)?.priceId,
                    object : DialogAddProductAdapter.DialogAddProductListener{
                    override fun selectedPrice(priceId: Long, cost: Double, stock: Int, isInitialProfit: Boolean) {
                        this@AddProductPOSFragment.viewModel.selectPrice(priceId, cost, stock, isInitialProfit)
                    }
                })

                recycler.adapter = adapter
                subcribeUi(adapter)
            }

            viewModel.selectProduct(positionSelected)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_options_pos_add_product, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_add_price_purchase -> {
            when(val code = viewModel.addProduct()){
                AppCode.VALIDATE_SUCCESS -> {
                    val direction = AddProductPOSFragmentDirections.toDashboardPosFragment()
                    findNavController().navigate(direction)
                }

                else -> { viewModel.setError(code) }
            }
            true
        }
        else -> { item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item) }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_add -> {viewModel.addQuantity()}
            R.id.button_remove -> {viewModel.removeQuantity()}
        }
    }

    private fun subcribeUi(adapter: DialogAddProductAdapter){
        val toolbar = (activity as AppCompatActivity).appbar
        binding.nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                val shouldShowToolbar = scrollY > toolbar.height
                toolbar.isActivated = shouldShowToolbar
            }
        )

        viewModel.searchProduct.observe(viewLifecycleOwner, Observer { response ->
            response.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    response?.data?.let {product ->
                        viewModel.setProductSelected(product)

                        adapter.submitList(viewModel.product.prices.map {price ->
                            PriceAddProductAdapter(price, false)
                        })

                        lifecycleScope.launch {
                            viewModel.searchedList.value?.data?.get(arguments?.getInt(ARG_POSITION) ?: 0)?.let {_ ->
                                //val profit = product.prices.find { it.id == searchSelected.priceId }
                            }


                            viewModel.getCategory(product.categoryId).also {
                                it.process({ binding.category = it.data }, null, null)
                            }
                        }
                    }
                }
            )
        })

        viewModel.profits.observe(viewLifecycleOwner, Observer {response ->
            response.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    response?.data?.let {profitList ->
                        if(profitList.isNotEmpty()){
                            context?.let {
                                binding.spinnerProfits.adapter =
                                    ArrayAdapter(
                                        it,
                                        android.R.layout.simple_list_item_1,
                                        profitList.map {item -> "${item.percent}% - ${item.name}" }
                                    )

                                viewModel.selectProfit(profitList[0])
                            }
                        }else{
                            viewModel.setError(ERROR_EMPTY_PROFIT_LIST)
                        }
                    }
                },
                onError = {
                    viewModel.setError(ERROR_EMPTY_PROFIT_LIST)
                }
            )
        })
    }

    companion object{
        const val ARG_POSITION = "ARG_POSITION"
    }
}