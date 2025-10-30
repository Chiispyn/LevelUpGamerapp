package com.levelupgamer.app.util

data class ShippingInfo(val cost: Int, val estimatedDays: String)

object ShippingManager {

    private val shippingCosts: Map<String, ShippingInfo> = mapOf(
        "Arica y Parinacota" to ShippingInfo(5000, "3-5 días"),
        "Tarapacá" to ShippingInfo(4500, "3-5 días"),
        "Antofagasta" to ShippingInfo(4000, "2-4 días"),
        "Atacama" to ShippingInfo(3500, "2-4 días"),
        "Coquimbo" to ShippingInfo(3000, "2-3 días"),
        "Valparaíso" to ShippingInfo(2500, "1-2 días"),
        "Metropolitana de Santiago" to ShippingInfo(2000, "1-2 días"),
        "Libertador General Bernardo O'Higgins" to ShippingInfo(2500, "1-2 días"),
        "Maule" to ShippingInfo(3000, "2-3 días"),
        "Ñuble" to ShippingInfo(3500, "2-4 días"),
        "Biobío" to ShippingInfo(3500, "2-4 días"),
        "La Araucanía" to ShippingInfo(4000, "3-5 días"),
        "Los Ríos" to ShippingInfo(4500, "3-5 días"),
        "Los Lagos" to ShippingInfo(5000, "4-6 días"),
        "Aysén del General Carlos Ibáñez del Campo" to ShippingInfo(7000, "5-10 días"),
        "Magallanes y de la Antártica Chilena" to ShippingInfo(8000, "5-10 días")
    )

    fun getShippingInfo(region: String): ShippingInfo {
        return shippingCosts[region] ?: ShippingInfo(9000, "No disponible") // Un valor por defecto
    }
}