package com.concredito.clientes.model

data class Document(
    val id: String,
    val nombre: String,
    val prospectoId: String,
    val archivo: ByteArray,
    val tipoArchivo: String,
    val metadata: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        if (id != other.id) return false
        if (nombre != other.nombre) return false
        if (prospectoId != other.prospectoId) return false
        if (!archivo.contentEquals(other.archivo)) return false
        if (tipoArchivo != other.tipoArchivo) return false
        if (metadata != other.metadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + prospectoId.hashCode()
        result = 31 * result + archivo.contentHashCode()
        result = 31 * result + tipoArchivo.hashCode()
        result = 31 * result + metadata.hashCode()
        return result
    }
}
