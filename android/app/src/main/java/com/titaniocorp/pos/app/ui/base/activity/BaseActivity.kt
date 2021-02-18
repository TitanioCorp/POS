package com.titaniocorp.pos.app.ui.base.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.util.Configurations
import com.titaniocorp.pos.util.Constants
import com.titaniocorp.pos.util.error.getStringsId
import com.titaniocorp.pos.util.ui.DialogUtil
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber

/**
 * Actividad base de la aplicación
 * @author Juan Ortiz
 * @date 10/09/2019
 */
open class BaseActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    protected lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    protected val baseViewModel: BaseViewModel by viewModels { viewModelFactory }

    private lateinit var progressDialog: AlertDialog
    private lateinit var errorDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = DialogUtil.progress(this)
        errorDialog = DialogUtil.default(this)

        if(!Configurations.init(this)){
            Timber.tag(Constants.TAG_APP_DEBUG).e("Configurations no ha podido ser leido.")
        }

        subscribeUi()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private fun subscribeUi(){
        baseViewModel.isLoading.observe(this, {
            setLoading(it)
        })

        baseViewModel.isError.observe(this, {
            val stringsId = it.getStringsId()
            val message = getString(stringsId.first)
            val title = getString(stringsId.second ?: R.string.error_title_error)

            showError(message, title)
        })
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            progressDialog.show()
        }else{
            progressDialog.dismiss()
        }
    }

    private fun showError(message: String, title: String){
        errorDialog.apply {
            setTitle(title)
            setMessage(message)
            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_accept)){ dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }
}

