package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.Event
import com.levelupgamer.app.util.EventManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEventsScreen(navController: NavController) {
    var events by remember(navController.currentBackStackEntry) { mutableStateOf(EventManager.getEvents()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("eventEdit") }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Evento")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(events, key = { it.id }) { event ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(event.name, fontWeight = FontWeight.Bold)
                            Text("Fecha: ${event.date} | Lugar: ${event.locationName}")
                        }
                        IconButton(onClick = { navController.navigate("eventEdit/${event.id}") }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = {
                            EventManager.deleteEvent(event.id)
                            events = EventManager.getEvents()
                        }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
                    }
                }
            }
        }
    }
}