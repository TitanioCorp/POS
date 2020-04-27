package com.titaniocorp.pos.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.titaniocorp.pos.app.ui.billing.DashboardBillingViewModel
import com.titaniocorp.pos.app.ui.category.DashboardCategoryViewModel
import com.titaniocorp.pos.app.ui.customer.DashboardCustomerViewModel
import com.titaniocorp.pos.app.ui.pos.POSViewModel
import com.titaniocorp.pos.app.ui.product.DashboardProductViewModel
import com.titaniocorp.pos.app.ui.product.detail.DetailProductViewModel
import com.titaniocorp.pos.app.ui.profit.DashboardProfitViewModel
import com.titaniocorp.pos.app.ui.purchase.DashboardPurchaseViewModel
import com.titaniocorp.pos.app.ui.purchase.detail.DetailPurchaseViewModel
import com.titaniocorp.pos.app.ui.report.DashboardReportViewModel
import com.titaniocorp.pos.app.ui.stock.DashboardStockViewModel
import com.titaniocorp.pos.app.ui.stock.add.AddStockViewModel
import com.titaniocorp.pos.app.ui.stock.detail.DetailStockViewModel
import com.titaniocorp.pos.app.ui.warehouse.DashboardWarehouseViewModel
import com.titaniocorp.pos.app.ui.warehouse.add.AddPaymentWarehouseViewModel
import com.titaniocorp.pos.app.viewmodel.AppViewModelFactory
import com.titaniocorp.pos.di.key.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Reusable
    @ViewModelKey(POSViewModel::class)
    abstract fun dashboardPOSViewModel(viewModel: POSViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardProductViewModel::class)
    abstract fun dashboardProductViewModel(viewModel: DashboardProductViewModel): ViewModel

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
}