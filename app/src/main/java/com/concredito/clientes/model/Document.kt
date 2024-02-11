package com.concredito.clientes.model

data class Document(
    val id: String,
    val prospectoId: String,
    val nombre: String,
    val tipoArchivo: String,
    val tamanoArchivo: String,
    val archivo: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Document

        if (prospectoId != other.prospectoId) return false
        if (nombre != other.nombre) return false
        if (tipoArchivo != other.tipoArchivo) return false
        if (tamanoArchivo != other.tamanoArchivo) return false
        if (!archivo.contentEquals(other.archivo)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = prospectoId.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + tipoArchivo.hashCode()
        result = 31 * result + tamanoArchivo.hashCode()
        result = 31 * result + archivo.contentHashCode()
        return result
    }
}
