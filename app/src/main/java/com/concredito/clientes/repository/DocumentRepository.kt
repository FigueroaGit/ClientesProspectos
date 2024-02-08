package com.concredito.clientes.repository

import android.util.Log
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Document
import com.concredito.clientes.network.DocumentAPI
import okhttp3.MultipartBody
import javax.inject.Inject

class DocumentRepository @Inject constructor(private val API: DocumentAPI) {

    suspend fun uploadDocument(file: MultipartBody.Part, name: String, prospectId: String): Resource<Document> {
        return try {
            Log.d("DocumentRepository", "Iniciando subida del archivo...")
            val item = API.uploadDocument(file, name, prospectId)
            Log.d("DocumentRepository", "Archivo subido con éxito: $item")
            Resource.Success(data = item)
        } catch (exception: Exception) {
            Log.e("DocumentRepository", "Error al subir archivo: ${exception.message}")
            Resource.Error(message = "Error al subir el documento: ${exception.message}")
        }
    }

    suspend fun downloadDocument(documentId: String): Resource<Document> {
        return try {
            Resource.Loading(data = true)
            val item = API.downloadDocument(documentId)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
        }
    }

    suspend fun getDocumentsByProspect(prospectId: String): Resource<List<Document>> {
        return try {
            Resource.Loading(data = true)
            val itemList = API.getDocumentsByProspect(prospectId)
            if (itemList.isNotEmpty()) {
                Resource.Loading(data = false)
            }
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun deleteDocument(documentoId: String): Resource<String> {
        return try {
            Resource.Loading(data = true)
            val item = API.deleteDocument(documentoId)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred: ${exception.message}")
        }
    }
}
