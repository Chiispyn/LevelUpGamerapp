package com.levelupgamer.app.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.UserLevel
import com.levelupgamer.app.util.RewardsManager

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userPrefs = remember { context.getSharedPreferences("user", Context.MODE_PRIVATE) }
    val name = remember { userPrefs.getString("name", "Usuario") ?: "Usuario" }
    val email = remember { userPrefs.getString("email", "") ?: "" }
    
    val points = remember { RewardsManager.getUserPoints(context) }
    val level = remember { RewardsManager.getUserLevel(context) }
    val referralCode = remember { RewardsManager.getUserReferralCode(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = level.color.copy(alpha = 0.3f))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(email, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text("Nivel: ${level.levelName}", fontWeight = FontWeight.Bold)
                Text("Puntos Level-Up: $points")
                Spacer(Modifier.height(8.dp))
                Text("Tu código de referido: $referralCode", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = { navController.navigate("editProfile") }, modifier = Modifier.fillMaxWidth()) {
            Text("Editar Perfil y Direcciones")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { navController.navigate("myOrders") }, modifier = Modifier.fillMaxWidth()) {
            Text("Mis Compras")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { navController.navigate("rewardsShop") }, modifier = Modifier.fillMaxWidth()) {
            Text("Mi Nivel y Beneficios")
        }

        Spacer(Modifier.weight(1f))

        Button(onClick = {
            val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
            context.getSharedPreferences("cart", Context.MODE_PRIVATE).edit().clear().apply()
            context.getSharedPreferences("rewards", Context.MODE_PRIVATE).edit().clear().apply()
            context.getSharedPreferences("addresses", Context.MODE_PRIVATE).edit().clear().apply()
            
            navController.navigate("login") {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar Sesión")
        }
    }
}