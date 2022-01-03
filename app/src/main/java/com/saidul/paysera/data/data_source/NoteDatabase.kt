package com.saidul.paysera.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saidul.paysera.domain.model.Balance

@Database(
    entities = [Balance::class],
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}