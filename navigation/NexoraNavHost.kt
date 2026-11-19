package com.nexora.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.nexora.ui.common.PlaceholderScreen
import com.nexora.ui.components.NexoraBottomBar
import com.nexora.ui.dashboard.DashboardScreen

@Composable
fun NexoraNavHost() {
    val nav = rememberNavController()
    val current = nav.currentBackStackEntryAsState().value?.destination?.route
    Box(Modifier.fillMaxSize().statusBarsPadding()) {
        NavHost(nav, startDestination = Destination.Home.route, modifier = Modifier.fillMaxSize()) {
            composable(Destination.Home.route) { DashboardScreen(onAdd = { nav.navigate(ROUTE_ADD) }) }
            composable(Destination.Transactions.route) { PlaceholderScreen("Movimientos", "Fase 2") }
            composable(Destination.Debts.route) { PlaceholderScreen("Deudas", "Fase 3") }
            composable(Destination.Statistics.route) { PlaceholderScreen("Estadísticas", "Fase 4") }
            composable(Destination.Settings.route) { PlaceholderScreen("Configuración", "Fase 2") }
            composable(ROUTE_ADD) { PlaceholderScreen("Nuevo movimiento", "Fase 2") }
        }
        if (current != ROUTE_ADD) {
            NexoraBottomBar(
                current = current,
                onSelect = { d -> nav.navigate(d.route) { popUpTo(Destination.Home.route); launchSingleTop = true } },
                onAdd = { nav.navigate(ROUTE_ADD) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
