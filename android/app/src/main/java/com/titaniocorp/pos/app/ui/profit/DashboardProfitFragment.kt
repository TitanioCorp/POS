package com.titaniocorp.pos.app.ui.profit

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
import com.google.android.material.snackbar.Snackbar
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentProfitDashboardBinding
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */

class DashboardProfitFragment: BaseFragment(),
    View.OnClickListener,
    DashboardProfitAdapter.OnItemClickListener{

    private lateinit var binding: FragmentProfitDashboardBinding
    val viewModel: DashboardProfitViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profit_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardProfitFragment

            val toolbar = (activity as AppCompatActivity).appbar
            nestedScrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                    val shouldShowToolbar = scrollY > toolbar.height
                    toolbar.isActivated = shouldShowToolbar
                }
            )

            val adapter = DashboardProfitAdapter(this@DashboardProfitFragment)
            recycler.adapter = adapter

            subcribeUi(adapter)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_item -> {
                DialogHelper.newProfit(
                    activity,
                    {profit ->
                        viewModel.insert(profit).observe(viewLifecycleOwner, Observer {
                            it.process(
                                {
                                    Timber.i("Se ha agregado la ganancia correctamente")
                                    Snackbar.make(binding.root,  "Se ha agregado la categoría correctamente.", Snackbar.LENGTH_SHORT).show()
                                },
                                {
                                    viewModel.setError(it.code, it.message)
                                },
                                {boolean -> setLoading(boolean)}
                            )
                        })
                    }
                )
            }
        }
    }

    override fun onClickItem(position: Int) {
        viewModel.profits.value?.data?.let {
            DialogHelper.newProfit(
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
            "Eliminar ganancia",
            "Eliminar",
            "Cancelar",
            {viewModel.delete(position)},
            {})?.show()
    }

    private fun subcribeUi(adapter: DashboardProfitAdapter){
        viewModel.profits.observe(viewLifecycleOwner, Observer {
            it.process(
                {
                    adapter.submitList(it.data)
                },
                onLoading = {boolean -> setLoading(boolean)}
            )
        })
    }
}