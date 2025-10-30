package com.levelupgamer.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.levelupgamer.app.util.LocationData // <-- Cambio clave: usamos el nuevo archivo de datos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionSelector(region: String, onRegionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = region,
            onValueChange = {},
            readOnly = true,
            label = { Text("Región") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            // Obtenemos las regiones del nuevo mapa de datos
            LocationData.regionsAndCommunes.keys.forEach { regionName ->
                DropdownMenuItem(text = { Text(regionName) }, onClick = {
                    onRegionSelected(regionName)
                    expanded = false
                })
            }
        }
    }
}
