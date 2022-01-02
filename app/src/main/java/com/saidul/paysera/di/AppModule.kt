package com.saidul.paysera.di

import android.app.Application
import androidx.room.Room
import com.saidul.paysera.data.data_source.NoteDatabase
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
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): CurrencyRepository {
        return CurrencyRepositoryImpl(db.noteDao)
    }

//    @Provides
//    @Singleton
//    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
//        return NoteUseCases(
//            getNotes = GetNotes(repository),
//            deleteNote = DeleteNote(repository),
//            addNote = AddNote(repository),
//            getNote = GetNote(repository)
//        )
//    }
}