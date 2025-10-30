package com.levelupgamer.app.model

// Modelo de Evento Actualizado
data class Event(
    val id: String,
    val name: String,
    val description: String,
    val date: String, // formato dd-MM-yyyy
    val time: String, // formato HH:mm
    val locationName: String, // El nombre del lugar es suficiente
    val inscriptionPoints: Int,
    val prizePoints: Int
)