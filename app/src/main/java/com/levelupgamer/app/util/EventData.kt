package com.levelupgamer.app.util

import com.levelupgamer.app.model.Event

object EventData {
    val events = mutableListOf(
        Event(
            id = "festigame-2025",
            name = "FestiGame 2025",
            description = "El evento de videojuegos más grande de Chile. ¡Prepárate!",
            date = "15-11-2025",
            time = "10:00",
            locationName = "Espacio Riesco, Santiago",
            inscriptionPoints = 200,
            prizePoints = 10000
        ),
        Event(
            id = "torneo-valorant-2026",
            name = "Torneo Valorant E-Sports Chile",
            description = "Demuestra tu habilidad en el shooter táctico del momento.",
            date = "20-01-2026",
            time = "14:30",
            locationName = "Movistar Arena, Santiago",
            inscriptionPoints = 100,
            prizePoints = 5000
        ),
        Event(
            id = "expo-indie-2026",
            name = "Expo Juegos Indie 2026",
            description = "Descubre las joyas ocultas del desarrollo de videojuegos nacional.",
            date = "03-03-2026",
            time = "11:00",
            locationName = "Centro Cultural Gabriela Mistral (GAM), Santiago",
            inscriptionPoints = 50,
            prizePoints = 2500
        )
    )
}