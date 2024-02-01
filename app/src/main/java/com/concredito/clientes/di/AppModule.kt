package com.concredito.clientes.di

import android.content.Context
import com.concredito.clientes.data.PreferencesManager
import com.concredito.clientes.network.DocumentAPI
import com.concredito.clientes.network.PromoterAPI
import com.concredito.clientes.network.ProspectAPI
import com.concredito.clientes.network.RejectObservationAPI
import com.concredito.clientes.repository.DocumentRepository
import com.concredito.clientes.repository.PromoterRepository
import com.concredito.clientes.repository.ProspectRepository
import com.concredito.clientes.repository.RejectObservationRepository
import com.concredito.clientes.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

    @Singleton
    @Provides
    fun providePromoterRepository(API: PromoterAPI) = PromoterRepository(API)

    @Singleton
    @Provides
    fun provideProspectRepository(API: ProspectAPI) = ProspectRepository(API)

    @Singleton
    @Provides
    fun provideDocumentRepository(API: DocumentAPI) = DocumentRepository(API)

    @Singleton
    @Provides
    fun provideRejectObservationRepository(API: RejectObservationAPI) = RejectObservationRepository(API)

    @Singleton
    @Provides
    fun providePromoterAPI(): PromoterAPI {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(PromoterAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideProspectAPI(): ProspectAPI {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ProspectAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideDocumentAPI(): DocumentAPI {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(DocumentAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideRejectObservationAPI(): RejectObservationAPI {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(RejectObservationAPI::class.java)
    }
}
