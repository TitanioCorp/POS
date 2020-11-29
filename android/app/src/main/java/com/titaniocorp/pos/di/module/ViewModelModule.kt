package com.titaniocorp.pos.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.titaniocorp.pos.app.ui.MainActivityViewModel
import com.titaniocorp.pos.app.ui.billing.DashboardBillingViewModel
import com.titaniocorp.pos.app.ui.category.DashboardCategoryViewModel
import com.titaniocorp.pos.app.ui.customer.DashboardCustomerViewModel
import com.titaniocorp.pos.app.ui.pos.PurchasePosViewModel
import com.titaniocorp.pos.app.ui.pos.addProduct.AddProductPosViewModel
import com.titaniocorp.pos.app.ui.product.DashboardProductViewModel
import com.titaniocorp.pos.app.ui.product.detail.DetailProductViewModel
import com.titaniocorp.pos.app.ui.product.detail.price.AddPriceProductViewModel
import com.titaniocorp.pos.app.ui.profit.DashboardProfitViewModel
import com.titaniocorp.pos.app.ui.profit.initial.DashboardInitialProfitViewModel
import com.titaniocorp.pos.app.ui.purchase.DashboardPurchaseViewModel
import com.titaniocorp.pos.app.ui.purchase.detail.DetailPurchaseViewModel
import com.titaniocorp.pos.app.ui.report.DashboardReportViewModel
import com.titaniocorp.pos.app.ui.report.products.stock.StockReportViewModel
import com.titaniocorp.pos.app.ui.settings.database.DashboardDatabaseViewModel
import com.titaniocorp.pos.app.ui.stock.DashboardStockViewModel
import com.titaniocorp.pos.app.ui.stock.add.AddStockViewModel
import com.titaniocorp.pos.app.ui.stock.detail.DetailStockViewModel
import com.titaniocorp.pos.app.ui.warehouse.DashboardWarehouseViewModel
import com.titaniocorp.pos.app.ui.warehouse.add.AddPaymentWarehouseViewModel
import com.titaniocorp.pos.app.viewmodel.AppViewModelFactory
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.di.annotation.FragmentScope
import com.titaniocorp.pos.di.key.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Suppress("unused")
@ExperimentalCoroutinesApi
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(BaseViewModel::class)
    abstract fun baseViewModel(viewModel: BaseViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun mainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @Reusable
    @ViewModelKey(PurchasePosViewModel::class)
    abstract fun purchasePosViewModel(viewModel: PurchasePosViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddProductPosViewModel::class)
    abstract fun addProductPosViewModel(viewModel: AddProductPosViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardProductViewModel::class)
    abstract fun dashboardProductViewModel(viewModel: DashboardProductViewModel): ViewModel

    //@FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(DetailProductViewModel::class)
    abstract fun detailProductViewModel(viewModel: DetailProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardProfitViewModel::class)
    abstract fun dashboardProfitViewModel(viewModel: DashboardProfitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardCustomerViewModel::class)
    abstract fun dashboardCustomerViewModel(viewModel: DashboardCustomerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardPurchaseViewModel::class)
    abstract fun dashboardPurchaseViewModel(viewModel: DashboardPurchaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardCategoryViewModel::class)
    abstract fun dashboardCategoryViewModel(viewModel: DashboardCategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailPurchaseViewModel::class)
    abstract fun detailPurchaseViewModel(viewModel: DetailPurchaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardBillingViewModel::class)
    abstract fun dashboardBillingViewModel(viewModel: DashboardBillingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardStockViewModel::class)
    abstract fun dashboardStockViewModel(viewModel: DashboardStockViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddStockViewModel::class)
    abstract fun addStockViewModel(viewModel: AddStockViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailStockViewModel::class)
    abstract fun detailStockViewModel(viewModel: DetailStockViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardWarehouseViewModel::class)
    abstract fun dashboardWarehouseViewModel(viewModel: DashboardWarehouseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddPaymentWarehouseViewModel::class)
    abstract fun addPaymentWarehouseViewModel(viewModel: AddPaymentWarehouseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardReportViewModel::class)
    abstract fun dashboardReportViewModel(viewModel: DashboardReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardDatabaseViewModel::class)
    abstract fun dashboardDatabaseViewModel(viewModel: DashboardDatabaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StockReportViewModel::class)
    abstract fun stockReportViewModel(viewModel: StockReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardInitialProfitViewModel::class)
    abstract fun dashboardInitialProfitViewModel(viewModel: DashboardInitialProfitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddPriceProductViewModel::class)
    abstract fun addPriceProductViewModel(viewModel: AddPriceProductViewModel): ViewModel
}