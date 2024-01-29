package com.concredito.clientes.network

import com.concredito.clientes.model.RejectObservation
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RejectObservationAPI {

    @GET("observaciones-rechazo")
    suspend fun getAllRejectObservations(): List<RejectObservation>

    @GET("observaciones-rechazo/{id}")
    suspend fun getRejectObservationsById(@Path("id") id: String): RejectObservation

    @GET("observaciones-rechazo/prospecto/{idProspecto}")
    suspend fun getRejectObservationsByProspectId(@Path("idProspecto") prospectId: String): List<RejectObservation>

    @POST("observaciones-rechazo")
    suspend fun addRejectObservations(@Body rejectObservation: RejectObservation): RejectObservation

    @PUT("observaciones-rechazo/{id}")
    suspend fun updateRejectObservations(@Path("id") id: String, @Body rejectObservation: RejectObservation): RejectObservation

    @DELETE("observaciones-rechazo/{id}")
    suspend fun deleteRejectObservations(@Path("id") id: String)
}
