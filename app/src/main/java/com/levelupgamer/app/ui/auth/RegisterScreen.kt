package com.levelupgamer.app.ui.auth

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.UserAddress
import com.levelupgamer.app.ui.components.visual_transformation.DateVisualTransformation
import com.levelupgamer.app.ui.components.visual_transformation.RutVisualTransformation
import com.levelupgamer.app.util.AddressManager
import com.levelupgamer.app.util.LocationData
import com.levelupgamer.app.util.RewardsManager
import com.levelupgamer.app.util.ValidadorChileno

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var numberOrApt by remember { mutableStateOf("") }
    var selectedRegion by remember { mutableStateOf(LocationData.regionsAndCommunes.keys.first()) }
    var selectedCommune by remember { mutableStateOf(LocationData.regionsAndCommunes[selectedRegion]!!.first()) }
    var referralCode by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val context = LocalContext.current

    var regionExpanded by remember { mutableStateOf(false) }
    var communeExpanded by remember { mutableStateOf(false) }
    val communesForSelectedRegion = remember(selectedRegion) { LocationData.regionsAndCommunes[selectedRegion] ?: emptyList() }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registro", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = rut, onValueChange = { if (it.length <= 10) rut = it.filter { c -> c.isDigit() || c.uppercaseChar() == 'K' } }, label = { Text("RUT") }, visualTransformation = RutVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = birthDate, onValueChange = { if (it.length <= 8) birthDate = it.filter { c -> c.isDigit() } }, label = { Text("Fecha Nacimiento") }, visualTransformation = DateVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirmar Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = referralCode, onValueChange = { referralCode = it }, label = { Text("Código de Referido (Opcional)") }, modifier = Modifier.fillMaxWidth())

            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text("Dirección de Envío", style = MaterialTheme.typography.headlineSmall)
            
            OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = numberOrApt, onValueChange = { numberOrApt = it }, label = { Text("Número / Depto") }, modifier = Modifier.fillMaxWidth())

            ExposedDropdownMenuBox(expanded = regionExpanded, onExpandedChange = { regionExpanded = !regionExpanded }) {
                OutlinedTextField(value = selectedRegion, onValueChange = {}, readOnly = true, label = { Text("Región") }, modifier = Modifier.menuAnchor().fillMaxWidth(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) })
                ExposedDropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                    LocationData.regionsAndCommunes.keys.forEach { regionName ->
                        DropdownMenuItem(text = { Text(regionName) }, onClick = { 
                            selectedRegion = regionName
                            selectedCommune = LocationData.regionsAndCommunes[regionName]!!.first()
                            regionExpanded = false 
                        })
                    }
                }
            }
            ExposedDropdownMenuBox(expanded = communeExpanded, onExpandedChange = { communeExpanded = !communeExpanded }) {
                OutlinedTextField(value = selectedCommune, onValueChange = {}, readOnly = true, label = { Text("Comuna") }, modifier = Modifier.menuAnchor().fillMaxWidth(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = communeExpanded) })
                ExposedDropdownMenu(expanded = communeExpanded, onDismissRequest = { communeExpanded = false }) {
                    communesForSelectedRegion.forEach { communeName ->
                        DropdownMenuItem(text = { Text(communeName) }, onClick = { 
                            selectedCommune = communeName
                            communeExpanded = false 
                        })
                    }
                }
            }

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
            }

            Button(
                onClick = {
                    val formattedBirthDate = DateVisualTransformation().filter(AnnotatedString(birthDate)).text.toString()
                    val formattedRut = RutVisualTransformation().filter(AnnotatedString(rut)).text.toString()
                    when {
                        name.isBlank() || rut.isBlank() || birthDate.isBlank() || email.isBlank() || password.isBlank() || street.isBlank() || numberOrApt.isBlank() -> error = "Todos los campos son obligatorios"
                        !ValidadorChileno.validarNombre(name) -> error = "El nombre solo puede contener letras y espacios"
                        !ValidadorChileno.validarRut(formattedRut) -> error = "El RUT ingresado no es válido"
                        !ValidadorChileno.validarEdad(formattedBirthDate) -> error = "Debes tener entre 18 y 99 años para registrarte"
                        password.length < 6 -> error = "La contraseña debe tener al menos 6 caracteres"
                        password != confirmPassword -> error = "Las contraseñas no coinciden"
                        else -> {
                            val userPrefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
                            userPrefs.edit()
                                .putString("name", name)
                                .putString("rut", formattedRut)
                                .putString("birthDate", formattedBirthDate)
                                .putString("email", email)
                                .apply()

                            val firstAddress = UserAddress(street = street, numberOrApt = numberOrApt, commune = selectedCommune, region = selectedRegion, isPrimary = true)
                            AddressManager.addAddress(context, firstAddress)

                            RewardsManager.generateAndSaveReferralCode(context)
                            RewardsManager.applyReferralCode(context, referralCode)

                            navController.navigate("main") { popUpTo("login") { inclusive = true } }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Crear Cuenta")
            }
        }
    }
}