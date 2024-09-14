package com.example.expensetracker.view


import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.expensetracker.MyApplication
import com.example.expensetracker.R
import com.example.expensetracker.model.TransactionType
import com.example.expensetracker.viewmodel.AddTransactionViewModel
import com.example.expensetracker.viewmodel.AddTransactionsViewModelFactory


class AddTransactionActivity : AppCompatActivity() {

    private val addTransactionViewModel: AddTransactionViewModel by viewModels {
        AddTransactionsViewModelFactory((application as MyApplication).transactionRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_transaction)

        val etDate: EditText = findViewById(R.id.et_date)
        val spinnerTransactionType: Spinner = findViewById(R.id.spinner_transaction_type)
        val etAmount: EditText = findViewById(R.id.et_amount)
        val spinnerCategory: Spinner = findViewById(R.id.spinner_category)
        val btnSaveTransaction: Button = findViewById(R.id.btn_save_transaction)
        val btnBackToMain: Button = findViewById(R.id.btn_back_to_main)

        // Set up Spinner for transaction types
        val transactionTypes = TransactionType.values().map { it.name }
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, transactionTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTransactionType.adapter = spinnerAdapter

        // Set up Spinner for categories (you can adjust this based on the type)
        val categories = listOf("Food", "Transport", "Shopping", "Bills") // Example categories
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        // Observe validation errors
        addTransactionViewModel.validationError.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        // Observe transaction addition result
        addTransactionViewModel.transactionAdded.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after adding the transaction
            }
        })

        // Set up Save button click listener
        btnSaveTransaction.setOnClickListener {
            val date = etDate.text.toString()
            val type = TransactionType.valueOf(spinnerTransactionType.selectedItem.toString())
            val amount = etAmount.text.toString()
            val category = spinnerCategory.selectedItem.toString()

            // Call ViewModel to add the transaction
            addTransactionViewModel.addTransaction(date, type, amount, category)
        }

        // Set up Back to Main button click listener
        btnBackToMain.setOnClickListener {
            finish() // Close the activity
        }
    }
}
