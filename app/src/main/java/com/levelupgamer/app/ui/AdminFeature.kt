package com.levelupgamer.app.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.ui.theme.GreenLime
import com.levelupgamer.app.util.ProductManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFeature(mainNavController: NavHostController) {
    val adminNavController = rememberNavController()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(getAdminScreenTitle(adminNavController.currentBackStackEntryAsState().value?.destination?.route)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = GreenLime
                ),
                navigationIcon = {
                    if (adminNavController.previousBackStackEntry != null) {
                        IconButton(onClick = { adminNavController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(navController = adminNavController, startDestination = "adminHome", modifier = Modifier.padding(innerPadding)) {
            composable("adminHome") { AdminHomeScreen(adminNavController, mainNavController) }
            composable("adminProducts") { AdminProductsScreen(adminNavController) }
            composable("adminEvents") { AdminEventsScreen(navController = adminNavController) }
            composable(
                route = "productEdit?productCode={productCode}",
                arguments = listOf(navArgument("productCode") { type = NavType.StringType; nullable = true })
            ) { backStackEntry ->
                val productCode = backStackEntry.arguments?.getString("productCode")
                ProductEditScreen(adminNavController, productCode)
            }
            composable(
                route = "eventEdit?eventId={eventId}",
                arguments = listOf(navArgument("eventId") { type = NavType.StringType; nullable = true })
            ) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId")
                EventEditScreen(adminNavController, eventId)
            }
        }
    }
}

@Composable
private fun AdminHomeScreen(navController: NavController, mainNavController: NavHostController) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Panel de Administración", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = { navController.navigate("adminProducts") }, modifier = Modifier.fillMaxWidth()) {
            Text("Gestionar Productos")
        }
        Button(onClick = { navController.navigate("adminEvents") }, modifier = Modifier.fillMaxWidth()) {
            Text("Gestionar Eventos")
        }
        Spacer(Modifier.weight(1f))
        Button(onClick = {
            context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().apply()
            mainNavController.navigate("login") { popUpTo(mainNavController.graph.id) { inclusive = true } }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Cerrar Sesión")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminProductsScreen(navController: NavController) {
    var products by remember(navController.currentBackStackEntry) { mutableStateOf(ProductManager.getProducts()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("productEdit") }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products, key = { it.code }) { product ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.name, fontWeight = FontWeight.Bold)
                            Text("Stock: ${product.quantity}")
                        }
                        IconButton(onClick = { navController.navigate("productEdit?productCode=${product.code}") }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = {
                            ProductManager.deleteProduct(product.code)
                            products = ProductManager.getProducts()
                        }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductEditScreen(navController: NavController, productCode: String?) {
    val isEditing = productCode != null
    val productToEdit = remember { productCode?.let { ProductManager.getProduct(it) } }

    var code by remember { mutableStateOf(productToEdit?.code ?: "") }
    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var category by remember { mutableStateOf(productToEdit?.category ?: "") }
    var price by remember { mutableStateOf(productToEdit?.price?.toString() ?: "") }
    var quantity by remember { mutableStateOf(productToEdit?.quantity?.toString() ?: "") }
    var description by remember { mutableStateOf(productToEdit?.description ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(productToEdit?.imageUrl?.let { Uri.parse(it) }) }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? -> imageUri = uri }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text(if (isEditing) "Editar Producto" else "Añadir Producto", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        if (imageUri != null) {
            AsyncImage(model = imageUri, contentDescription = "Vista previa", modifier = Modifier.height(200.dp).fillMaxWidth(), contentScale = ContentScale.Crop)
        } else {
            Text("No se ha seleccionado ninguna imagen.")
        }
        Button(onClick = { imagePickerLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text("Seleccionar Imagen")
        }

        OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Código (SKU)") }, enabled = !isEditing, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Stock") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        
        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            val product = Product(
                code = if (isEditing) productToEdit!!.code else code,
                name = name, category = category, price = price.toIntOrNull() ?: 0, 
                quantity = quantity.toIntOrNull() ?: 0, description = description, 
                imageUrl = imageUri?.toString() ?: ""
            )
            if (isEditing) ProductManager.updateProduct(product) else ProductManager.addProduct(product)
            navController.popBackStack()
        }, modifier = Modifier.fillMaxWidth()) {
            Text(if (isEditing) "Guardar Cambios" else "Añadir Producto")
        }
    }
}

private fun getAdminScreenTitle(route: String?): String {
    return when {
        route == "adminHome" -> "Panel de Admin"
        route == "adminProducts" -> "Gestionar Productos"
        route?.startsWith("productEdit") == true -> "Editar Producto"
        route == "adminEvents" -> "Gestionar Eventos"
        route?.startsWith("eventEdit") == true -> "Editar Evento"
        else -> "Admin"
    }
}