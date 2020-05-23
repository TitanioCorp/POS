package com.titaniocorp.pos.app.ui.settings.database

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
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentBillingDashboardBinding
import com.titaniocorp.pos.databinding.FragmentDatabaseDashboardBinding
import com.titaniocorp.pos.util.*
import com.titaniocorp.pos.util.ui.DialogHelper
import com.titaniocorp.pos.util.ui.DialogUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Fragmento que lista las peliculas
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardDatabaseFragment: BaseFragment(), View.OnClickListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentDatabaseDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDatabaseDashboardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initializeFragment(viewModel)
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }
}