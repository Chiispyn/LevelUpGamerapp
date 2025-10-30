package com.levelupgamer.app.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.Order
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.util.CartManager
import com.levelupgamer.app.util.OrderManager

@Composable
fun CartScreen(
    navController: NavController,
    cartItems: List<Product>,
    onQuantityChanged: (Product, Int) -> Unit,
    onProductRemoved: (Product) -> Unit,
    onCartCleared: () -> Unit
) {
    val context = LocalContext.current
    val total = remember(cartItems) { CartManager.getTotal(context, cartItems) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
        } else {
            LazyColumn(Modifier.weight(1f)) {
                items(items = cartItems, key = { it.code }) { product ->
                    CartItem(
                        product = product,
                        onQtyChange = { delta -> onQuantityChanged(product, delta) },
                        onRemove = { onProductRemoved(product) }
                    )
                }
            }

            Divider()
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total: $$total", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.weight(1f))
                Button(onClick = onCartCleared) {
                    Text("Vaciar Carrito")
                }
            }
            Button(
                onClick = { navController.navigate("checkout") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text("Ir a Pagar")
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun CartItem(product: Product, onQtyChange: (Int) -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text("$${product.price}", style = MaterialTheme.typography.bodyLarge)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onQtyChange(-1) }) {
                    Text("–")
                }
                Text("${product.quantity ?: 1}")
                IconButton(onClick = { onQtyChange(1) }) {
                    Text("+")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = onRemove) {
                    Text("Quitar")
                }
            }
        }
    }
}