package com.concredito.clientes.data

import android.content.Context

class PreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun savePromoterInfo(promoterId: String, username: String) {
        with(sharedPreferences.edit()) {
            putString("PROMOTER_ID", promoterId)
            putString("USERNAME", username)
            apply()
        }
    }

    fun getPromoterId(): String? {
        return sharedPreferences.getString("PROMOTER_ID", null)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("USERNAME", null)
    }

    fun clearCredentials() {
        with(sharedPreferences.edit()) {
            remove("PROMOTER_ID")
            remove("USERNAME")
            apply()
        }
    }
}
