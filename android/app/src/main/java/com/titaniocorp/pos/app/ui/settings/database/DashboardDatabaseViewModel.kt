package com.titaniocorp.pos.app.ui.settings.database

import androidx.lifecycle.asLiveData
import com.titaniocorp.pos.app.viewmodel.ObservableViewModel
import com.titaniocorp.pos.repository.DatabaseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DashboardDatabaseViewModel @Inject constructor(
    private val repository: DatabaseRepository
): ObservableViewModel() {
    fun export() = repository.export().asLiveData()

    fun import() = repository.import().asLiveData()
}