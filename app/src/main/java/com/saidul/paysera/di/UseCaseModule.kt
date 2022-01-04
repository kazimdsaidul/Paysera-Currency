package com.saidul.paysera.di

import com.saidul.paysera.domain.repository.CurrencyRepository
import com.saidul.paysera.domain.usecase.*
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
    fun provideNoteUseCases(repository: CurrencyRepository): LatestCurrencyRateUseCase {
        return LatestCurrencyRateUseCase(
            repository = repository
        )
    }


    @Provides
    @Singleton
    fun provideCurrencyConvertUseCase(repository: CurrencyRepository): CalculationAndConvertUseCase {
        return CalculationAndConvertUseCase(
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

    @Provides
    @Singleton
    fun provideDefaultBalanceAddUseCase(repository: CurrencyRepository): DefaultBalanceAddUseCase {
        return DefaultBalanceAddUseCase(
            repository = repository
        )
    }

    @Provides
    @Singleton
    fun provideGetCalcuationUseCase(repository: CurrencyRepository): CalculationUseCase {
        return CalculationUseCase(
            repository = repository
        )
    }


}