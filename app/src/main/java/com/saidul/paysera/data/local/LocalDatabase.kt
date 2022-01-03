package com.saidul.paysera.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.model.Rate

const val KEY_DATABASE_NAME = "CURRENCY"

@Database(
    entities = [Balance::class, Rate::class],
    version = 1
)
abstract class LocalDatabase : RoomDatabase() {

    abstract val localDBDao: LocalDBDao

    companion object {
        const val DATABASE_NAME = KEY_DATABASE_NAME
    }
}