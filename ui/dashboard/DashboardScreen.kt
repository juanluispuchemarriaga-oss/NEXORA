package com.nexora.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexora.core.Money
import com.nexora.data.database.TransactionEntity
import com.nexora.data.database.TransactionType
import com.nexora.ui.theme.NexoraColors as C
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(onAdd: () -> Unit, vm: DashboardViewModel = viewModel(factory = DashboardViewModel.Factory)) {
    val s by vm.state.collectAsStateWithLifecycle()
    var hidden by remember { mutableStateOf(false) }
    fun m(v: Long) = if (hidden) "$ ••••" else Money.format(v)

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Header() }
        item {
            HeroCard(m(s.totalBalance), if (hidden) "••••" else Money.formatSigned(s.income - s.expense),
                m(s.income), m(s.expense), onToggle = { hidden = !hidden })
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(s.accounts) { a -> AccountCard(a.account.name, m(a.balance)) }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ActionChip("Agregar gasto", C.Expense, onAdd)
                ActionChip("Agregar ingreso", C.Income, onAdd)
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Movimientos", fontWeight = FontWeight.SemiBold)
                Text("Recientes", color = C.TextSecondary, fontSize = 12.sp)
            }
        }
        if (s.recent.isEmpty()) item {
            Text("Aún no hay movimientos.", color = C.TextSecondary, modifier = Modifier.fillMaxWidth().padding(24.dp), textAlign = TextAlign.Center)
        } else items(s.recent) { TransactionRow(it, hidden) }
    }
}

@Composable private fun Header() {
    val month = remember { SimpleDateFormat("MMMM yyyy", Locale("es")).format(Date()).replaceFirstChar { it.uppercase() } }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(36.dp).clip(CircleShape).background(C.SurfaceHigh), contentAlignment = Alignment.Center) { Text("N", fontWeight = FontWeight.Bold) }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text("Hola", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(month, fontSize = 12.sp, color = C.TextSecondary)
        }
        RoundIcon(Icons.Outlined.Search, "Buscar"); Spacer(Modifier.width(8.dp)); RoundIcon(Icons.Outlined.Notifications, "Avisos")
    }
}

@Composable private fun RoundIcon(icon: ImageVector, desc: String) =
    Box(Modifier.size(38.dp).clip(CircleShape).background(C.Surface), contentAlignment = Alignment.Center) {
        Icon(icon, desc, modifier = Modifier.size(18.dp))
    }

@Composable private fun HeroCard(total: String, delta: String, income: String, expense: String, onToggle: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(C.Accent).clickable(onClick = onToggle).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Saldo total", color = C.OnAccent, fontSize = 13.sp)
        Text(total, color = C.OnAccent, fontSize = 34.sp, fontWeight = FontWeight.Bold)
        Text(delta, color = C.OnAccent, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp)
            .clip(RoundedCornerShape(50)).background(Color.White.copy(alpha = 0.55f)).padding(horizontal = 10.dp, vertical = 2.dp))
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Flow(Icons.Outlined.ArrowDownward, C.Income, income, "Ingresos del mes")
            Flow(Icons.Outlined.ArrowUpward, C.Expense, expense, "Gastos del mes")
        }
    }
}

@Composable private fun Flow(icon: ImageVector, tint: Color, value: String, label: String) =
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(32.dp).clip(CircleShape).background(tint), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = C.OnAccent, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(8.dp))
        Column { Text(value, color = C.OnAccent, fontWeight = FontWeight.Bold, fontSize = 16.sp); Text(label, color = C.OnAccent, fontSize = 10.sp) }
    }

@Composable private fun AccountCard(name: String, balance: String) =
    Column(Modifier.width(150.dp).clip(RoundedCornerShape(18.dp)).background(C.Surface).padding(14.dp)) {
        Text(name, fontSize = 12.sp, color = C.TextSecondary)
        Spacer(Modifier.height(14.dp))
        Text(balance, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Guardado en el dispositivo", fontSize = 9.sp, color = C.TextSecondary)
    }

@Composable private fun ActionChip(label: String, dot: Color, onClick: () -> Unit) =
    Row(Modifier.clip(RoundedCornerShape(50)).background(C.Surface).clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).clip(CircleShape).background(dot)); Spacer(Modifier.width(8.dp)); Text(label, fontSize = 12.sp)
    }

@Composable private fun TransactionRow(t: TransactionEntity, hidden: Boolean) {
    val sign = when (t.type) { TransactionType.INCOME -> 1L; TransactionType.EXPENSE -> -1L; else -> 0L }
    val color = when (sign) { 1L -> C.Income; -1L -> C.Expense; else -> C.TextSecondary }
    val day = remember(t.date) { SimpleDateFormat("d MMM", Locale("es")).format(Date(t.date)) }
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(C.Surface).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(38.dp).clip(CircleShape).background(C.SurfaceHigh), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.ShoppingBag, null, tint = C.Accent, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(t.description.ifBlank { t.category }, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(t.category, fontSize = 11.sp, color = C.TextSecondary)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(if (hidden) "••••" else Money.formatSigned(sign * t.amount), color = color, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(day, fontSize = 10.sp, color = C.TextSecondary)
        }
    }
}
