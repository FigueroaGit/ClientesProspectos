package com.concredito.clientes.model

import com.google.gson.annotations.SerializedName

data class Promoter(
    @SerializedName("id")
    val id: String,
    @SerializedName("usuario")
    val username: String,
    @SerializedName("contrasena")
    val password: String,
)
