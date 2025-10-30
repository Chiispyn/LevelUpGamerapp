package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.Event
import com.levelupgamer.app.ui.components.visual_transformation.DateVisualTransformation
import com.levelupgamer.app.util.EventManager

@Composable
fun EventEditScreen(navController: NavController, eventId: String?) {
    val isEditing = eventId != null
    val eventToEdit = remember { if (isEditing) EventManager.getEvent(eventId!!) else null }

    var id by remember { mutableStateOf(eventToEdit?.id ?: "") }
    var name by remember { mutableStateOf(eventToEdit?.name ?: "") }
    var description by remember { mutableStateOf(eventToEdit?.description ?: "") }
    var date by remember { mutableStateOf(eventToEdit?.date ?: "") }
    var time by remember { mutableStateOf(eventToEdit?.time ?: "") }
    var locationName by remember { mutableStateOf(eventToEdit?.locationName ?: "") }
    var inscriptionPoints by remember { mutableStateOf(eventToEdit?.inscriptionPoints?.toString() ?: "") }
    var prizePoints by remember { mutableStateOf(eventToEdit?.prizePoints?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(if (isEditing) "Editar Evento" else "Añadir Evento", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        if (!isEditing) {
            OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("ID del Evento") }, modifier = Modifier.fillMaxWidth())
        }
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre del Evento") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = date, 
            onValueChange = { if (it.length <= 8) date = it.filter { char -> char.isDigit() } }, 
            label = { Text("Fecha (dd-MM-yyyy)") }, 
            visualTransformation = DateVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Hora (HH:mm)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = locationName, onValueChange = { locationName = it }, label = { Text("Nombre del Lugar") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = inscriptionPoints, onValueChange = { inscriptionPoints = it }, label = { Text("Puntos por Inscripción") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = prizePoints, onValueChange = { prizePoints = it }, label = { Text("Puntos de Premio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val event = Event(
                    id = if (isEditing) eventToEdit!!.id else id,
                    name = name,
                    description = description,
                    date = date,
                    time = time,
                    locationName = locationName,
                    inscriptionPoints = inscriptionPoints.toIntOrNull() ?: 0,
                    prizePoints = prizePoints.toIntOrNull() ?: 0
                )

                if (isEditing) {
                    EventManager.updateEvent(event)
                } else {
                    EventManager.addEvent(event)
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditing) "Guardar Cambios" else "Añadir Evento")
        }
    }
}