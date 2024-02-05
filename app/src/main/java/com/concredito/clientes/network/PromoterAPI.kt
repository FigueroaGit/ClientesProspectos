package com.concredito.clientes.network

import com.concredito.clientes.model.Promoter
import com.concredito.clientes.model.Prospect
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface PromoterAPI {

    @GET("promotores")
    suspend fun getAllPromoters(): List<Promoter>

    @GET("promotores/{id}")
    suspend fun getPromoterById(@Path("id") id: String): Promoter

    @POST("promotores")
    suspend fun createPromoter(@Body promoter: Promoter): Promoter

    @PUT("promotores/{id}")
    suspend fun updatePromoter(@Path("id") id: String, @Body promoter: Promoter): Promoter

    @DELETE("promotores/{id}")
    suspend fun deletePromoter(@Path("id") id: String)

    @POST("promotores/login")
    @FormUrlEncoded
    suspend fun login(@Field("usuario") username: String, @Field("contrasena") password: String): Promoter

    @GET("promotores/{id}/prospectos")
    suspend fun getProspectsByPromoter(@Path("id") id: String): List<Prospect>

}
