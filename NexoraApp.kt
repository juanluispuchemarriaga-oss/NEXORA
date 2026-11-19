package com.nexora

import android.app.Application
import com.nexora.data.FinanceRepository
import com.nexora.data.database.NexoraDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NexoraApp : Application() {
    val database by lazy { NexoraDatabase.build(this) }
    val repository by lazy { FinanceRepository(database) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch { repository.ensureDefaultAccount() }
    }
}
