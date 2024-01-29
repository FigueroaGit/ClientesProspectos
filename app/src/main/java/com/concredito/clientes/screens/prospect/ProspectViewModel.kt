package com.concredito.clientes.screens.prospect

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.repository.ProspectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProspectViewModel @Inject constructor(private val repository: ProspectRepository) : ViewModel() {

    var list: List<Prospect> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

    init {
        loadProspects()
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
}
