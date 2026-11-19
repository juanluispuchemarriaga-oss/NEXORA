package com.nexora.domain

import com.nexora.data.database.AccountEntity
import com.nexora.data.database.TransactionEntity
import com.nexora.data.database.TransactionType

data class PeriodTotals(val income: Long, val expense: Long) { val net get() = income - expense }

object BalanceCalculator {
    /** Saldo = inicial + ingresos - gastos - transferencias enviadas + transferencias recibidas. */
    fun accountBalance(account: AccountEntity, txs: List<TransactionEntity>): Long {
        var b = account.initialBalance
        for (t in txs) when (t.type) {
            TransactionType.INCOME -> if (t.accountId == account.id) b += t.amount
            TransactionType.EXPENSE -> if (t.accountId == account.id) b -= t.amount
            TransactionType.TRANSFER -> {
                if (t.accountId == account.id) b -= t.amount
                if (t.toAccountId == account.id) b += t.amount
            }
        }
        return b
    }

    fun totalBalance(accounts: List<AccountEntity>, txs: List<TransactionEntity>) =
        accounts.sumOf { accountBalance(it, txs) }

    /** Las transferencias nunca cuentan como ingreso ni gasto. */
    fun periodTotals(txs: List<TransactionEntity>, from: Long, to: Long): PeriodTotals {
        val inRange = txs.filter { it.date in from..to }
        return PeriodTotals(
            income = inRange.filter { it.type == TransactionType.INCOME }.sumOf { it.amount },
            expense = inRange.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        )
    }
}
