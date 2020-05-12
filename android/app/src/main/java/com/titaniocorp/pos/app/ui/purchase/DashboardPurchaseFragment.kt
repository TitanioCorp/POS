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
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentPurchaseDashboardBinding
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DatePickerFragment
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardPurchaseFragment: BaseFragment(),
    View.OnClickListener,
    DatePickerDialog.OnDateSetListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentPurchaseDashboardBinding
    val viewModel: DashboardPurchaseViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(viewModel)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardPurchaseFragment

            val adapter = DashboardPurchaseAdapter()
            recycler.adapter = adapter

            context?.let{
                val typesAdapter = ArrayAdapter(
                    it,
                    android.R.layout.simple_list_item_1,
                    resources.getStringArray(R.array.purchase_filters)
                )

                spinnerType.adapter = typesAdapter

                val customerAdapter = ArrayAdapter(
                    it,
                    android.R.layout.simple_list_item_1,
                    listOf("Ninguno")
                )

                spinnerCustomer.adapter = customerAdapter
            }

            subscribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val startDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val finishDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)

            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        viewModel.searchBetween(startDate.timeInMillis, finishDate.timeInMillis)
    }

    private fun subscribeUi(adapter: DashboardPurchaseAdapter){
        viewModel.purchases.observe(viewLifecycleOwner, Observer {
            it.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    binding.recycler.visibility = View.VISIBLE
                    binding.viewContent.visibility = View.GONE
                    adapter.submitList(it.data)
                },
                onError = {
                    binding.recycler.visibility = View.GONE
                    binding.viewContent.visibility = View.VISIBLE

                }
            )
        })

        binding.spinnerType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.spinnerCustomer.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun loadAll(){
        viewModel.searchBetween(null, null)
    }

    private fun selectRange(){
        DatePickerFragment(this).show(parentFragmentManager, "DatePickerFragment")
    }
}