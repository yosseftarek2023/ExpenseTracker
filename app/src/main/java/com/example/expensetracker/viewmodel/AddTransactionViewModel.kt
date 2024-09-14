package com.example.expensetracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.model.Transaction
import com.example.expensetracker.model.TransactionType
import com.example.expensetracker.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class AddTransactionViewModel(private val repository: TransactionRepository) : ViewModel() {
    private val _transactionAdded = MutableLiveData<Boolean>()
    val transactionAdded: LiveData<Boolean> = _transactionAdded

    private val _validationError = MutableLiveData<String>()
    val validationError: LiveData<String> = _validationError

    fun addTransaction(date: String, type: TransactionType, amount: String, category: String) {
        val validationErrorMessage = validateInputs(date, amount)
        if (validationErrorMessage != null) {
            _validationError.value = validationErrorMessage
            return
        }

        val parsedDate = parseDate(date)
        val parsedAmount = amount.toDouble()
        val transaction = Transaction(
            date = parsedDate,
            type = type,
            amount = parsedAmount,
            category = category,
            id = 0  // Room will auto-generate this
        )

        viewModelScope.launch {
            try {
                repository.insertTransaction(transaction)
                _transactionAdded.value = true
                Log.d("AddTransactionViewModel", "Adding transaction: $transaction")
            } catch (e: Exception) {
                _validationError.value = "Error adding transaction: ${e.message}"
                _transactionAdded.value = false
            }
        }
    }

    private fun validateInputs(date: String, amount: String): String? {
        return when {
            !isValidDate(date) -> "Invalid date format. Use dd/mm/yyyy."
            amount.isEmpty() -> "Amount cannot be empty."
            amount.toDoubleOrNull() == null -> "Amount must be a number."
            else -> null
        }
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date) != null
        } catch (e: Exception) {
            false
        }
    }

    private fun parseDate(date: String): Date {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date)!!
    }
}