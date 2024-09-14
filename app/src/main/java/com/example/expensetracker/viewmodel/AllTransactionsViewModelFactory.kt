package com.example.expensetracker.viewmodel

import AllTransactionsViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.repository.TransactionRepository

class AllTransactionsViewModelFactory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllTransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AllTransactionsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
