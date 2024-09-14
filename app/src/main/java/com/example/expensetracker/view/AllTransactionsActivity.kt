package com.example.expensetracker.view

import AllTransactionsViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.expensetracker.R
import com.example.expensetracker.adapter.TransactionAdapter
import com.example.expensetracker.database.AppDatabase
import com.example.expensetracker.repository.TransactionRepository
import com.example.expensetracker.viewmodel.AllTransactionsViewModelFactory
import kotlinx.coroutines.launch

class AllTransactionsActivity : AppCompatActivity() {
    private lateinit var viewModel: AllTransactionsViewModel
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_transactions)

        val database = AppDatabase.getDatabase(this)
        val transactionDao = database.transactionDao()
        val repository = TransactionRepository(transactionDao)
        val viewModelFactory = AllTransactionsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[AllTransactionsViewModel::class.java]

        adapter = TransactionAdapter { transaction ->
            // Handle click on transaction if needed
        }

        findViewById<RecyclerView>(R.id.rv_all_transactions).apply {
            this.adapter = this@AllTransactionsActivity.adapter
            layoutManager = LinearLayoutManager(this@AllTransactionsActivity)
        }

        lifecycleScope.launch {
            viewModel.transactions.collect { transactions ->
                adapter.submitList(transactions)
                Log.d("AllTransactionsActivity", "Transactions: $transactions")

            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                findViewById<ProgressBar>(R.id.progressBar).visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        findViewById<Button>(R.id.btn_back_to_main).setOnClickListener {
            finish() // This will close the activity and return to the previous one
        }
    }
}
