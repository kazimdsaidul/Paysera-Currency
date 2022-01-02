package com.saidul.paysera.data.repository


import com.saidul.paysera.data.data_source.NoteDao
import com.saidul.paysera.domain.repository.CurrencyRepository

class CurrencyRepositoryImpl(
    private val dao: NoteDao
) : CurrencyRepository {


}