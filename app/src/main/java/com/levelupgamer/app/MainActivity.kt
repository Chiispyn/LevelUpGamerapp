package com.levelupgamer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.levelupgamer.app.ui.AdminFeature // <-- El nuevo punto de entrada para TODO el admin
import com.levelupgamer.app.ui.MainScreen
import com.levelupgamer.app.ui.auth.LoginScreen
import com.levelupgamer.app.ui.auth.RegisterScreen
import com.levelupgamer.app.ui.theme.LevelUpGamerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevelUpGamerTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("main") { MainScreen() }
        // La ruta "admin" ahora lanza el componente que contiene TODO el universo del administrador
        composable("admin") { AdminFeature(mainNavController = navController) }
    }
}