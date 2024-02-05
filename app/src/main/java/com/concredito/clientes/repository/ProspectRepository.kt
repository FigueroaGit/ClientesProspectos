package com.concredito.clientes.repository

import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.network.ProspectAPI
import javax.inject.Inject

class ProspectRepository @Inject constructor(private val API: ProspectAPI) {

    suspend fun getAllProspects(): Resource<List<Prospect>> {
        return try {
            Resource.Loading(data = true)
            val itemList = API.getAllProspects()
            if (itemList.isNotEmpty()) {
                Resource.Loading(data = false)
            }
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun getProspectsByPromoterId(promoterId: String): Resource<List<Prospect>> {
        return try {
            Resource.Loading(data = true)
            val itemList = API.getProspectsByPromoterId(promoterId)
            if (itemList.isNotEmpty()) {
                Resource.Loading(data = false)
            }
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun getProspectById(prospectId: String): Resource<Prospect> {
        val response = try {
            Resource.Loading(data = true)
            API.getProspectById(prospectId)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred ${exception.message}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun createProspect(prospect: Prospect): Resource<Prospect> {
        return try {
            Resource.Loading(data = true)
            val item = API.createProspect(prospect)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }

    suspend fun updateProspect(id: String, prospect: Prospect): Resource<Prospect> {
        return try {
            Resource.Loading(data = true)
            val item = API.updateProspect(id, prospect)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }

    suspend fun deleteProspect(id: String): Resource<Unit> {
        return try {
            Resource.Loading(data = true)
            API.deleteProspect(id)
            Resource.Success(data = Unit)
        } catch (exception: Exception) {
            Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }
}
