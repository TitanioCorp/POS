package com.titaniocorp.pos.app.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentCustomerDashboardBinding
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardCustomerFragment: BaseFragment(),
    View.OnClickListener,
    DashboardCustomerAdapter.OnItemClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentCustomerDashboardBinding
    val viewModel: DashboardCustomerViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(viewModel)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardCustomerFragment

            val toolbar = (activity as AppCompatActivity).appbar
            nestedScrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                    val shouldShowToolbar = scrollY > toolbar.height
                    toolbar.isActivated = shouldShowToolbar
                }
            )

            val adapter = DashboardCustomerAdapter(this@DashboardCustomerFragment)
            recycler.adapter = adapter

            subscribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_customer -> {
                DialogHelper.showCustomer(
                    activity,
                    {
                        viewModel.addCustomer(it)
                    }
                )
            }
        }
    }

    override fun onClickItem(position: Int) {
        viewModel.customers.value?.data?.let {
            DialogHelper.showCustomer(
                activity,
                {customer ->
                    viewModel.updateCustomer(customer)
                },
                item = it[position]
            )
        }
    }

    override fun onClickRemoveItem(position: Int) {
        DialogHelper.normal(
            activity,
            "Esta acción no podrá deshacerse.",
            "Eliminar cliente",
            "Eliminar",
            "Cancelar",
            {
                viewModel.deleteCustomer(position)
            },
            {})?.show()
    }

    private fun subscribeUi(adapter: DashboardCustomerAdapter){
        viewModel.customers.observe(viewLifecycleOwner, Observer {
            it.process(
                {adapter.submitList(it.data)},
                onLoading = {boolean -> setLoading(boolean)}
            )
        })
    }
}