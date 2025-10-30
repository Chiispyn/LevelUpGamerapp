package com.levelupgamer.app.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.R
import com.levelupgamer.app.model.Event
import com.levelupgamer.app.util.EventData
import com.levelupgamer.app.util.RewardsManager

@Composable
fun EventDetailScreen(navController: NavController, eventId: String) {
    val context = LocalContext.current
    val event = remember { EventData.events.find { it.id == eventId } } ?: return

    var hasEnrolled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(event.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text("Fecha: ${event.date} a las ${event.time} hrs.") // <-- HORA AÑADIDA
        Text("Lugar: ${event.locationName}")
        Spacer(Modifier.height(16.dp))
        Text(event.description, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))

        // Mapa Simulado
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Mapa del evento",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                // --- LÓGICA CORREGIDA ---
                // Busca por el nombre del lugar en lugar de coordenadas
                val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(event.locationName)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cómo Llegar")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                if (!hasEnrolled) {
                    RewardsManager.addPoints(context, event.inscriptionPoints)
                    hasEnrolled = true
                }
            },
            enabled = !hasEnrolled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (hasEnrolled) "¡Inscrito! (+${event.inscriptionPoints} puntos)" else "Inscribirse (+${event.inscriptionPoints} puntos)")
        }
    }
}