package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.database.AppDatabase
import com.example.expensetracker.repository.TransactionRepository

class MyApplication : Application() {
    // Lazy initialization of the database and repository
    val database by lazy { AppDatabase.getDatabase(this) }
    val transactionRepository by lazy { TransactionRepository(database.transactionDao()) }
}

