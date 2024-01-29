package com.concredito.clientes.repository

import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.RejectObservation
import com.concredito.clientes.network.RejectObservationAPI
import javax.inject.Inject

class RejectObservationRepository @Inject constructor(private val API: RejectObservationAPI) {

    suspend fun getAllRejectObservations(): Resource<List<RejectObservation>> {
        return try {
            Resource.Loading(data = true)
            val itemList = API.getAllRejectObservations()
            if (itemList.isNotEmpty()) {
                Resource.Loading(data = false)
            }
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun getRejectObservationsById(rejectObservationId: String): Resource<RejectObservation> {
        val response = try {
            Resource.Loading(data = true)
            API.getRejectObservationsById(rejectObservationId)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred ${exception.message}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun getRejectObservationsByProspectId(prospectId: String): Resource<List<RejectObservation>> {
        return try {
            Resource.Loading(data = true)
            val itemList = API.getRejectObservationsByProspectId(prospectId)
            if (itemList.isNotEmpty()) {
                Resource.Loading(data = false)
            }
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun addRejectObservations(rejectObservation: RejectObservation): Resource<RejectObservation> {
        return try {
            Resource.Loading(data = true)
            val item = API.addRejectObservations(rejectObservation)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }

    suspend fun updateRejectObservations(
        id: String,
        rejectObservation: RejectObservation,
    ): Resource<RejectObservation> {
        return try {
            Resource.Loading(data = true)
            val item = API.updateRejectObservations(id, rejectObservation)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }

    suspend fun deleteRejectObservations(id: String): Resource<Unit> {
        return try {
            Resource.Loading(data = true)
            API.deleteRejectObservations(id)
            Resource.Success(data = Unit)
        } catch (exception: Exception) {
            Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }
}
