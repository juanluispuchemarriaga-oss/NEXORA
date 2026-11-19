package com.nexora.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nexora.NexoraApp
import com.nexora.data.FinanceRepository
import com.nexora.data.database.AccountEntity
import com.nexora.data.database.TransactionEntity
import com.nexora.domain.BalanceCalculator
import kotlinx.coroutines.flow.*
import java.util.Calendar

data class AccountBalance(val account: AccountEntity, val balance: Long)

data class DashboardState(
    val totalBalance: Long = 0, val income: Long = 0, val expense: Long = 0,
    val accounts: List<AccountBalance> = emptyList(), val recent: List<TransactionEntity> = emptyList()
)

class DashboardViewModel(repo: FinanceRepository) : ViewModel() {
    val state: StateFlow<DashboardState> = combine(repo.accounts(), repo.transactions()) { accs, txs ->
        val cal = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val totals = BalanceCalculator.periodTotals(txs, cal.timeInMillis, Long.MAX_VALUE)
        DashboardState(
            totalBalance = BalanceCalculator.totalBalance(accs, txs),
            income = totals.income, expense = totals.expense,
            accounts = accs.map { AccountBalance(it, BalanceCalculator.accountBalance(it, txs)) },
            recent = txs.take(5)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardState())

    companion object {
        val Factory = viewModelFactory {
            initializer { DashboardViewModel((this[APPLICATION_KEY] as NexoraApp).repository) }
        }
    }
}
