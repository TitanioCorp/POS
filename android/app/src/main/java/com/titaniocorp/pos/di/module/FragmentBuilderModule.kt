package com.titaniocorp.pos.di.module

import com.titaniocorp.pos.app.ui.billing.DashboardBillingFragment
import com.titaniocorp.pos.app.ui.category.DashboardCategoryFragment
import com.titaniocorp.pos.app.ui.customer.DashboardCustomerFragment
import com.titaniocorp.pos.app.ui.pos.DashboardPosFragment
import com.titaniocorp.pos.app.ui.pos.addProduct.AddProductPosFragment
import com.titaniocorp.pos.app.ui.pos.purchase.PurchasePOSFragment
import com.titaniocorp.pos.app.ui.product.DashboardProductFragment
import com.titaniocorp.pos.app.ui.product.detail.DetailProductFragment
import com.titaniocorp.pos.app.ui.profit.DashboardProfitFragment
import com.titaniocorp.pos.app.ui.purchase.DashboardPurchaseFragment
import com.titaniocorp.pos.app.ui.purchase.detail.DetailPurchaseFragment
import com.titaniocorp.pos.app.ui.report.DashboardReportFragment
import com.titaniocorp.pos.app.ui.report.products.stock.StockReportFragment
import com.titaniocorp.pos.app.ui.settings.SettingsFragment
import com.titaniocorp.pos.app.ui.settings.database.DashboardDatabaseFragment
import com.titaniocorp.pos.app.ui.stock.DashboardStockFragment
import com.titaniocorp.pos.app.ui.stock.add.AddStockFragment
import com.titaniocorp.pos.app.ui.stock.detail.DetailStockFragment
import com.titaniocorp.pos.app.ui.warehouse.DashboardWarehouseFragment
import com.titaniocorp.pos.app.ui.warehouse.add.AddPaymentWarehouseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun settingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun dashboardPOSFragment(): DashboardPosFragment

    @ContributesAndroidInjector
    abstract fun purchasePOSFragment(): PurchasePOSFragment

    @ContributesAndroidInjector
    abstract fun addProductPOSFragment(): AddProductPosFragment

    @ContributesAndroidInjector
    abstract fun dashboardProductFragment(): DashboardProductFragment

    @ContributesAndroidInjector
    abstract fun detailProductFragment(): DetailProductFragment

    @ContributesAndroidInjector
    abstract fun dashboardProfitFragment(): DashboardProfitFragment

    @ContributesAndroidInjector
    abstract fun dashboardCategoryFragment(): DashboardCategoryFragment

    @ContributesAndroidInjector
    abstract fun dashboardCustomerFragment(): DashboardCustomerFragment

    @ContributesAndroidInjector
    abstract fun dashboardPurchaseFragment(): DashboardPurchaseFragment

    @ContributesAndroidInjector
    abstract fun detailPurchaseFragment(): DetailPurchaseFragment

    @ContributesAndroidInjector
    abstract fun dashboardBillingFragment(): DashboardBillingFragment

    @ContributesAndroidInjector
    abstract fun dashboardStockFragment(): DashboardStockFragment

    @ContributesAndroidInjector
    abstract fun addStockFragment(): AddStockFragment

    @ContributesAndroidInjector
    abstract fun detailStockFragment(): DetailStockFragment

    @ContributesAndroidInjector
    abstract fun dashboardWarehouseFragment(): DashboardWarehouseFragment

    @ContributesAndroidInjector
    abstract fun addPaymentWarehouseFragment(): AddPaymentWarehouseFragment

    @ContributesAndroidInjector
    abstract fun dashboardReportFragment(): DashboardReportFragment

    @ContributesAndroidInjector
    abstract fun dashboardDatabaseFragment(): DashboardDatabaseFragment

    @ContributesAndroidInjector
    abstract fun stockReportFragment(): StockReportFragment
}