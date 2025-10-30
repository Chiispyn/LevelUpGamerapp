package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.ui.navigation.NavScreen
import com.levelupgamer.app.util.CartManager

// Importaciones de todas las pantallas del flujo de usuario
import com.levelupgamer.app.ui.HomeScreen
import com.levelupgamer.app.ui.CatalogScreen
import com.levelupgamer.app.ui.CommunityScreen
import com.levelupgamer.app.ui.ProfileScreen
import com.levelupgamer.app.ui.CartScreen
import com.levelupgamer.app.ui.ReviewScreen
import com.levelupgamer.app.ui.EditProfileScreen
import com.levelupgamer.app.ui.AddressScreen
import com.levelupgamer.app.ui.OrderConfirmationScreen
import com.levelupgamer.app.ui.CheckoutScreen
import com.levelupgamer.app.ui.MyOrdersScreen
import com.levelupgamer.app.ui.RewardsShopScreen
import com.levelupgamer.app.ui.EventDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    var cartItems by remember { mutableStateOf(CartManager.getItems(context)) }

    val onProductAdded: (Product) -> Unit = { cartItems = CartManager.add(context, it).toMutableList() }
    val onProductRemoved: (Product) -> Unit = { cartItems = CartManager.remove(context, it).toMutableList() }
    val onQuantityChanged: (Product, Int) -> Unit = { product, delta -> cartItems = CartManager.changeQty(context, product, delta).toMutableList() }
    val onCartCleared: () -> Unit = { cartItems = CartManager.clear(context).toMutableList() }

    val bottomNavScreens = listOf(NavScreen.Home, NavScreen.Store, NavScreen.Redeem, NavScreen.Community, NavScreen.Profile)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(getScreenTitle(currentRoute, bottomNavScreens)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    if (currentRoute != null && currentRoute !in bottomNavScreens.map { it.route }) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavScreens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = NavScreen.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(NavScreen.Home.route) { HomeScreen(navController, cartItems, onProductAdded) }
            composable(NavScreen.Store.route) { CatalogScreen(navController, cartItems, onProductAdded) }
            composable(NavScreen.Redeem.route) { RewardsShopScreen() }
            composable(NavScreen.Community.route) { CommunityScreen(navController) }
            composable(NavScreen.Profile.route) { ProfileScreen(navController) }
            composable("cart") { CartScreen(navController, cartItems, onQuantityChanged, onProductRemoved, onCartCleared) }
            composable("checkout") { CheckoutScreen(navController, cartItems, onCartCleared) }
            composable("myOrders") { MyOrdersScreen() }
            composable("rewardsShop") { RewardsShopScreen() }
            composable("event/{eventId}") { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                EventDetailScreen(navController, eventId)
            }
            composable("review/{productCode}") { /* ... */ }
            composable("editProfile") { EditProfileScreen(navController) }
            composable("address") { AddressScreen(navController) }
            composable("address/{addressId}") { /* ... */ }
            composable("orderConfirmation/{orderId}/{pointsEarned}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                val pointsEarned = backStackEntry.arguments?.getString("pointsEarned")?.toInt() ?: 0
                OrderConfirmationScreen(navController, orderId, pointsEarned)
            }
        }
    }
}

private fun getScreenTitle(route: String?, bottomNavScreens: List<NavScreen>): String {
    val mainScreen = bottomNavScreens.find { it.route == route }
    if (mainScreen != null) return mainScreen.title

    return when {
        route?.startsWith("event/") == true -> "Detalle del Evento"
        route?.startsWith("orderConfirmation") == true -> "Compra Finalizada"
        route?.startsWith("address") == true -> "Gestión de Dirección"
        route?.startsWith("review") == true -> "Escribir Reseña"
        route == "cart" -> "Carrito"
        route == "checkout" -> "Finalizar Compra"
        route == "myOrders" -> "Mis Compras"
        route == "rewardsShop" -> "Tienda de Canje"
        route == "editProfile" -> "Editar Perfil"
        else -> "Level-Up Gamer"
    }
}