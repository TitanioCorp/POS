package com.titaniocorp.pos.app.ui.stock.add

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.PriceStock
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentStockAddBinding
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import com.titaniocorp.pos.util.ui.DialogStockHelper
import javax.inject.Inject

/**
 * Fragmento que lista las compras de inventario
 * @author Juan Ortiz
 * @date 28/01/2019
 */
class AddStockFragment: BaseFragment(),
    View.OnClickListener ,
    SearchView.OnQueryTextListener{

    private lateinit var binding: FragmentStockAddBinding
    val viewModel: AddStockViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_add, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@AddStockFragment
            mViewModel = viewModel

            val adapter = AddStockAdapter(this@AddStockFragment)
            adapter.submitList(viewModel.stock.prices)
            recycler.adapter = adapter

            searchView.apply {
                setOnQueryTextListener(this@AddStockFragment)
                onActionViewExpanded()
                val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            }
            searchView.clearFocus()

            mSearchSrcTextView = searchView.findViewById(R.id.search_src_text)
            mSearchSrcTextView?.onItemClickListener = onSearch()

            subscribeUi(adapter)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_options_stock_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_save_stock -> {
            if(viewModel.validateStock()){
                DialogHelper.normal(
                    activity,
                    "Esta acción no podrá deshacerse.",
                    "Guardar compra de inventario",
                    "Guardar",
                    "Cancelar",
                    {
                        viewModel.stock.purchaseRef = binding.inputPurchaseRef.text.toString()
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_item -> {

            }

            R.id.view_content -> {
                val position = v.tag as Int

                DialogHelper.normal(
                    activity,
                    "Esta acción no podrá deshacerse.",
                    "Remover item",
                    "Remover",
                    "Cancelar",
                    {
                        if(viewModel.removePriceToStock(position)){
                            (binding.recycler.adapter as AddStockAdapter).notifyItemRemoved(position)
                        }
                    },
                    {})?.show()
            }
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            if(it.isNotEmpty()){
                viewModel.search(it)
            }
        }
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    private fun onSearch() = AdapterView.OnItemClickListener { _, _, position, _ ->
        viewModel.selectProduct(position)
    }

    private fun subscribeUi(adapter: AddStockAdapter){
        viewModel.searchedList.observe(viewLifecycleOwner, Observer {
            it.process(
                {
                    context?.let {context ->
                        it?.data?.let {list ->
                            binding.mSearchSrcTextView?.setAdapter(
                                ArrayAdapter(
                                    context,
                                    android.R.layout.simple_dropdown_item_1line,
                                    list.map {item -> "${item.productName} - ${item.priceName}" }
                                )
                            )
                        } ?: run {
                            binding.mSearchSrcTextView?.setAdapter(
                                ArrayAdapter(
                                    context,
                                    android.R.layout.simple_dropdown_item_1line,
                                    listOf<String>()
                                )
                            )
                        }
                    }
                }
            )
        })

        viewModel.searchProduct.observe(viewLifecycleOwner, Observer { response ->
            response.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    response?.data?.let {product ->
                        DialogStockHelper.addPrice(
                            activity,
                            {priceStock ->
                                adapter.notifyItemInserted(viewModel.addPriceToStock(priceStock))
                            },
                            product = product,
                            priceId = viewModel.searchedPriceId
                        )
                    }
                }
            )
        })
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