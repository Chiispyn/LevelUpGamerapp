package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.levelupgamer.app.model.UserLevel
import com.levelupgamer.app.util.RewardsManager

@Composable
fun RewardsScreen() {
    val context = LocalContext.current
    val userLevel = remember { RewardsManager.getUserLevel(context) }
    val userPoints = remember { RewardsManager.getUserPoints(context) }
    val allLevels = remember { UserLevel.values() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Programa de Recompensas", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))
            Text("Tu Nivel: ${userLevel.levelName}", style = MaterialTheme.typography.titleLarge, color = userLevel.color)
            Text("Puntos Actuales: $userPoints", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(24.dp))
            Text("Niveles Disponibles", style = MaterialTheme.typography.headlineSmall)
        }

        items(allLevels) { level ->
            LevelCard(level = level)
        }
    }
}

@Composable
fun LevelCard(level: UserLevel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = level.color.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(level.levelName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = level.color)
            Text("Puntos Requeridos: ${level.requiredPoints}")
            Spacer(Modifier.height(8.dp))
            Text("Beneficios:", fontWeight = FontWeight.Bold)
            when (level) {
                UserLevel.BRONZE -> Text("Acceso al programa de puntos.")
                UserLevel.SILVER -> Text("Descuento del 5% en todos los pedidos.")
                UserLevel.GOLD -> Text("Descuento del 10% y soporte prioritario.")
                UserLevel.VIP -> Text("Descuento del 15%, regalo de cumpleaños y acceso a eventos VIP.")
            }
        }
    }
}