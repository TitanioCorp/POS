package com.titaniocorp.pos.app.ui.purchase.detail

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
import androidx.lifecycle.lifecycleScope
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.dto.DetailPurchaseAdapterDto
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentPurchaseDetailBinding
import com.titaniocorp.pos.util.AppCode
import com.titaniocorp.pos.util.Constants.Companion.TAG_APP_DEBUG
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogPurchaseHelper
import com.titaniocorp.pos.util.ui.SnackbarUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DetailPurchaseFragment: BaseFragment(),
    View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentPurchaseDetailBinding
    val viewModel: DetailPurchaseViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(viewModel)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DetailPurchaseFragment
            viewModel = this@DetailPurchaseFragment.viewModel

            val toolbar = (activity as AppCompatActivity).appbar
            binding.nestedScrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                    val shouldShowToolbar = scrollY > toolbar.height
                    toolbar.isActivated = shouldShowToolbar
                }
            )

            val adapterProducts = DetailPurchaseAdapter(this@DetailPurchaseFragment)
            val adapterRefund = DetailPurchaseAdapter()

            recyclerProducts.adapter = adapterProducts
            recyclerRefunds.adapter = adapterRefund

            val adapterPayments = PaymentDetailPurchaseAdapter()
            recyclerPayments.adapter = adapterPayments

            subscribeUi(adapterProducts, adapterRefund, adapterPayments)
        }

        arguments?.let {
            viewModel.setPurchaseId(it.getLong(ARG_PURCHASE_ID, 0))
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_add_payment -> { showPaymentDialog() }
            R.id.view_content -> {
                showRefundDialog(v.tag as DetailPurchaseAdapterDto)
            }
        }
    }

    private fun subscribeUi(adapterProducts: DetailPurchaseAdapter, adapterRefund: DetailPurchaseAdapter, adapterPayments: PaymentDetailPurchaseAdapter){
        viewModel.purchase.observe(viewLifecycleOwner, Observer {
            it.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    binding.purchase = it.data
                    lifecycleScope.launch{
                        adapterPayments.submitList(viewModel.computePurchase())
                    }
                }
            )
        })

        viewModel.pricesDto.observe(viewLifecycleOwner, Observer {
            it.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    lifecycleScope.launch{
                        val data = viewModel.computePricesDto(it.data)
                        adapterProducts.submitList(data.first)
                        adapterRefund.submitList(data.second)
                    }
                }
            )
        })
    }

    private fun addRefund(id: Long, value: Int){
        viewModel.addRefund(id, value)?.observe(viewLifecycleOwner, Observer {
            it.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    Timber.tag(TAG_APP_DEBUG).d("Refund added successfully - id: ${it.data?.first}")
                },
                onError = {
                    Timber.tag(TAG_APP_DEBUG).e("Refund could not be added")
                }
            )
        })
    }

    private fun showRefundDialog(item: DetailPurchaseAdapterDto){
        if(viewModel.canRefund()){
            DialogPurchaseHelper.addRefund(
                activity,
                {id, value ->
                    addRefund(id, value)
                },
                data = item)
        }else{
            viewModel.setError(AppCode.ERROR_CANNOT_ADD_PAYMENT, null)
        }

    }

    private fun showPaymentDialog(){
        if(viewModel.canAddPayment()){
            DialogPurchaseHelper.addPayment(
                activity,
                {addPayment(it)},
                missingPayment = viewModel.getMissingPayment())
        }else{
            viewModel.setError(AppCode.ERROR_CANNOT_ADD_PAYMENT, null)
        }

    }

    private fun addPayment(value: Double){
        viewModel.addPayment(value)?.observe(viewLifecycleOwner, Observer {
            it.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    SnackbarUtil.default(binding.root, "Se añadido el abono correctamente.")
                    lifecycleScope.launch{
                        viewModel.setPayments(it.data)
                        (binding.recyclerPayments.adapter as PaymentDetailPurchaseAdapter).submitList(viewModel.computePurchase())
                    }

                },
                onError = {
                    SnackbarUtil.default(binding.root, "Error - El abono no ha podido ser añadido.")
                }
            )
        })
    }

    companion object{
        const val ARG_PURCHASE_ID = "purchaseId"
    }
}