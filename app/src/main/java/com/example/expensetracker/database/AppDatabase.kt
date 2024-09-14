package com.example.expensetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensetracker.model.DateConverter
import com.example.expensetracker.model.Transaction
import com.example.expensetracker.model.TransactionType
import com.example.expensetracker.model.TransactionTypeConverter

@Database(entities = [Transaction::class], version = 1, exportSchema = false) // Increment the version number
@TypeConverters(DateConverter::class, TransactionTypeConverter::class)

abstract class AppDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Transaction_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}