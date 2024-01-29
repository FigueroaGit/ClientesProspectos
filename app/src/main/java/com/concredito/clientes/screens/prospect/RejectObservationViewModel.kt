package com.concredito.clientes.screens.prospect

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.RejectObservation
import com.concredito.clientes.repository.RejectObservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RejectObservationViewModel @Inject constructor(private val repository: RejectObservationRepository) :
    ViewModel() {

    var list: List<RejectObservation> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

    init {
        loadObservations()
    }

    private fun loadObservations() {
        viewModelScope.launch {
            try {
                when (val response = repository.getAllRejectObservations()) {
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

    suspend fun getRejectObservationsById(rejectObservationId: String): Resource<RejectObservation> {
        return repository.getRejectObservationsById(rejectObservationId)
    }

    suspend fun getRejectObservationsByProspectId(prospectId: String): Resource<List<RejectObservation>> {
        return repository.getRejectObservationsByProspectId(prospectId)
    }

    fun addRejectObservations(rejectObservation: RejectObservation) {
        viewModelScope.launch {
            repository.addRejectObservations(rejectObservation)
        }
    }

    suspend fun updateRejectObservations(id: String, rejectObservation: RejectObservation): Resource<RejectObservation> {
        return repository.updateRejectObservations(id, rejectObservation)
    }

    suspend fun deleteRejectReservations(id: String): Resource<Unit> {
        return repository.deleteRejectObservations(id)
    }
}
