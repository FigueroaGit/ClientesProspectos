package com.concredito.clientes.model

data class Document(
    val prospectoId: String,
    val nombre: String,
    val tipoArchivo: String,
    val tamanoArchivo: String,
    val archivo: ByteArray,
)
