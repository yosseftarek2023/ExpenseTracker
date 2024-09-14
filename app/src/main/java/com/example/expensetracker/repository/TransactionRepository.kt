    package com.example.expensetracker.repository

    import com.example.expensetracker.database.TransactionDao
    import com.example.expensetracker.model.Transaction
    import com.example.expensetracker.model.TransactionType
    import kotlinx.coroutines.flow.Flow

    class TransactionRepository(private val transactionDao: TransactionDao) {

        suspend fun insertTransaction(transaction: Transaction){
            transactionDao.insertTransaction(transaction)
        }

        fun getAllTransactions(): Flow<List<Transaction>> {
            return transactionDao.getAllTransactions() // Return the Flow directly
        }

        suspend fun getTransactionByType(type:TransactionType):List<Transaction>{
            return transactionDao.getTransactionsByType(type)
        }

        suspend fun getTotalAmountByType(type: TransactionType):  Double?{
            return transactionDao.getTotalAmountByType(type)
        }
        suspend fun deleteTransactionById(transactionId: Long) {
            transactionDao.deleteTransactionById(transactionId)
        }
    }