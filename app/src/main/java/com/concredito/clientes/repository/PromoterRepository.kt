package com.concredito.clientes.repository

import android.util.Log
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Promoter
import com.concredito.clientes.model.Prospect
import com.concredito.clientes.network.PromoterAPI
import javax.inject.Inject

class PromoterRepository @Inject constructor(private val API: PromoterAPI) {

    suspend fun getAllPromoters(): Resource<List<Promoter>> {
        return try {
            Resource.Loading(data = true)
            val itemList = API.getAllPromoters()
            if (itemList.isNotEmpty()) {
                Resource.Loading(data = false)
            }
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun getPromoterById(promoterId: String): Resource<Promoter> {
        val response = try {
            Resource.Loading(data = true)
            API.getPromoterById(promoterId)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred ${exception.message}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun getProspectsByPromoter(id: String): Resource<List<Prospect>> {
        return try {
            Resource.Loading(data = true)
            val itemList = API.getProspectsByPromoter(id)
            if (itemList.isNotEmpty()) {
                Resource.Loading(data = false)
            }
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun login(username: String, password: String): Resource<Promoter> {
        return try {
            Resource.Loading(data = true)
            val item = API.login(username, password)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }

    suspend fun createPromoter(promoter: Promoter): Resource<Promoter> {
        return try {
            Resource.Loading(data = true)
            val item = API.createPromoter(promoter)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }

    suspend fun updatePromoter(id: String, promoter: Promoter): Resource<Promoter> {
        return try {
            Resource.Loading(data = true)
            val item = API.updatePromoter(id, promoter)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }

    suspend fun deletePromoter(id: String): Resource<Unit> {
        return try {
            Resource.Loading(data = true)
            API.deletePromoter(id)
            Resource.Success(data = Unit)
        } catch (exception: Exception) {
            Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }
}
