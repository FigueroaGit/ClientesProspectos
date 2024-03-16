package com.concredito.clientes.model

import com.google.gson.annotations.SerializedName

data class File(
    @SerializedName("id")
    val id: String,
    @SerializedName("prospectoId")
    val prospectId: String,
    @SerializedName("nombre")
    val name: String,
    @SerializedName("tipoArchivo")
    val fileType: String,
    @SerializedName("tamanoArchivo")
    val fileSize: String,
    @SerializedName("archivo")
    val file: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as File

        if (prospectId != other.prospectId) return false
        if (name != other.name) return false
        if (fileType != other.fileType) return false
        if (fileSize != other.fileSize) return false
        if (!file.contentEquals(other.file)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = prospectId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + fileType.hashCode()
        result = 31 * result + fileSize.hashCode()
        result = 31 * result + file.contentHashCode()
        return result
    }
}
