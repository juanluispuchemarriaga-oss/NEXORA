package com.nexora.data

import com.nexora.data.database.*

class FinanceRepository(private val db: NexoraDatabase) {
    fun accounts() = db.accountDao().observeAll()
    fun transactions() = db.transactionDao().observeAll()

    /** Provisional hasta la pantalla de configuración inicial (Fase 2). */
    suspend fun ensureDefaultAccount() {
        if (db.accountDao().count() == 0)
            db.accountDao().insert(AccountEntity(name = "Efectivo", type = AccountType.CASH, initialBalance = 0))
    }
}
