package com.concredito.clientes.model

data class Promoter(
    val id: String,
    val usuario: String,
    val contrasena: String,
    val prospectos: List<Prospect>,
)
