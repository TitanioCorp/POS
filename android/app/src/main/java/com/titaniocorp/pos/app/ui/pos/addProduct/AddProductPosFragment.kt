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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.onNavDestinationSelected
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.app.model.PriceAddProductAdapter
import com.titaniocorp.pos.app.ui.MainActivity
import com.titaniocorp.pos.app.ui.base.adapter.DialogAddProductAdapter
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.app.ui.pos.PurchasePosViewModel
import com.titaniocorp.pos.databinding.FragmentPosAddProductBinding
import com.titaniocorp.pos.util.AppCode
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_EMPTY_PROFIT_LIST
import com.titaniocorp.pos.util.process
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */

@ExperimentalCoroutinesApi
class AddProductPosFragment: BaseFragment(), View.OnClickListener{

    private val args: AddProductPosFragmentArgs by navArgs()
    private lateinit var binding: FragmentPosAddProductBinding
    val viewModel: AddProductPosViewModel by viewModels { viewModelFactory }
    val purchaseViewModel: PurchasePosViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_options_pos_add_product, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pos_add_product, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@AddProductPosFragment
            mViewModel = viewModel
        }

        val adapter = DialogAddProductAdapter(
            args.priceId,
            object : DialogAddProductAdapter.DialogAddProductListener{
                override fun selectedPrice(priceId: Long, cost: Double, stock: Int, initialProfitId: Long?) {
                    viewModel.selectPrice(priceId, cost, stock, initialProfitId ?: InitialProfit.DEFAULT_INITIAL_PROFIT_ID)
                }
            }
        )

        binding.recycler.adapter = adapter

        subscribeUi(adapter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_add_price_purchase -> {
            when(val code = validate()){
                AppCode.VALIDATE_SUCCESS -> {
                    val direction = AddProductPosFragmentDirections.toDashboardPosFragment()
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

    private fun subscribeUi(adapter: DialogAddProductAdapter){
        val toolbar = (activity as MainActivity).binding.appbar
        binding.nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                val shouldShowToolbar = scrollY > toolbar.height
                toolbar.isActivated = shouldShowToolbar
            }
        )

        binding.spinnerProfits.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                viewModel.profits.value?.data?.get(position)?.let { profit ->
                    viewModel.selectProfit(profit)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewModel.getProduct(args.productId).runLiveData({
            it.data?.let{product ->
                viewModel.setGotProduct(product)

                adapter.submitList(product.prices.map {price ->
                    PriceAddProductAdapter(price, false)
                })

                lifecycleScope.launch {
                    viewModel.getCategory(product.categoryId).also {resource ->
                        resource.process(
                            {
                                binding.category = resource.data
                            },
                            null,
                            null
                        )
                    }
                }
            }
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
                                        android.R.layout.simple_spinner_dropdown_item,
                                        profitList.map {item -> "${item.percent}% | ${item.name}" }
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

    fun validate(): Int{
        if(viewModel.pricePurchase.quantity !=0 ){
            val totalQuantity = viewModel.product.prices.find { it.id == viewModel.pricePurchase.priceId }?.stock ?: 0
            var currentQuantity = 0

            purchaseViewModel.purchase.prices.forEach {
                if(it.priceId == viewModel.pricePurchase.priceId){
                    currentQuantity += it.quantity
                }
            }

            return if(viewModel.pricePurchase.quantity + currentQuantity > totalQuantity){
                AppCode.ERROR_QUANTITY_PRICE_PURCHASE
            }else{
                with(viewModel.pricePurchase){
                    productName = viewModel.product.name
                    priceName = viewModel.product.prices.find { it.id == priceId }?.name ?: ""
                    profitName = viewModel.profitSelected.name
                }

                purchaseViewModel.addProduct(viewModel.pricePurchase)

                AppCode.VALIDATE_SUCCESS
            }
        }else{
            return AppCode.ERROR_ZERO_QUANTITY_PRICE
        }
    }
}