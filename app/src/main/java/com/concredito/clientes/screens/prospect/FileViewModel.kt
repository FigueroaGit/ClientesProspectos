package com.concredito.clientes.screens.prospect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.concredito.clientes.data.Resource
import com.concredito.clientes.model.File
import com.concredito.clientes.repository.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(private val repository: FileRepository) :
    ViewModel() {

    fun uploadFile(file: MultipartBody.Part, name: String, prospectId: String) {
        viewModelScope.launch {
            repository.uploadFile(file, name, prospectId)
        }
    }

    fun downloadFile(fileId: String) {
        viewModelScope.launch {
            repository.downloadFile(fileId)
        }
    }

    suspend fun getFilesByProspect(prospectId: String): Resource<List<File>> {
        return repository.getFilesByProspect(prospectId)
    }

    suspend fun deleteFile(fileId: String): Resource<String> {
        return repository.deleteFile(fileId)
    }
}
