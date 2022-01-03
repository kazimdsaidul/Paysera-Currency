package com.saidul.paysera.di

import com.saidul.paysera.domain.repository.CurrencyRepository
import com.saidul.paysera.domain.usecase.CurrencyConvertUseCase
import com.saidul.paysera.domain.usecase.CurrencyLatestUseCase
import com.saidul.paysera.domain.usecase.GetBalanceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideNoteUseCases(repository: CurrencyRepository): CurrencyLatestUseCase {
        return CurrencyLatestUseCase(
            repository = repository
        )
    }


    @Provides
    @Singleton
    fun provideCurrencyConvertUseCase(repository: CurrencyRepository): CurrencyConvertUseCase {
        return CurrencyConvertUseCase(
            repository = repository
        )
    }


    @Provides
    @Singleton
    fun provideGetBalanceUseCase(repository: CurrencyRepository): GetBalanceUseCase {
        return GetBalanceUseCase(
            repository = repository
        )
    }

}