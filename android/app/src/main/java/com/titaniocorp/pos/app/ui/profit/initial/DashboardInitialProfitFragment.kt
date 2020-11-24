package com.titaniocorp.pos.app.ui.profit.initial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.InitialProfit
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.app.ui.profit.initial.dialog.showInitialProfitDialog
import com.titaniocorp.pos.databinding.FragmentInitialProfitDashboardBinding
import com.titaniocorp.pos.util.Constants
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import com.titaniocorp.pos.util.ui.showDefaultDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

/**
 * Fragmento que lista las ganancias iniciales
 * @author Juan Ortiz
 * @date 20/11/2020
 */

@ExperimentalCoroutinesApi
class DashboardInitialProfitFragment: BaseFragment(),
    View.OnClickListener,
    DashboardInitialProfitAdapter.OnItemClickListener
{

    private lateinit var binding: FragmentInitialProfitDashboardBinding
    val viewModel: DashboardInitialProfitViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_initial_profit_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardInitialProfitFragment

            val adapter = DashboardInitialProfitAdapter(this@DashboardInitialProfitFragment)
            recycler.adapter = adapter

            subscribeUi(adapter)
        }
    }

    private fun subscribeUi(adapter: DashboardInitialProfitAdapter){
        viewModel.initialProfits.runLiveData({
            adapter.submitList(it.data)
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_item -> {
                context?.showInitialProfitDialog({item ->
                    viewModel.insert(item).runLiveData({
                        Timber.tag(Constants.TAG_APP_DEBUG).d("Ganancia inicial agregada!")
                        Snackbar.make(binding.root,"Ganancia inicial agregada!", Snackbar.LENGTH_SHORT).show()
                    })
                })
            }
        }
    }

    override fun onClickItem(item: InitialProfit) {
        context?.showInitialProfitDialog({itemResult ->
            viewModel.update(itemResult).runLiveData({
                Timber.tag(Constants.TAG_APP_DEBUG).d("Ganancia inicial [${item.id}] modificada!")
                Snackbar.make(binding.root,"Ganancia inicial [${item.id}] modificada!", Snackbar.LENGTH_SHORT).show()
            })
        }, item)
    }

    override fun onClickRemoveItem(item: InitialProfit) {
        if(item.id > 1){
            context?.showDefaultDialog(
                "Esta acción no podrá deshacerse.",
                "Remover la ganancia de la lista",
                positiveString = "Remover",
                positiveCallback = {
                    viewModel.delete(item).runLiveData({
                        Timber.tag(Constants.TAG_APP_DEBUG).d("Ganancia inicial [${item.id}] eliminada!")
                        Snackbar.make(binding.root,"Ganancia inicial [${item.id}] eliminada!", Snackbar.LENGTH_SHORT).show()
                    })
                }
            )
        } else {
            context?.showDefaultDialog(
                "Este elemento no puede ser removido.",
                "Elemento necesario",
                negativeString = null
            )
        }
    }
}