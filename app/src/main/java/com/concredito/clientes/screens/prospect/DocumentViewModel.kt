package com.concredito.clientes.screens.prospect

import android.util.Log
import androidx.compose.runtime.getValue
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

    fun uploadDocument(file: MultipartBody.Part, name: String, prospectId: String) {
        viewModelScope.launch {
            repository.uploadDocument(file, name, prospectId)
            Log.d("DocumentViewModel", "Iniciando subida del archivo...")
        }
    }

    fun downloadDocument(documentId: String) {
        viewModelScope.launch {
            repository.downloadDocument(documentId)
        }
    }

    suspend fun getDocumentsByProspect(prospectId: String): Resource<List<Document>> {
        return repository.getDocumentsByProspect(prospectId)
    }

    suspend fun deleteDocument(documentId: String): Resource<String> {
        return repository.deleteDocument(documentId)
    }
}
