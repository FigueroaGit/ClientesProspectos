package com.concredito.clientes.model

import com.google.gson.annotations.SerializedName

data class RejectObservation(
    @SerializedName("id")
    val id: String,
    @SerializedName("observaciones")
    val observations: String,
    @SerializedName("prospectoId")
    val prospectId: String,
)
