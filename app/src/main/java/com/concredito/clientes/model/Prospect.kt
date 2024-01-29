package com.concredito.clientes.model

data class Prospect(
    val id: String,
    val idPromotor: String,
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String? = null,
    val calle: String,
    val numero: String,
    val colonia: String,
    val codigoPostal: String,
    val telefono: String,
    val rfc: String,
    var estatus: ProspectStatus,
)
