package com.concredito.clientes.model

import com.google.gson.annotations.SerializedName

data class Prospect(
    @SerializedName("id")
    val id: String,
    @SerializedName("idPromotor")
    val promoterId: String,
    @SerializedName("nombre")
    val name: String,
    @SerializedName("primerApellido")
    val surname: String,
    @SerializedName("segundoApellido")
    val secondSurname: String? = null,
    @SerializedName("calle")
    val streetAddress: String,
    @SerializedName("numero")
    val numberAddress: String,
    @SerializedName("colonia")
    val neighborhood: String,
    @SerializedName("codigoPostal")
    val zipCode: String,
    @SerializedName("telefono")
    val phoneNumber: String,
    @SerializedName("rfc")
    val rfc: String,
    @SerializedName("estatus")
    var status: ProspectStatus,
)
