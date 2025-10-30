package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.UserAddress
import com.levelupgamer.app.util.AddressManager

@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    var addresses by remember { mutableStateOf(AddressManager.getAddresses(context)) }

    // Campos para cambiar contraseña
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text("Mis Direcciones", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        addresses.forEach { address ->
            AddressItem(address = address, onEdit = {
                navController.navigate("address/${address.id}")
            }, onDelete = {
                AddressManager.deleteAddress(context, address.id)
                addresses = AddressManager.getAddresses(context) // Refresh list
            })
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate("address") }) {
            Text("Añadir Nueva Dirección")
        }

        Divider(modifier = Modifier.padding(vertical = 24.dp))

        Text("Cambiar Contraseña", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = currentPassword, onValueChange = { currentPassword = it }, label = { Text("Contraseña Actual") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("Nueva Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = confirmNewPassword, onValueChange = { confirmNewPassword = it }, label = { Text("Confirmar Nueva Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        
        if (message.isNotEmpty()) {
            Text(message, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp))
        }

        Button(onClick = {
             if (newPassword.isNotEmpty()) {
                if (newPassword.length < 6) {
                    message = "La nueva contraseña debe tener al menos 6 caracteres"
                } else if (newPassword != confirmNewPassword) {
                    message = "Las nuevas contraseñas no coinciden"
                } else {
                    message = "Contraseña actualizada con éxito"
                }
            } else {
                 message = ""
            }
        }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Actualizar Contraseña")
        }
    }
}

@Composable
fun AddressItem(address: UserAddress, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${address.street} ${address.numberOrApt}", fontWeight = FontWeight.Bold)
                Text("${address.commune}, ${address.region}")
                if (address.isPrimary) {
                    Text("(Principal)", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
        }
    }
}