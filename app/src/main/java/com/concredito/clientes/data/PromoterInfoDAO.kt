package com.concredito.clientes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.concredito.clientes.model.PromoterInfo

@Dao
interface PromoterInfoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePromoterInfo(promoterInfo: PromoterInfo)

    @Query("SELECT * FROM promoter_info LIMIT 1")
    suspend fun getPromoterInfo(): PromoterInfo?
}