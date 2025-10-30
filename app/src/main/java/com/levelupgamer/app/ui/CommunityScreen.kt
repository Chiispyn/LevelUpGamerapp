package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.Event
import com.levelupgamer.app.util.EventData

@Composable
fun CommunityScreen(navController: NavController) {
    val events = EventData.events

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Próximos Eventos", style = MaterialTheme.typography.headlineLarge)
        }
        items(events) { event ->
            EventCard(event = event, navController = navController)
        }
    }
}

@Composable
fun EventCard(event: Event, navController: NavController) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Fecha: ${event.date}")
            Text("Lugar: ${event.locationName}")
            Spacer(Modifier.height(8.dp))
            Text(event.description)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { navController.navigate("event/${event.id}") }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Detalles e Inscribirse")
            }
        }
    }
}