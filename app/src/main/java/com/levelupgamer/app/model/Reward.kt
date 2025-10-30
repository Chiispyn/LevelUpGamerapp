package com.levelupgamer.app.model

data class Reward(
    val id: String,
    val title: String,
    val description: String,
    val pointsCost: Int
)

// Lista de recompensas disponibles en la tienda de canje
val availableRewards = listOf(
    Reward("d-5000", "Descuento de $5.000", "Un descuento de $5.000 para tu próxima compra.", 5000),
    Reward("d-10000", "Descuento de $10.000", "Un gran descuento de $10.000 para tu próxima compra.", 9000),
    Reward("s-free", "Envío Gratis", "Obtén envío gratis en tu próximo pedido a todo Chile.", 7500),
    Reward("p-polera", "Polera Exclusiva Level-Up", "Una polera de edición limitada para verdaderos gamers.", 15000)
)