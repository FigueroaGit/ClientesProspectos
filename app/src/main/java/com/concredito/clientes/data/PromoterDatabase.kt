package com.concredito.clientes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.concredito.clientes.model.PromoterInfo

@Database(entities = [PromoterInfo::class], version = 1, exportSchema = false)
abstract class PromoterDatabase : RoomDatabase() {
    abstract fun promoterInfoDAO(): PromoterInfoDAO

    companion object {
        @Volatile
        private var instance: PromoterDatabase? = null

        fun getInstance(context: Context): PromoterDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PromoterDatabase {
            return Room.databaseBuilder(context, PromoterDatabase::class.java, "promoter_database")
                .build()
        }
    }
}