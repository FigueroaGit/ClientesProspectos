package com.concredito.clientes.data

import android.content.Context
import com.concredito.clientes.model.PromoterInfo

class LocalDataService(context: Context) {

    private val promoterDatabase = PromoterDatabase.getInstance(context)

    suspend fun savePromoterInfo(promoterId: String, username: String) {
        val promoterInfo = PromoterInfo(promoterId, username)
        promoterDatabase.promoterInfoDAO().savePromoterInfo(promoterInfo)
        println("Username saved: $username")
    }

    suspend fun getPromoterId(): String? {
        val promoterInfo = promoterDatabase.promoterInfoDAO().getPromoterInfo()
        return promoterInfo?.id
    }

    suspend fun getUsername(): String? {
        val promoterInfo = promoterDatabase.promoterInfoDAO().getPromoterInfo()
        val retrievedUsername = promoterInfo?.username
        println("Retrieved Username: $retrievedUsername")
        return retrievedUsername
        return promoterInfo?.username
    }
}