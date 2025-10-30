package com.levelupgamer.app.util

import com.levelupgamer.app.model.Event

// Simulación de gestión de eventos. En una app real, esto interactuaría con una base de datos.
object EventManager {

    private var eventList = EventData.events.toMutableList()

    fun getEvents(): List<Event> {
        return eventList
    }

    fun getEvent(id: String): Event? {
        return eventList.find { it.id == id }
    }

    fun addEvent(event: Event) {
        if (eventList.none { it.id == event.id }) {
            eventList.add(event)
        }
    }

    fun updateEvent(updatedEvent: Event) {
        val index = eventList.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            eventList[index] = updatedEvent
        }
    }

    fun deleteEvent(id: String) {
        eventList.removeAll { it.id == id }
    }
}