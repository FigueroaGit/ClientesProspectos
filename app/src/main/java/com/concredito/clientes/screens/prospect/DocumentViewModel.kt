package com.concredito.clientes.screens.prospect

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.Document
import com.concredito.clientes.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(private val repository: DocumentRepository) :
    ViewModel() {

    var list: List<Document> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

    suspend fun getDocumentsByProspectId(prospectId: String): Resource<List<Document>> {
        return repository.getDocumentsByProspectId(prospectId)
    }

    fun saveDocument(
        prospectId: String,
        name: String,
        fileByBytes: ByteArray,
        fileType: String,
        metadata: String,
    ) {
        viewModelScope.launch {
            try {
                Log.d("UploadDocument", "Prospect ID: $prospectId, Document Name: $name, Document Type: $fileType")
                repository.saveDocument(prospectId, name, fileByBytes, fileType, metadata)
            } catch (e: Exception) {
                Log.e("UploadDocument", "Error uploading document: ${e.message}")
            }
        }
    }

    fun downloadDocument(documentId: String) {
        viewModelScope.launch {
            repository.downloadDocument(documentId)
        }
    }
}
