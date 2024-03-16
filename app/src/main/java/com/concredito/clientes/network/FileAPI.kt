package com.concredito.clientes.network

import com.concredito.clientes.model.File
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface FileAPI {

    @Multipart
    @POST("documentos/upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("nombre") name: String,
        @Part("prospectoId") prospectId: String,
    ): Unit

    @GET("documentos/download/{id}")
    @Streaming
    suspend fun downloadFile(@Path("id") id: String): File

    @GET("documentos/documentos-por-prospecto/{prospectoId}")
    suspend fun getFilesByProspect(@Path("prospectoId") prospectId: String): List<File>

    @DELETE("documentos/eliminar-documento")
    suspend fun deleteFile(@Query("documentoId") fileId: String): String
}
