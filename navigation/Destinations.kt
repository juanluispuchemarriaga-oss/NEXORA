package com.nexora.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Inicio", Icons.Outlined.Home),
    Transactions("transactions", "Movimientos", Icons.Outlined.SwapVert),
    Debts("debts", "Deudas", Icons.Outlined.People),
    Statistics("statistics", "Estadísticas", Icons.Outlined.BarChart),
    Settings("settings", "Configuración", Icons.Outlined.Settings)
}

const val ROUTE_ADD = "add_transaction"
