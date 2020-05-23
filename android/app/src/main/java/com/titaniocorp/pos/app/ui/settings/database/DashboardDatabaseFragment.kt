package com.titaniocorp.pos.app.ui.settings.database

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.titaniocorp.pos.BuildConfig
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentDatabaseDashboardBinding
import com.titaniocorp.pos.util.toFormatFileString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.titaniocorp.pos.util.process
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Fragmento que exporta/improta la base de datos
 * @author Juan Ortiz
 * @date 23/05/2020
 */
@ExperimentalCoroutinesApi
class DashboardDatabaseFragment: BaseFragment(), View.OnClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentDatabaseDashboardBinding
    val viewModel: DashboardDatabaseViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDatabaseDashboardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment(viewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.clickListener = this

        if(!allPermissionsGranted()){
            activity?.let{
                ActivityCompat.requestPermissions(
                    it,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    override fun onClick(v: View?) {
        if(allPermissionsGranted()){
            when(v?.id){
                R.id.button_export -> {export()}
                R.id.button_import -> {import()}
            }
        }else{
            activity?.let{
                ActivityCompat.requestPermissions(
                    it,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    private fun export(){
        viewModel.export().observe(viewLifecycleOwner, Observer {
            it.process(
                {
                    showSuccess("La base de datos se ha exportado correctamente.")
                },
                onError = { viewModel.setError(it.code, it.message) },
                onLoading = {boolean -> setLoading(boolean)}
            )
        })
    }

    private fun import(){
        viewModel.export().observe(viewLifecycleOwner, Observer {
            it.process(
                {
                    showSuccess("La base de datos se ha importado correctamente.")
                },
                onError = { viewModel.setError(it.code, it.message) },
                onLoading = {boolean -> setLoading(boolean)}
            )
        })
    }

    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        return activity?.let {activity ->
            ContextCompat.checkSelfPermission(activity.baseContext, it) == PackageManager.PERMISSION_GRANTED
        } ?: run {
            false
        }
    }

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private const val REQUEST_CODE_PERMISSIONS = 10

        private const val DATABASE_NAME = "appdatabase"
    }
}