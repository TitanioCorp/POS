package com.titaniocorp.pos.app.ui.pos

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentPosDashboardBinding
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */

class DashboardPosFragment: BaseFragment(),
    View.OnClickListener,
    SearchView.OnQueryTextListener,
    DashboardPOSAdapter.OnItemClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentPosDashboardBinding
    val viewModel: POSViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPosDashboardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(viewModel)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            mViewModel = viewModel
            clickListener = this@DashboardPosFragment

            searchView.apply {
                setOnQueryTextListener(this@DashboardPosFragment)
                onActionViewExpanded()
                val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            }
            searchView.clearFocus()

            mSearchSrcTextView = searchView.findViewById(R.id.search_src_text)
            mSearchSrcTextView?.setOnItemClickListener { _, _, position, _ ->
                val direction = DashboardPosFragmentDirections.toAddProductPOSFragment(position)
                findNavController().navigate(direction)
            }

            val adapter = DashboardPOSAdapter(this@DashboardPosFragment)
            adapter.submitList(viewModel.adapterPriceList)
            recycler.adapter = adapter

            subcribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_transaction -> {
                val direction = DashboardPosFragmentDirections.toTransactionPOSFragment()
                findNavController().navigate(direction)
            }

            R.id.button_barcode -> {

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

    override fun onQueryTextSubmit(query: String?): Boolean = false
    override fun onClickItem(position: Int) {}

    override fun onClickRemoveItem(position: Int) {
        DialogHelper.normal(
            activity,
            "Esta acción no podrá deshacerse.",
            "Remover el producto de la lista",
            "Remover",
            "Cancelar",
            { viewModel.removeAdapterItem(position) },
            {})?.show()
    }

    private fun subcribeUi(adapter: DashboardPOSAdapter){
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

        viewModel.adapterUpdated.observe(viewLifecycleOwner, Observer {
            when(it.first){
                1 -> {adapter.notifyItemInserted(it.second)}

                2 -> {adapter.notifyItemChanged(it.second)}

                3 -> {
                    adapter.notifyItemRemoved(it.second)
                    adapter.notifyItemRangeChanged(it.second, adapter.itemCount)
                }

                4 -> { adapter.notifyDataSetChanged() }
            }
        })
    }
}