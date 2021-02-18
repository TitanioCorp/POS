package com.titaniocorp.pos.app.ui.purchase

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.domain.Customer
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentPurchaseDashboardBinding
import com.titaniocorp.pos.util.DateUtil
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DatePickerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */

@ExperimentalCoroutinesApi
class DashboardPurchaseFragment: BaseFragment(),
    View.OnClickListener,
    DatePickerDialog.OnDateSetListener{

    private lateinit var binding: FragmentPurchaseDashboardBinding
    val viewModel: DashboardPurchaseViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardPurchaseFragment

            val adapter = DashboardPurchaseAdapter()
            recycler.adapter = adapter

            context?.let{
                val dateAdapter = ArrayAdapter(
                    it,
                    android.R.layout.simple_list_item_1,
                    resources.getStringArray(R.array.purchase_filter_date)
                )

                val typeAdapter = ArrayAdapter(
                    it,
                    android.R.layout.simple_list_item_1,
                    resources.getStringArray(R.array.purchase_filter_type)
                )

                spinnerDate.adapter = dateAdapter
                spinnerType.adapter = typeAdapter

                spinnerDate.setSelection(0, false)
                spinnerType.setSelection(0, false)
            }

            subscribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateRange = DateUtil.getRange(year, month, dayOfMonth)
        viewModel.filterByDate(dateRange.first, dateRange.second)
    }

    private fun subscribeUi(adapter: DashboardPurchaseAdapter){
        viewModel.customers.observe(viewLifecycleOwner, Observer {response ->
            response.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    response?.data?.let {listResponse ->
                        context?.let {

                            val list = listResponse.toMutableList().apply{
                                add(0, Customer())
                            }

                            binding.spinnerCustomer.adapter =
                                ArrayAdapter(
                                    it,
                                    android.R.layout.simple_list_item_1,
                                    list.map {item ->
                                        if(item.id == 0L){
                                            "Todos"
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

        viewModel.purchases.observe(viewLifecycleOwner, Observer {
            it.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    binding.recycler.visibility = View.VISIBLE
                    binding.viewEmptyList.visibility = View.GONE
                    adapter.submitList(it.data)
                    viewModel.searchFinished()
                },
                onError = {
                    binding.recycler.visibility = View.GONE
                    binding.viewEmptyList.visibility = View.VISIBLE
                    viewModel.searchFinished()
                }
            )
        })

        binding.spinnerDate.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                when(position){
                    0 -> {
                        val dateRange = DateUtil.todayRange()
                        viewModel.filterByDate(dateRange.first, dateRange.second)
                    }
                    1 -> {
                        viewModel.filterByDate(0, 0)
                    }
                    2 -> {
                        selectDate()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                viewModel.filterByType(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerCustomer.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                viewModel.filterByCustomer(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    private fun selectDate(){
        DatePickerFragment(this).show(parentFragmentManager, "DatePickerFragment")
    }
}