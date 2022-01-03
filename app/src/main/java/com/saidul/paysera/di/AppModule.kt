package com.saidul.paysera.di

import android.app.Application
import androidx.room.Room
import com.saidul.paysera.data.local.LocalDatabase
import com.saidul.paysera.data.remote.ApiService
import com.saidul.paysera.data.repository.CurrencyRepositoryImpl
import com.saidul.paysera.domain.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): LocalDatabase {
        return Room.databaseBuilder(
            app,
            LocalDatabase::class.java,
            LocalDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: LocalDatabase, apiService: ApiService): CurrencyRepository {
        return CurrencyRepositoryImpl(db.localDBDao, apiService)
    }

}