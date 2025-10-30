package com.levelupgamer.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : NavScreen("home", "Inicio", Icons.Default.Home)
    object Store : NavScreen("catalog", "Tienda", Icons.Default.Store)
    object Redeem : NavScreen("redeem", "Canjear", Icons.Default.WorkspacePremium) // <-- Nueva Sección
    object Community : NavScreen("community", "Comunidad", Icons.Default.People)
    object Profile : NavScreen("profile", "Perfil", Icons.Default.Person)
}