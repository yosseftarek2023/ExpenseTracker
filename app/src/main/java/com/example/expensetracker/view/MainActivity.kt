package com.example.expensetracker.view

import MainActivityViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.MyApplication
import com.example.expensetracker.R
import com.example.expensetracker.adapter.TransactionAdapter
import com.example.expensetracker.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by viewModels {
        MainViewModelFactory((application as MyApplication).transactionRepository)
    }
    private lateinit var adapter: TransactionAdapter
    private lateinit var rvTransactions: RecyclerView
    private lateinit var tvTotalBalance: TextView
    private lateinit var tvTotalIncome: TextView
    private lateinit var tvTotalExpense: TextView
    private lateinit var btnShowAllTransactions: Button
    private lateinit var btnAddTransaction: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTransactions = findViewById(R.id.rv_transactions)
        tvTotalBalance = findViewById(R.id.tv_total_balance)
        tvTotalIncome = findViewById(R.id.tv_total_income)
        tvTotalExpense = findViewById(R.id.tv_total_expense)
        btnShowAllTransactions = findViewById(R.id.btn_show_all_transactions)
        btnAddTransaction = findViewById(R.id.btn_add_transaction)

        adapter = TransactionAdapter { transaction ->
        }
        rvTransactions.layoutManager = LinearLayoutManager(this)
        rvTransactions.adapter = adapter

        // Collect flows and update UI
        lifecycleScope.launch {
            viewModel.transactions.collect { transactions ->
                Log.d("MainActivity", "Transactions collected: $transactions")
                adapter.submitList(transactions)
            }
        }

        lifecycleScope.launch {
            viewModel.totalBalance.collect { balance ->
                val intBalance = balance.toInt()
                tvTotalBalance.text = String.format("%d", intBalance)
            }
        }

        lifecycleScope.launch {
            viewModel.totalIncome.collect { income ->
                val intIncome = income.toInt()
                tvTotalIncome.text = String.format("%d", intIncome)
            }
        }

        lifecycleScope.launch {
            viewModel.totalExpense.collect { expense ->
                val intExpense = expense.toInt()
                tvTotalExpense.text = String.format("%d", intExpense)
            }
        }

        // Button to show all transactions - Open AllTransactionsActivity
        btnShowAllTransactions.setOnClickListener {
            val intent = Intent(this, AllTransactionsActivity::class.java)
            startActivity(intent)
        }

        // Button to add a new transaction - Open AddTransactionActivity
        btnAddTransaction.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_TRANSACTION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_TRANSACTION) {
            viewModel.refreshTransactions()
        }
    }


    companion object {
        private const val REQUEST_CODE_ADD_TRANSACTION = 1
    }
}