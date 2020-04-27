package com.titaniocorp.pos.app.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentCategoryDashboardBinding
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardCategoryFragment: BaseFragment(),
    View.OnClickListener,
    DashboardCategoryAdapter.OnItemClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentCategoryDashboardBinding
    val viewModel: DashboardCategoryViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(viewModel)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardCategoryFragment

            val adapter = DashboardCategoryAdapter(this@DashboardCategoryFragment)
            recycler.adapter = adapter
            subcribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_item -> {
                DialogHelper.newCategory(
                    activity,
                    {profit ->
                        viewModel.insert(profit)
                    }
                )
            }
        }
    }

    override fun onClickItem(position: Int) {
        viewModel.categories.value?.data?.let {
            DialogHelper.newCategory(
                activity,
                {profit ->
                    viewModel.update(profit)
                },
                item = it[position]
            )
        }
    }

    override fun onClickRemoveItem(position: Int) {
        DialogHelper.normal(
            activity,
            "Esta acción no podrá deshacerse.",
            "Eliminar categoria",
            "Eliminar",
            "Cancelar",
            {viewModel.delete(position)},
            {})?.show()
    }

    private fun subcribeUi(adapter: DashboardCategoryAdapter){
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            it.process(
                { adapter.submitList(it.data) },
                onLoading = {boolean -> setLoading(boolean)}
            )
        })
    }
}