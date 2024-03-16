package com.concredito.clientes.repository

import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.File
import com.concredito.clientes.network.FileAPI
import okhttp3.MultipartBody
import javax.inject.Inject

class FileRepository @Inject constructor(private val API: FileAPI) {

    suspend fun uploadFile(file: MultipartBody.Part, name: String, prospectId: String): Resource<Unit> {
        return try {
            Resource.Loading(data = true)
            val item = API.uploadFile(file, name, prospectId)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            Resource.Error(message = "Error al subir el documento: ${exception.message}")
        }
    }

    suspend fun downloadFile(fileId: String): Resource<File> {
        return try {
            Resource.Loading(data = true)
            val item = API.downloadFile(fileId)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }

    suspend fun getFilesByProspect(prospectId: String): Resource<List<File>> {
        return try {
            Resource.Loading(data = true)
            val itemList = API.getFilesByProspect(prospectId)
            if (itemList.isNotEmpty()) {
                Resource.Loading(data = false)
            }
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun deleteFile(fileId: String): Resource<String> {
        return try {
            Resource.Loading(data = true)
            val item = API.deleteFile(fileId)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred: ${exception.message}")
        }
    }
}
