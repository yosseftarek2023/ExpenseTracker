import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.model.Transaction
import com.example.expensetracker.model.TransactionType
import com.example.expensetracker.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: TransactionRepository) : ViewModel() {
    private val _totalBalance = MutableStateFlow(0.0)
    val totalBalance: StateFlow<Double> = _totalBalance.asStateFlow()

    private val _totalIncome = MutableStateFlow(0.0)
    val totalIncome: StateFlow<Double> = _totalIncome.asStateFlow()

    private val _totalExpense = MutableStateFlow(0.0)
    val totalExpense: StateFlow<Double> = _totalExpense.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    init {
        fetchTransactions()
    }

    private fun fetchTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions()
                .collect { allTransactions ->
                    _transactions.value = allTransactions

                    // Calculate totals
                    val income = allTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
                    val expense = allTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

                    _totalIncome.value = income
                    _totalExpense.value = expense
                    _totalBalance.value = income - expense
                }
        }
    }

    fun refreshTransactions() {
        fetchTransactions()
    }
}