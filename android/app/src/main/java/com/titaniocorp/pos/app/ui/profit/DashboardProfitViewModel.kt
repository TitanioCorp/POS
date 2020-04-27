package com.titaniocorp.pos.app.ui.profit

import com.titaniocorp.pos.app.model.Profit
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.ProfitRepository
import javax.inject.Inject

class DashboardProfitViewModel @Inject constructor(
    private val profitRepository: ProfitRepository
): BaseViewModel() {
    val profits = profitRepository.getAll()

    fun insert(item: Profit) = profitRepository.add(item)

    fun update(item: Profit) = profitRepository.update(item)

    fun delete(position: Int) {
        profits.value?.data?.let {
            profitRepository.delete(it[position])
        }
    }
}