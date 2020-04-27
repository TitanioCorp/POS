package com.titaniocorp.pos.app.ui.category

import com.titaniocorp.pos.app.model.Category
import com.titaniocorp.pos.app.viewmodel.BaseViewModel
import com.titaniocorp.pos.repository.CategoryRepository
import javax.inject.Inject

class DashboardCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): BaseViewModel() {
    val categories = categoryRepository.getAll()

    fun insert(item: Category) = categoryRepository.add(item)

    fun update(item: Category) = categoryRepository.update(item)

    fun delete(position: Int) {
        categories.value?.data?.let {
            val item = it[position].copy(active = false)
            categoryRepository.update(item)
        }
    }
}