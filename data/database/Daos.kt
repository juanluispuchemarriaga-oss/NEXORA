package com.nexora.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY id") fun observeAll(): Flow<List<AccountEntity>>
    @Query("SELECT COUNT(*) FROM accounts") suspend fun count(): Int
    @Insert suspend fun insert(account: AccountEntity): Long
    @Update suspend fun update(account: AccountEntity)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC") fun observeAll(): Flow<List<TransactionEntity>>
    @Insert suspend fun insert(tx: TransactionEntity): Long
    @Update suspend fun update(tx: TransactionEntity)
    @Delete suspend fun delete(tx: TransactionEntity)
}

@Dao
interface DebtDao {
    @Query("SELECT * FROM debts ORDER BY createdAt DESC") fun observeAll(): Flow<List<DebtEntity>>
    @Query("SELECT * FROM debt_payments ORDER BY date DESC") fun observePayments(): Flow<List<DebtPaymentEntity>>
    @Insert suspend fun insert(debt: DebtEntity): Long
    @Insert suspend fun insertPayment(payment: DebtPaymentEntity): Long
}
