package com.levelupgamer.app.ui.auth

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.UserAddress
import com.levelupgamer.app.util.AddressManager
import com.levelupgamer.app.util.RewardsManager

// Funciones auxiliares movidas aquí para mantener el LoginScreen limpio
private fun clearAllData(context: Context) {
    context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().apply()
    context.getSharedPreferences("cart", Context.MODE_PRIVATE).edit().clear().apply()
    context.getSharedPreferences("rewards", Context.MODE_PRIVATE).edit().clear().apply()
    context.getSharedPreferences("addresses", Context.MODE_PRIVATE).edit().clear().apply()
}

private fun setupTestUser(context: Context, email: String, name: String, addresses: List<UserAddress>) {
    clearAllData(context)
    val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    prefs.edit().putString("email", email).putString("name", name).apply()
    addresses.forEach { AddressManager.addAddress(context, it) }
    RewardsManager.generateAndSaveReferralCode(context)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Level-Up Gamer", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, singleLine = true, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email == "admin@levelupgamer.com" && password == "admin123") {
                        navController.navigate("admin") { popUpTo("login") { inclusive = true } }
                        return@Button
                    }
                    when {
                        email.isBlank() -> error = "Ingresa tu email"
                        password.length < 6 -> error = "Contraseña mínima 6 caracteres"
                        else -> {
                            error = ""
                            clearAllData(context)
                            val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
                            prefs.edit().putString("email", email).putString("name", "Usuario Registrado").apply()
                            RewardsManager.generateAndSaveReferralCode(context)
                            navController.navigate("main") { popUpTo("login") { inclusive = true } }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ingresar") }

            TextButton(onClick = { navController.navigate("register") }) {
                Text("¿No tienes cuenta? Regístrate")
            }

            Spacer(Modifier.height(24.dp))
            Text("Atajos de Desarrollo", style = MaterialTheme.typography.labelSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { navController.navigate("admin") { popUpTo("login") { inclusive = true } } }) { Text("Admin") }
                Button(onClick = {
                    setupTestUser(
                        context = context,
                        email = "cliente@ejemplo.com",
                        name = "Cliente de Prueba",
                        addresses = listOf(
                            UserAddress(street = "Av. Siempre Viva", numberOrApt = "742", commune = "Santiago", region = "Metropolitana de Santiago", isPrimary = true),
                            UserAddress(street = "Calle Falsa", numberOrApt = "123", commune = "Maipú", region = "Metropolitana de Santiago")
                        )
                    )
                    navController.navigate("main") { popUpTo("login") { inclusive = true } }
                }) { Text("Cliente") }
                Button(onClick = {
                    setupTestUser(
                        context = context,
                        email = "cliente@duocuc.cl",
                        name = "Cliente Duoc",
                        addresses = listOf(
                            UserAddress(street = "Av. Costanera", numberOrApt = "123", commune = "Lota", region = "Biobío", isPrimary = true),
                            UserAddress(street = "Plaza de Armas", numberOrApt = "456", commune = "Concepción", region = "Biobío")
                        )
                    )
                    navController.navigate("main") { popUpTo("login") { inclusive = true } }
                }) { Text("Cliente Duoc") }
            }
        }
    }
}