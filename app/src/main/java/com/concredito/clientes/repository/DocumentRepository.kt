package com.concredito.clientes.repository

import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Document
import com.concredito.clientes.network.DocumentAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class DocumentRepository @Inject constructor(private val API: DocumentAPI) {

    suspend fun getDocumentsByProspectId(prospectId: String): Resource<List<Document>> {
        return try {
            Resource.Loading(data = true)
            val itemList = API.getDocumentsByProspectId(prospectId)
            if (itemList.isNotEmpty()) {
                Resource.Loading(data = false)
            }
            Resource.Success(data = itemList)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun saveDocument(
        prospectId: String,
        name: String,
        fileByBytes: ByteArray,
        fileType: String,
        metadata: String,
    ): Resource<Document> {
        return try {
            Resource.Loading(data = true)
            val fileRequestBody = fileByBytes.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", name, fileRequestBody)
            val item = API.saveDocument(prospectId, name, filePart, fileType, metadata)
            Resource.Success(data = item)
        } catch (exception: Exception) {
            return Resource.Error(message = "An error occurred: ${exception.message}")
        } finally {
            Resource.Loading(data = false)
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
}
