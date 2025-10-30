package com.levelupgamer.app.model

import androidx.compose.ui.graphics.Color
import com.levelupgamer.app.ui.theme.BlueElectric

// Definimos los niveles y sus propiedades
enum class UserLevel(
    val levelName: String,
    val requiredPoints: Int,
    val discountPercentage: Int,
    val color: Color
) {
    BRONZE("Bronze", 0, 0, Color.Gray),
    SILVER("Silver", 5000, 5, Color.LightGray),
    GOLD("Gold", 20000, 10, Color.Yellow),
    VIP("V.I.P.", 50000, 15, BlueElectric)
}