package com.concredito.clientes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promoter_info")
data class PromoterInfo(
    @PrimaryKey val id: String,
    val username: String
)