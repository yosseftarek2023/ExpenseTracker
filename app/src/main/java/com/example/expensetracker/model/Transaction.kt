package com.example.expensetracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = "Transactions")
@Parcelize
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TransactionType, // Type can be Expense or Income
    val date: Date,
    val amount: Double,
    val category: String
) : Parcelable

enum class TransactionType {
    EXPENSE,
    INCOME
}
