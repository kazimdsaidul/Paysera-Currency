package com.saidul.paysera.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.model.Rate
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDBDao {

    @Query("SELECT * FROM Balance")
    fun getBalance(): Flow<List<Balance>>

    @Query("SELECT EXISTS(SELECT * FROM Balance WHERE id = :id)")
    fun isRowIsExist(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalance(balance: Balance)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalance(balance: List<Balance>)

    @Query("DELETE FROM Balance")
    suspend fun deleteBalanceData()


    @Query("SELECT * FROM Rate")
    fun getRates(): Flow<List<Rate>>

    @Query("SELECT * FROM Rate WHERE  currencyName = :currencyName")
    fun getRates(currencyName: String): Flow<Rate>

    @Query("SELECT EXISTS(SELECT * FROM Rate WHERE id = :id)")
    fun isRowIsExistRate(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(balance: Rate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(balance: List<Rate>)

    @Query("DELETE FROM Rate")
    suspend fun deleteRateData()

}