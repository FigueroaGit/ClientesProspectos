package com.concredito.clientes.network

import com.concredito.clientes.model.Document
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface DocumentAPI {

    @Multipart
    @POST("documentos/upload")
    suspend fun uploadDocument(
        @Part archivo: MultipartBody.Part,
        @Part("nombre") nombre: String,
        @Part("prospectoId") prospectoId: String,
    ): Unit

    @GET("documentos/download/{id}")
    @Streaming
    suspend fun downloadDocument(@Path("id") id: String): Document

    @GET("documentos/documentos-por-prospecto/{prospectoId}")
    suspend fun getDocumentsByProspect(@Path("prospectoId") prospectoId: String): List<Document>

    @DELETE("documentos/eliminar-documento")
    suspend fun deleteDocument(@Query("documentoId") documentoId: String): String
}
