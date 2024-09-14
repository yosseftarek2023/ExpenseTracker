package com.example.expensetracker.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetracker.model.Transaction
import com.example.expensetracker.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM Transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>> // Use Flow instead of List

    @Query("SELECT * FROM Transactions WHERE type = :type ORDER BY date DESC")
    suspend fun getTransactionsByType(type: TransactionType): List<Transaction>

    @Query("SELECT SUM(amount) FROM Transactions WHERE type = :type")
    suspend fun getTotalAmountByType(type: TransactionType): Double

    @Query("DELETE FROM Transactions WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: Long)
}
