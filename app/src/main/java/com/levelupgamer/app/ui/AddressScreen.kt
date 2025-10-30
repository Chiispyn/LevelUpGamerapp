package com.levelupgamer.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.UserAddress
import com.levelupgamer.app.util.AddressManager
import com.levelupgamer.app.util.LocationData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(navController: NavController, addressId: String? = null) {
    val context = LocalContext.current
    val isEditing = addressId != null
    val addressToEdit = remember { if (isEditing) AddressManager.getAddress(context, addressId!!) else null }

    var street by remember { mutableStateOf(addressToEdit?.street ?: "") }
    var numberOrApt by remember { mutableStateOf(addressToEdit?.numberOrApt ?: "") }
    var selectedRegion by remember { mutableStateOf(addressToEdit?.region ?: LocationData.regionsAndCommunes.keys.first()) }
    var selectedCommune by remember { mutableStateOf(addressToEdit?.commune ?: LocationData.regionsAndCommunes[selectedRegion]!!.first()) }
    var isPrimary by remember { mutableStateOf(addressToEdit?.isPrimary ?: false) }
    val communesForSelectedRegion = remember(selectedRegion) {
        LocationData.regionsAndCommunes[selectedRegion] ?: emptyList()
    }

    var regionExpanded by remember { mutableStateOf(false) }
    var communeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (isEditing) "Editar Dirección" else "Añadir Dirección", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = numberOrApt, onValueChange = { numberOrApt = it }, label = { Text("Número / Depto") }, modifier = Modifier.fillMaxWidth())
        
        // Selector de Región
        ExposedDropdownMenuBox(expanded = regionExpanded, onExpandedChange = { regionExpanded = !regionExpanded }) {
            OutlinedTextField(value = selectedRegion, onValueChange = {}, readOnly = true, label = { Text("Región") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                LocationData.regionsAndCommunes.keys.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        selectedRegion = it
                        selectedCommune = LocationData.regionsAndCommunes[it]!!.first()
                        regionExpanded = false
                    })
                }
            }
        }

        // Selector de Comuna
        ExposedDropdownMenuBox(expanded = communeExpanded, onExpandedChange = { communeExpanded = !communeExpanded }) {
            OutlinedTextField(value = selectedCommune, onValueChange = {}, readOnly = true, label = { Text("Comuna") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = communeExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = communeExpanded, onDismissRequest = { communeExpanded = false }) {
                communesForSelectedRegion.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = {
                        selectedCommune = it
                        communeExpanded = false
                    })
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isPrimary, onCheckedChange = { isPrimary = it })
            Text("Marcar como dirección principal")
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val newAddress = UserAddress(
                    id = addressToEdit?.id ?: "",
                    street = street,
                    numberOrApt = numberOrApt,
                    commune = selectedCommune,
                    region = selectedRegion,
                    isPrimary = isPrimary
                )
                if (isEditing) {
                    AddressManager.updateAddress(context, newAddress)
                } else {
                    AddressManager.addAddress(context, newAddress)
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditing) "Guardar Cambios" else "Añadir Dirección")
        }
    }
}