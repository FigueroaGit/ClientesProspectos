package com.concredito.clientes.network

import com.concredito.clientes.model.Document
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming

interface DocumentAPI {

    @Multipart
    @POST("documentos/guardar")
    suspend fun saveDocument(
        @Part("nombre") name: String,
        @Part("prospectoId") prospectId: String,
        @Part file: MultipartBody.Part,
        @Part("tipoArchivo") fileType: String,
        @Part("metadata") metadata: String,
    ): Document

    @GET("documentos/porProspecto/{prospectoId}")
    suspend fun getDocumentsByProspectId(@Path("prospectoId") prospectId: String): List<Document>

    @GET("documentos/descargar/{documentoId}")
    @Streaming
    suspend fun downloadDocument(@Path("documentoId") documentoId: String): Document
}
