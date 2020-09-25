package com.titaniocorp.pos.app.ui.base.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import javax.inject.Inject

/**
 * Maneja las funcionalidades base de los fragmentos.
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class BaseFragment: Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val baseViewModel: BaseViewModel by viewModels { viewModelFactory }

    private var successDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let{
            successDialog = DialogHelper.normal(it)
        }
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun onDetach() {
        successDialog?.dismiss()
        super.onDetach()
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

    protected fun setLoading(isLoading: Boolean) {
        baseViewModel.setLoading(isLoading)
    }
}