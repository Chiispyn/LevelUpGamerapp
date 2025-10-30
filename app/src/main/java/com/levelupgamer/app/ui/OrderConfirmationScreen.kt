package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun OrderConfirmationScreen(navController: NavController, orderId: String, pointsEarned: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Gracias por tu compra!", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        Text("Tu pedido ha sido recibido.")
        Text("Número de Orden: ${orderId.take(8)}...", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        if (pointsEarned > 0) {
            Text("¡Has ganado $pointsEarned puntos Level-Up!", 
                style = MaterialTheme.typography.titleMedium, 
                fontWeight = FontWeight.Bold, 
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
        }

        Text("Se ha enviado una copia de la boleta a tu correo.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(32.dp))
        Button(onClick = { navController.navigate("home") { popUpTo("main") { inclusive = true } } }) {
            Text("Volver al Inicio")
        }
    }
}