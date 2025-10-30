package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.levelupgamer.app.model.Order
import com.levelupgamer.app.util.OrderManager
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MyOrdersScreen() {
    val context = LocalContext.current
    val userEmail = remember { context.getSharedPreferences("user", 0).getString("email", "") ?: "" }
    val userOrders = remember(userEmail) { 
        OrderManager.getOrders(context).filter { it.userEmail == userEmail }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(userOrders) { order ->
            MyOrderItem(order = order)
        }
    }
}

@Composable
fun MyOrderItem(order: Order) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pedido: ${order.id.take(8)}...", fontWeight = FontWeight.Bold)
            Text("Fecha: ${dateFormatter.format(order.date)}")
            Text("Total: $${order.total}")
            Spacer(Modifier.height(8.dp))
            Text("Estado: ${order.status}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}