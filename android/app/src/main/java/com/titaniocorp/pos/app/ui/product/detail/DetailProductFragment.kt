package com.titaniocorp.pos.app.ui.product.detail

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.snackbar.Snackbar
import com.titaniocorp.pos.BR
import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.Category
import com.titaniocorp.pos.app.ui.base.adapter.CategorySpinnerAdapter
import com.titaniocorp.pos.app.ui.base.fragment.BaseFragment
import com.titaniocorp.pos.databinding.FragmentProductDetailBinding
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_FORM_CATEGORY
import com.titaniocorp.pos.util.AppCode.Companion.ERROR_FORM_VALIDATE
import com.titaniocorp.pos.util.AppCode.Companion.SUCCESS_FORM_VALIDATE
import com.titaniocorp.pos.util.process
import com.titaniocorp.pos.util.ui.DialogHelper
import com.titaniocorp.pos.util.validations.ValidateUtil
import com.titaniocorp.pos.util.validations.toValidate
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Fragmento que muestra el detalle  de un producto
 * @author Juan Ortiz
 * @date 19/12/2019
 */
class DetailProductFragment: BaseFragment(),
    View.OnClickListener,
    AdapterView.OnItemSelectedListener,
    DetailProductAdapter.DetailProductItemListener{

    private lateinit var binding: FragmentProductDetailBinding
    val viewModel: DetailProductViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_options_detail_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val productId = it.getLong("productId", 0)
            if(productId > 0){
                viewModel.mProductId.value = arguments?.getLong("productId")
            }
        }

        with(binding){

            lifecycleOwner = viewLifecycleOwner
            this.viewModel = this@DetailProductFragment.viewModel
            clickListener = this@DetailProductFragment

            val toolbar = (activity as AppCompatActivity).appbar
            nestedScrollView.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                    val shouldShowToolbar = scrollY > toolbar.height
                    toolbar.isActivated = shouldShowToolbar
                }
            )

            val adapter = DetailProductAdapter(this@DetailProductFragment)
            adapter.submitList(this@DetailProductFragment.viewModel.product.prices)

            binding.recycler.adapter = adapter

            subscribeUi(adapter)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_finish_product -> {
            if(validate()){
                if(viewModel.product.id > 0L){
                    updateProduct()
                }else{
                    addProduct()
                }
            }
            true
        }
        else -> { item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item) }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_new_category -> actionNewCategory()
            R.id.button_new_price -> actionNewPrice()
            R.id.button_delete -> deleteProduct()
        }
    }

    private fun subscribeUi(adapter: DetailProductAdapter){
        viewModel.getProduct().observe(viewLifecycleOwner, Observer {
            it.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    it.data?.let{ data ->
                        viewModel.product = data
                        adapter.submitList(viewModel.product.prices)
                        viewModel.notifyPropertyChanged(BR.product)

                        for((index, category) in viewModel.categories.withIndex()){
                            if(category.id == viewModel.product.categoryId){
                                binding.spinnerCategory.setSelection(index, true)
                                break
                            }
                        }

                        (activity as AppCompatActivity).toolbar.title = viewModel.product.name
                    }
                }
            )
        })

        viewModel.getAllCategory().observe(viewLifecycleOwner, Observer {
            it.process(
                onLoading = {boolean -> setLoading(boolean)},
                onSuccess = {
                    it.data?.let {list ->
                        viewModel.categories = list

                        with(binding.spinnerCategory){
                            this.adapter = CategorySpinnerAdapter(
                                context,
                                android.R.layout.simple_spinner_item,
                                android.R.layout.simple_spinner_dropdown_item,
                                this@DetailProductFragment.viewModel.categories
                            )
                            this.onItemSelectedListener = this@DetailProductFragment
                        }
                    }
                }
            )
        })
    }

    //Category Spinner
    private fun actionNewCategory(){
        DialogHelper.newCategory(
            activity,
            {category ->
                viewModel.addCategory(category).observe(viewLifecycleOwner, Observer {
                    it.process(
                        {
                            Timber.i("Se ha agregado la categoría correctamente")
                            Snackbar.make(binding.root,  "Se ha agregado la categoría correctamente.", Snackbar.LENGTH_SHORT).show()
                        },
                        { viewModel.setError(it.code, it.message) },
                        {boolean -> setLoading(boolean)}
                    )
                })
            }
        )?.show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.product.categoryId =
            (binding.spinnerCategory.adapter.getItem(position) as Category).id ?: 0
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    //Price
    private fun actionNewPrice(){
        val direction = DetailProductFragmentDirections.toAddPriceProductFragment()
        findNavController().navigate(direction)
    }

    override fun onClickItem(position: Int) {
        val direction = DetailProductFragmentDirections.toAddPriceProductFragment(position)
        findNavController().navigate(direction)
    }

    override fun onRemoveItem(position: Int) {
        viewModel.removePrice(position)
        binding.recycler.adapter?.notifyDataSetChanged()
    }

    private fun validate(): Boolean{
        var code = SUCCESS_FORM_VALIDATE

        with(binding){

            when {
                !ValidateUtil.inputs(
                    inputName.toValidate()
                ) -> {
                    code = ERROR_FORM_VALIDATE
                }

                this@DetailProductFragment.viewModel.product.categoryId == 0L -> {
                    code = ERROR_FORM_CATEGORY
                }

                this@DetailProductFragment.viewModel.product.prices.size == 0 -> {
                    code = ERROR_FORM_VALIDATE
                }
            }
        }

        return if(code != SUCCESS_FORM_VALIDATE){
            viewModel.setError(code)
            false
        }else{
            true
        }
    }

    //Product
    private fun addProduct(){
        viewModel.addProduct().observe(viewLifecycleOwner, Observer {
            it.process(
                {
                    showSuccess(
                        getString(R.string.text_success_add_product),
                        positiveCallback = {
                            val direction = DetailProductFragmentDirections.toDashboardProductFragment()
                            findNavController().navigate(direction)
                        }
                    )
                },
                { viewModel.setError(it.code, it.message) },
                {boolean -> setLoading(boolean)}
            )
        })
    }

    private fun updateProduct(){
        viewModel.updateProduct().observe(viewLifecycleOwner, Observer {
            it.process(
                {
                    showSuccess(
                        getString(R.string.text_success_update_product),
                        positiveCallback = {
                            val direction = DetailProductFragmentDirections.toDashboardProductFragment()
                            findNavController().navigate(direction)
                        }
                    )
                },
                { viewModel.setError(it.code, it.message) },
                {boolean -> setLoading(boolean)}
            )
        })
    }

    private fun deleteProduct(){
        showSuccess(
            getString(R.string.text_really_deleted_product),
            getString(R.string.title_delete_product),
            positiveString = getString(R.string.action_delete),
            positiveCallback = {
                viewModel.deleteProduct().observe(viewLifecycleOwner, Observer {
                    it.process(
                        {
                            DialogHelper.normal(
                                activity,
                                getString(R.string.text_success_deleted_product),
                                getString(R.string.success_title),
                                positiveCallback = {
                                    val direction = DetailProductFragmentDirections.toDashboardProductFragment()
                                    findNavController().navigate(direction)
                                }
                            )?.show()
                        },
                        { viewModel.setError(it.code, it.message) },
                        {boolean -> setLoading(boolean)}
                    )
                })
            },
            negativeCallback = {}
        )
    }
}