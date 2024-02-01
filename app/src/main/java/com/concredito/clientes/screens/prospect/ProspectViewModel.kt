package com.concredito.clientes.screens.prospect

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concredito.clientes.data.PreferencesManager
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.repository.ProspectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProspectViewModel @Inject constructor(private val repository: ProspectRepository, private val preferencesManager: PreferencesManager) : ViewModel() {

    var list: List<Prospect> by mutableStateOf(listOf())
    var listOfPromoterId: List<Prospect> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

    init {
        loadProspects()
        getPromoterId()?.let { loadProspectsByPromoter(it) }
    }

    private fun loadProspects() {
        viewModelScope.launch {
            try {
                when (val response = repository.getAllProspects()) {
                    is Resource.Success -> {
                        list = response.data!!
                        if (list.isNotEmpty()) isLoading = false
                    }

                    is Resource.Error -> {
                        isLoading = false
                        Log.d("Network", "searchProspects: Failed getting prospects")
                        Log.d("RES", "searchProspects: Failed getting prospects")
                    }

                    else -> {
                        isLoading = false
                    }
                }
            } catch (exception: Exception) {
                isLoading = false
                Log.d("Network", "searchBooks: ${exception.message}")
                Log.d("RES", "searchBooks: ${exception.message}")
            }
        }
    }

    private fun loadProspectsByPromoter(promoterId: String) {
        viewModelScope.launch {
            try {
                 when (val response = repository.getProspectsByPromoterId(promoterId)) {
                     is Resource.Success -> {
                         listOfPromoterId = response.data!!
                         if (listOfPromoterId.isNotEmpty()) isLoading = false
                     }

                     is Resource.Error -> {
                         isLoading = false
                         Log.d("Network", "searchProspects: Failed getting prospects")
                         Log.d("RES", "searchProspects: Failed getting prospects")
                     }

                     else -> {
                         isLoading = false
                     }
                 }
            } catch (exception: Exception) {
                isLoading = false
                Log.d("Network", "searchBooks: ${exception.message}")
                Log.d("RES", "searchBooks: ${exception.message}")
            }
        }
    }

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
