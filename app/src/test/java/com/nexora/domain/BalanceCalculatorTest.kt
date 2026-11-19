package com.nexora.domain

import com.nexora.data.database.*
import org.junit.Assert.assertEquals
import org.junit.Test

class BalanceCalculatorTest {
    private val cash = AccountEntity(1, "Efectivo", AccountType.CASH, 100_000)
    private val bank = AccountEntity(2, "Banco", AccountType.BANK, 0)
    private fun tx(type: TransactionType, amt: Long, from: Long, to: Long? = null, date: Long = 10) =
        TransactionEntity(type = type, amount = amt, category = "x", date = date, accountId = from, toAccountId = to)

    @Test fun incomeAndExpenseUpdateBalance() {
        val txs = listOf(tx(TransactionType.INCOME, 50_000, 1), tx(TransactionType.EXPENSE, 20_000, 1))
        assertEquals(130_000L, BalanceCalculator.accountBalance(cash, txs))
    }

    @Test fun transferMovesMoneyWithoutChangingTotal() {
        val txs = listOf(tx(TransactionType.TRANSFER, 40_000, 1, 2))
        assertEquals(60_000L, BalanceCalculator.accountBalance(cash, txs))
        assertEquals(40_000L, BalanceCalculator.accountBalance(bank, txs))
        assertEquals(100_000L, BalanceCalculator.totalBalance(listOf(cash, bank), txs))
    }

    @Test fun transfersAreNotIncomeOrExpense() {
        val txs = listOf(tx(TransactionType.TRANSFER, 40_000, 1, 2), tx(TransactionType.INCOME, 5_000, 1))
        val t = BalanceCalculator.periodTotals(txs, 0, 100)
        assertEquals(5_000L, t.income); assertEquals(0L, t.expense)
    }

    @Test fun periodFiltersByDate() {
        val txs = listOf(tx(TransactionType.EXPENSE, 1_000, 1, date = 5), tx(TransactionType.EXPENSE, 2_000, 1, date = 50))
        assertEquals(2_000L, BalanceCalculator.periodTotals(txs, 20, 100).expense)
    }
}
