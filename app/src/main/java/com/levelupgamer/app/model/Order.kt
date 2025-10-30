package com.levelupgamer.app.model

import java.util.Date
import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val products: List<Product>,
    val subtotal: Int, // Renombrado para claridad
    val shippingCost: Int, // Nuevo campo
    val total: Int, // Subtotal + Envío - Descuento
    val date: Date = Date(),
    val userEmail: String,
    val shippingAddress: UserAddress,
    val status: OrderStatus = OrderStatus.PROCESANDO
)