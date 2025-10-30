package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.util.MockData

@Composable
fun HomeScreen(navController: NavController, cartItems: List<Product>, onProductAdded: (Product) -> Unit) {
    val products = remember { MockData.products.shuffled().take(4) } // Mostramos 4 productos al azar

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("¡Bienvenido a Level-Up Gamer!", style = MaterialTheme.typography.headlineMedium)
        Text("Tu próximo nivel, nuestro compromiso.", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Text("Productos Destacados", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products, key = { it.code }) { product ->
                ProductCard(
                    product = product, 
                    navController = navController, 
                    isInCart = cartItems.any { cartItem -> cartItem.code == product.code },
                    onProductAdded = onProductAdded
                )
            }
        }
    }
}