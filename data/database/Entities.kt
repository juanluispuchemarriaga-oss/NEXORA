package com.nexora.data.database

import androidx.room.*

enum class AccountType { CASH, BANK, SAVINGS, WALLET, CUSTOM }
enum class TransactionType { INCOME, EXPENSE, TRANSFER }
enum class DebtDirection { OWED_TO_ME, I_OWE }

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: AccountType,
    val initialBalance: Long,
    val currency: String = "COP",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(AccountEntity::class, ["id"], ["accountId"], onDelete = ForeignKey.RESTRICT),
        ForeignKey(AccountEntity::class, ["id"], ["toAccountId"], onDelete = ForeignKey.RESTRICT)
    ],
    indices = [Index("accountId"), Index("toAccountId"), Index("date")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: TransactionType,
    val amount: Long,               // siempre positivo; el tipo define el signo
    val category: String,
    val description: String = "",
    val date: Long,
    val accountId: Long,
    val toAccountId: Long? = null   // solo en transferencias
)

@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personName: String,
    val direction: DebtDirection,
    val initialAmount: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val dueDate: Long? = null,
    val description: String = ""
)

/** Saldo de deuda = initialAmount - suma de abonos. Los abonos no se editan destructivamente. */
@Entity(
    tableName = "debt_payments",
    foreignKeys = [ForeignKey(DebtEntity::class, ["id"], ["debtId"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index("debtId")]
)
data class DebtPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val debtId: Long,
    val amount: Long,
    val date: Long,
    val note: String = ""
)
