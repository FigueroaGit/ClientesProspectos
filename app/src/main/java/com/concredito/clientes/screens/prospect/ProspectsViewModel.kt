package com.concredito.clientes.screens.prospect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concredito.clientes.data.PreferencesManager
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.repository.ProspectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProspectsViewModel @Inject constructor(
    private val repository: ProspectRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    suspend fun getProspectsByPromoterId(promoterId: String): Resource<List<Prospect>> {
        return repository.getProspectsByPromoterId(promoterId)
    }

    suspend fun getProspectById(prospectId: String): Resource<Prospect> {
        return repository.getProspectById(prospectId)
    }

    fun createProspect(prospect: Prospect) {
        viewModelScope.launch {
            repository.createProspect(prospect)
        }
    }

    fun updateProspect(id: String, prospect: Prospect) {
        viewModelScope.launch {
            repository.updateProspect(id, prospect)
        }
    }

    suspend fun deleteProspect(id: String): Resource<Unit> {
        return repository.deleteProspect(id)
    }

    fun getPromoterId(): String? {
        return preferencesManager.getPromoterId()
    }

    fun getUsername(): String? {
        return preferencesManager.getUsername()
    }
}
