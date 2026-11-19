package com.nexora.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AccountEntity::class, TransactionEntity::class, DebtEntity::class, DebtPaymentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NexoraDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun debtDao(): DebtDao

    companion object {
        fun build(context: Context): NexoraDatabase =
            Room.databaseBuilder(context, NexoraDatabase::class.java, "nexora.db").build()
    }
}
