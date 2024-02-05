package com.concredito.clientes.screens.prospect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Promoter
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.repository.PromoterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromoterViewModel @Inject constructor(private val repository: PromoterRepository) :
    ViewModel() {

    suspend fun getPromoterById(promoterId: String): Resource<Promoter> {
        return repository.getPromoterById(promoterId)
    }

    suspend fun getProspectsByPromoter(promoterId: String): Resource<List<Prospect>> {
        return repository.getProspectsByPromoter(promoterId)
    }

    suspend fun login(username: String, password: String): Resource<Promoter> {
        return repository.login(username, password)
    }

    fun createPromoter(promoter: Promoter) {
        viewModelScope.launch {
            repository.createPromoter(promoter)
        }
    }

    fun updatePromoter(id: String, promoter: Promoter) {
        viewModelScope.launch {
            repository.updatePromoter(id, promoter)
        }
    }

    suspend fun deletePromoter(id: String): Resource<Unit> {
        return repository.deletePromoter(id)
    }
}
