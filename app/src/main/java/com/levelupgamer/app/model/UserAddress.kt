package com.levelupgamer.app.model

import java.util.UUID

data class UserAddress(
    val id: String = UUID.randomUUID().toString(), // Para identificar cada dirección de forma única
    val street: String,
    val numberOrApt: String,
    val commune: String,
    val region: String,
    val isPrimary: Boolean = false // Para marcar una como la dirección por defecto
)