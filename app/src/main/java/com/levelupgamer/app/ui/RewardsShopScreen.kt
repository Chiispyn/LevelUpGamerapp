package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.levelupgamer.app.model.Reward
import com.levelupgamer.app.model.availableRewards
import com.levelupgamer.app.util.RewardsManager

@Composable
fun RewardsShopScreen() {
    val context = LocalContext.current
    var userPoints by remember { mutableStateOf(RewardsManager.getUserPoints(context)) }
    val userLevel = remember { RewardsManager.getUserLevel(context) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tienda de Canje", style = MaterialTheme.typography.headlineLarge)
        Text("Tus Puntos: $userPoints", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(availableRewards) { reward ->
                RewardItem(reward = reward, userPoints = userPoints, onRedeem = {
                    if (RewardsManager.redeemReward(context, reward)) {
                        userPoints = RewardsManager.getUserPoints(context) // Actualiza los puntos en la UI
                    }
                })
            }
        }
    }
}

@Composable
fun RewardItem(reward: Reward, userPoints: Int, onRedeem: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(reward.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(reward.description, style = MaterialTheme.typography.bodySmall)
                Text("Costo: ${reward.pointsCost} puntos", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
            }
            Button(
                onClick = onRedeem,
                enabled = userPoints >= reward.pointsCost
            ) {
                Text("Canjear")
            }
        }
    }
}