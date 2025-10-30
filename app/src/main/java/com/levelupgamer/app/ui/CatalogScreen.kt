package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.ui.theme.BlueElectric
import com.levelupgamer.app.ui.theme.GreenLime
import com.levelupgamer.app.util.MockData
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CatalogScreen(navController: NavController, cartItems: List<Product>, onProductAdded: (Product) -> Unit) {
    val products = remember { MockData.products }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product, 
                navController = navController, 
                isInCart = cartItems.any { it.code == product.code }, // Comprueba si el producto está en el carrito
                onProductAdded = onProductAdded
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product, 
    navController: NavController, 
    isInCart: Boolean, 
    onProductAdded: (Product) -> Unit
) {
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color.Yellow)
                    Text("${product.rating} (${product.ratingCount})", style = MaterialTheme.typography.bodySmall)
                }
                Text(formatter.format(product.price), fontWeight = FontWeight.Bold, color = BlueElectric)
                Spacer(Modifier.height(8.dp))

                // --- Botón Inteligente ---
                if (isInCart) {
                    Button(
                        onClick = { navController.navigate("cart") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ir al Carrito")
                    }
                } else {
                    Button(
                        onClick = { onProductAdded(product) },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenLime),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Añadir al Carrito", color = Color.Black)
                    }
                }
            }
        }
    }
}