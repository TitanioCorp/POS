package com.titaniocorp.pos.app.ui.base.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.ErrorResource
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.di.Injectable
import com.titaniocorp.pos.util.AppCode
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_PURCHASE_EMPTY_PRICES
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_PURCHASE_NO_CUSTOMER
import com.titaniocorp.pos.util.Constants
import com.titaniocorp.pos.util.ui.DialogUtil
import com.titaniocorp.pos.util.hideKeyboard
import com.titaniocorp.pos.util.ui.DialogHelper
import timber.log.Timber
import java.net.HttpURLConnection

/**
 * Maneja las funcionalidades base de los fragmentos.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class BaseFragment: Fragment(), Injectable {
    private var loadingDialog: AlertDialog? = null
    private var successDialog: AlertDialog? = null
    private var errorDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let{
            loadingDialog = DialogUtil.progress(it)
            successDialog = DialogHelper.normal(it)
            errorDialog = DialogHelper.normal(it)
        }
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun onDetach() {
        loadingDialog?.dismiss()
        successDialog?.dismiss()
        errorDialog?.dismiss()
        super.onDetach()
    }

    protected fun initializeFragment(viewModel: BaseViewModel){
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            setLoading(it)
        })

        viewModel.isError.observe(viewLifecycleOwner, Observer {
            it.process()
        })
    }

    private fun showError(message: String, title: String = getString(R.string.error_title_error)){
        errorDialog?.apply {
            setTitle(title)
            setMessage(message)
            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_accept)){ dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    protected fun showSuccess(
        message: String,
        title: String = getString(R.string.success_title),
        positiveString: String = getString(R.string.action_accept),
        negativeString: String = getString(R.string.action_cancel),
        positiveCallback: (() -> Unit)? = null,
        negativeCallback: (() -> Unit)? = null
    ){
        successDialog?.apply {
            setTitle(title)
            setMessage(message)
            setButton(AlertDialog.BUTTON_POSITIVE, positiveString){ dialog, _ ->
                dialog.dismiss()
                positiveCallback?.invoke()
            }
            negativeCallback?.let{
                setButton(AlertDialog.BUTTON_NEGATIVE, negativeString){ dialog, _ ->
                    dialog.dismiss()
                    it()
                }
            }
            show()
        }
    }

    protected fun setLoading(isLoading: Boolean){
        if(isLoading){
            loadingDialog?.show()
        }else{
            loadingDialog?.dismiss()
        }
    }

    /**
     * Extension de ErrorResource el cual procesa el error. Maneja los errores de acuerdo al código
     * ya sean los definidos en ResponseCode o los de HttpURLConnection.
     * @author Juan Ortiz
     * @date 10/09/2019
     * */
    protected fun ErrorResource.process(){
        Timber.tag(Constants.TAG_APP_DEBUG).e("Code: $code - Message: $message")
        when(this.code){
            /* HTTP ERROR */
            HttpURLConnection.HTTP_INTERNAL_ERROR,
            HttpURLConnection.HTTP_UNAVAILABLE,
            HttpURLConnection.HTTP_VERSION -> {
                showError(getString(R.string.error_text_http_internal_error))
            }

            HttpURLConnection.HTTP_NOT_FOUND -> {
                showError(getString(R.string.error_text_http_not_found))
            }

            /* SYSTEM ERROR */
            AppCode.NO_INTERNET -> {
                showError(getString(R.string.error_text_no_internet))
            }
            AppCode.QUERY_DATABASE -> {
                showError(getString(R.string.error_text_query_database))
            }
            AppCode.ERROR_FORM_VALIDATE ->
                showError(
                    getString(R.string.error_text_form_validate),
                    getString(R.string.error_title_information)
                )

            AppCode.ERROR_FORM_CATEGORY ->
                showError(
                    getString(R.string.error_text_form_category),
                    getString(R.string.error_title_information)
                )

            /* DATA ERROR */
            AppCode.DATA_EMPTY -> {
                showError(getString(R.string.error_text_data_empty))
            }

            /* PURCHASE VALIDATION */
            ERROR_PURCHASE_EMPTY_PRICES -> {
                showError(getString(R.string.validate_text_purchase_empty_prices), getString(R.string.error_title_information))
            }

            ERROR_PURCHASE_NO_CUSTOMER -> {
                showError(getString(R.string.validate_text_purchase_no_customer), getString(R.string.error_title_validation_customer))
            }

            AppCode.ERROR_QUANTITY_PRICE_PURCHASE -> showError(getString(R.string.validate_text_quantity_price_purchase), getString(R.string.error_title_quantity_price_purchase))
            AppCode.ERROR_ZERO_QUANTITY_PRICE -> showError(getString(R.string.validate_text_zero_quantity_price), getString(R.string.error_title_zero_quantity_price))
            AppCode.ERROR_CANNOT_REFUND -> showError(getString(R.string.validate_text_cannot_refund), getString(R.string.error_title_alert))
            AppCode.ERROR_CANNOT_ADD_PAYMENT -> showError(getString(R.string.validate_text_cannot_add_payment), getString(R.string.error_title_alert))
            AppCode.ERROR_EMPTY_PROFIT_LIST -> showError(getString(R.string.validate_text_empty_profit_list), getString(R.string.error_title_empty_profit_list))

            /* Default Message */
            else -> {
                showError(getString(R.string.error_text_default))
            }
        }
    }
}