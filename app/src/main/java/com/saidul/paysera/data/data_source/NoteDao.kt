package com.saidul.paysera.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saidul.paysera.domain.model.Balance
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM Balance")
    fun getBalance(): Flow<List<Balance>>

    @Query("SELECT EXISTS(SELECT * FROM Balance WHERE id = :id)")
    fun isRowIsExist(id: Int): Flow<Boolean>

    //
//    @Query("SELECT * FROM note WHERE id = :id")
//    suspend fun getNoteById(id: Int): ContactsContract.CommonDataKinds.Note?
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(balance: Balance)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(balance: List<Balance>)
//
//    @Delete
//    suspend fun deleteNote(note: ContactsContract.CommonDataKinds.Note)
}