package com.titaniocorp.pos.app.ui.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentReportDashboardBinding
import com.titaniocorp.pos.util.ui.DatePickerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fragmento para generar reportes
 * @author Juan Ortiz
 * @date 04/02/2020
 */

class DashboardReportFragment: BaseFragment(),
    View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentReportDashboardBinding
    val viewModel: DashboardReportViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardReportFragment
            mViewModel = viewModel

            spinnerTypeReport.adapter = ArrayAdapter(
                binding.root.context,
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.array_type_report)
            )

            spinnerTypeReport.onItemSelectedListener = this@DashboardReportFragment

        }

        subscribeUi()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_start_date -> {
                selectStartDate()
            }
            R.id.button_end_date -> {
                selectEndDate()
            }
            R.id.button_generate_report -> {
                viewModel.navigate()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.selectReport(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun selectStartDate(){
        DatePickerFragment { _, year, month, dayOfMonth ->
            viewModel.selectStartDate(year, month, dayOfMonth)
        }.show(parentFragmentManager, "startDateFragment")
    }

    private fun selectEndDate(){
        DatePickerFragment { _, year, month, dayOfMonth ->
            viewModel.selectEndDate(year, month, dayOfMonth)
        }.show(parentFragmentManager, "endDateFragment")
    }

    private fun subscribeUi(){
        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            when(it.first){
                0 -> {
                    val direction = DashboardReportFragmentDirections.toStockReportFragment()
                    findNavController().navigate(direction)
                }
                1 -> {
                    lifecycleScope.launch{
                        val dates = viewModel.getDates()
                        val direction = DashboardReportFragmentDirections.toDashboardBillingFragment(
                            dates.first, dates.second
                        )
                        findNavController().navigate(direction)
                    }
                }
            }
        })
    }
}