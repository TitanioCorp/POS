package com.titaniocorp.pos.app.ui.billing

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
import androidx.navigation.fragment.navArgs
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.app.ui.pos.addProduct.AddProductPosFragmentArgs
import com.titaniocorp.pos.databinding.FragmentBillingDashboardBinding
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
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
class DashboardBillingFragment: BaseFragment(), View.OnClickListener{
    private lateinit var binding: FragmentBillingDashboardBinding

    private val args: DashboardBillingFragmentArgs by navArgs()
    private val viewModel: DashboardBillingViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_billing_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            lifecycleOwner = viewLifecycleOwner
            clickListener = this@DashboardBillingFragment
            mViewModel = viewModel

            val toolbar = (activity as AppCompatActivity).appbar
            binding.nestedScrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                    val shouldShowToolbar = scrollY > toolbar.height
                    toolbar.isActivated = shouldShowToolbar
                }
            )

            viewModel.report.observe(viewLifecycleOwner, {
                it.process(
                    onLoading = {boolean -> setLoading(boolean)},
                    onSuccess = {
                        it.data?.let{billing ->
                            viewModel.updateBilling(billing)
                        }
                    },
                    onError = {}
                )
            })

            setDates()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.button -> {
                activity?.let{activity ->
                    lifecycleScope.launch{
                        withContext(Dispatchers.Default){
                            Configurations.setDirectory(FileUtil.getDirectory(activity).second)
                        }

                        viewModel.sendMail().observe(viewLifecycleOwner, Observer{
                            it.processEmail()
                        })
                    }
                }
            }

            R.id.button_generate_report -> {
                activity?.let{activity ->
                    lifecycleScope.launch{
                        withContext(Dispatchers.Default){
                            Configurations.setDirectory(FileUtil.getDirectory(activity).second)
                        }

                        viewModel.sendMail(TypeEmail.REPORT).observe(viewLifecycleOwner, Observer{
                            it.processEmail()
                        })

                    }
                }
            }
        }
    }

    private fun setDates(){
        lifecycleScope.launch(Dispatchers.IO){
            if(args.startDate > 0 && args.endDate > 0){
                val startDate: Calendar = Calendar.getInstance().apply {
                    timeInMillis = args.startDate
                }

                val endDate: Calendar = Calendar.getInstance().apply {
                    timeInMillis = args.endDate
                }

                val stringDates = "${startDate.get(Calendar.DAY_OF_MONTH)}/${startDate.get(Calendar.MONTH) + 1}/${startDate.get(Calendar.YEAR)} - ${endDate.get(Calendar.DAY_OF_MONTH)}/${endDate.get(Calendar.MONTH)}/${endDate.get(Calendar.YEAR)}"
                binding.textTitle.text = "Reporte"
                binding.textDate.text = stringDates
                binding.button.visibility = View.GONE

                viewModel.selectStartDate(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH))
                viewModel.selectEndDate(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH))

                binding.buttonGenerateReport.visibility = View.VISIBLE
                binding.textBilling.visibility = View.GONE
            } else {
                binding.textDate.text = Calendar.getInstance().time.toFormatString()
            }

            viewModel.updateDates()
        }
    }

    private fun Resource<String>.processEmail(){
        process(
            onLoading = {
                    boolean -> setLoading(boolean)
            },
            onSuccess = {
                try {
                    activity?.let{FileUtil.deleteDirectory(it.applicationContext)}
                    Configurations.setDirectory(null)

                    DialogHelper.normal(
                        activity,
                        "El email se ha enviado exitosamente",
                        "Envíado exitoso",
                        positiveString = "Aceptar",
                        positiveCallback = {})?.show()
                }catch (exception: Exception){
                    Timber.tag(Constants.TAG_APP_DEBUG).e(exception)
                }
            },
            onError = {
                DialogHelper.normal(
                    activity,
                    "$data",
                    "Envíado fallido!",
                    negativeString = "Aceptar",
                    negativeCallback = {}
                )?.show()
            }
        )
    }

    companion object{
        const val ARG_START_DATE = "startDate"
        const val ARG_END_DATE = "endDate"
    }
}