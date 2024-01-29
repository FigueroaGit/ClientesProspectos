package com.concredito.clientes.network

import com.concredito.clientes.model.Prospect
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface ProspectAPI {

    @GET("prospectos")
    suspend fun getAllProspects(): List<Prospect>

    @GET("prospectos/{id}")
    suspend fun getProspectById(@Path("id") id: String): Prospect

    @POST("prospectos")
    suspend fun createProspect(@Body prospect: Prospect): Prospect

    @PUT("prospectos/{id}")
    suspend fun updateProspect(@Path("id") id: String, @Body prospect: Prospect): Prospect

    @DELETE("prospectos/{id}")
    suspend fun deleteProspect(@Path("id") id: String)
}