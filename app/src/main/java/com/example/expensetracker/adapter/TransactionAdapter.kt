package com.example.expensetracker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.model.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(private val onClick: (Transaction) -> Unit) :
    ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.bind(transaction)
    }

    class TransactionViewHolder(itemView: View, private val onClick: (Transaction) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val typeView: TextView = itemView.findViewById(R.id.tv_type)
        private val dateView: TextView = itemView.findViewById(R.id.tv_date)
        private val amountView: TextView = itemView.findViewById(R.id.tv_amount)
        private val categoryView: TextView = itemView.findViewById(R.id.tv_category)
        private var currentTransaction: Transaction? = null

        init {
            itemView.setOnClickListener {
                currentTransaction?.let { transaction ->
                    onClick(transaction)
                }
            }
        }

        fun bind(transaction: Transaction) {
            currentTransaction = transaction
            typeView.text = transaction.type.toString() // Convert enum to string
            dateView.text = formatDate(transaction.date) // Convert Date to string
            amountView.text = formatAmount(transaction.amount) // Format amount
            categoryView.text = transaction.category // Display category as is
            Log.d("TransactionAdapter", "Binding transaction: $transaction")

        }

        private fun formatAmount(amount: Double): String {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            return numberFormat.format(amount)
        }

        private fun formatDate(date: Date): String {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return dateFormat.format(date)
        }
    }
}

class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
        oldItem == newItem
}
