package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.levelupgamer.app.model.Order
import com.levelupgamer.app.model.OrderStatus
import com.levelupgamer.app.util.OrderManager
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AdminOrdersScreen() {
    val context = LocalContext.current
    var orders by remember { mutableStateOf(OrderManager.getOrders(context)) }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(orders) { order ->
            AdminOrderItem(order = order, onStatusChange = {
                OrderManager.updateOrderStatus(context, order.id, it)
                orders = OrderManager.getOrders(context) // Refresh list
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderItem(order: Order, onStatusChange: (OrderStatus) -> Unit) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pedido: ${order.id.take(8)}...", fontWeight = FontWeight.Bold)
            Text("Cliente: ${order.userEmail}")
            Text("Fecha: ${dateFormatter.format(order.date)}")
            Text("Total: $${order.total}")
            Spacer(Modifier.height(8.dp))
            Text("Estado Actual: ${order.status}", fontWeight = FontWeight.Bold)

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = order.status.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cambiar Estado") },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    OrderStatus.values().forEach { status ->
                        DropdownMenuItem(text = { Text(status.name) }, onClick = {
                            onStatusChange(status)
                            expanded = false
                        })
                    }
                }
            }
        }
    }
}