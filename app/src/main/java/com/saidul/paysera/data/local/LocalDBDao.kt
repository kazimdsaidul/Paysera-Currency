package com.saidul.paysera.data.local

import androidx.room.*
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.model.Rate
import com.saidul.paysera.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDBDao {

    // Balance table CRUD operation
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

    @Query("SELECT * FROM Balance WHERE currencyName = :currency")
    fun getBalance(currency: String): Flow<Balance>

    @Update(entity = Balance::class)
    suspend fun updateBalance(updateSellBalance: Balance)


    // Rate table CRUD operation
    @Query("SELECT * FROM Rate")
    fun getRates(): Flow<List<Rate>>

    @Query("SELECT * FROM Rate WHERE  currencyName = :currencyName")
    fun getRates(currencyName: String): Flow<Rate>

    @Query("SELECT EXISTS(SELECT * FROM Rate WHERE id = :id)")
    fun isRowIsExistRate(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: Rate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRateList(rate: List<Rate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(balance: List<Rate>)

    @Query("DELETE FROM Rate")
    suspend fun deleteRateData()


    // Transaction table CRUD operation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransction(balance: Transaction)

    @Query("SELECT * FROM `Transaction`")
    fun getTransction(): Flow<List<Transaction>>

    @Query("DELETE FROM `Transaction`")
    suspend fun deleteTranstion()


}