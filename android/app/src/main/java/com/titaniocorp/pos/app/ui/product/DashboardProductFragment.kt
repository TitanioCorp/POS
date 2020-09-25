package com.titaniocorp.pos.app.ui.product

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentProductDashboardBinding
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardProductFragment: BaseFragment(),
    View.OnClickListener,
    SearchView.OnQueryTextListener{

    private lateinit var binding: FragmentProductDashboardBinding
    val viewModel: DashboardProductViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardProductFragment
            searchView.apply {
                setOnQueryTextListener(this@DashboardProductFragment)
                onActionViewExpanded()
                val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            }
            mSearchSrcTextView = searchView.findViewById(R.id.search_src_text)
            mSearchSrcTextView?.setOnItemClickListener { _, _, _, _ -> }

            val toolbar = (activity as AppCompatActivity).appbar
            nestedScrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                    val shouldShowToolbar = scrollY > toolbar.height
                    toolbar.isActivated = shouldShowToolbar
                }
            )

            val adapter = DashboardProductAdapter()
            recyclerAdapter = adapter
            recycler.adapter = recyclerAdapter


            subcribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_item -> {
                val direction = DashboardProductFragmentDirections.actionToDetailProductFragment()
                findNavController().navigate(direction)
            }
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.filter(newText ?: "")
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    private fun subcribeUi(adapter: DashboardProductAdapter){
        viewModel.products.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            val adapterSearch = ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, it.map {item -> item.name })
            binding.mSearchSrcTextView?.setAdapter(adapterSearch)
        })
    }
}