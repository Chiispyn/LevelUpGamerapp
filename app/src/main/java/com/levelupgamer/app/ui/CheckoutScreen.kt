package com.levelupgamer.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.levelupgamer.app.model.Order
import com.levelupgamer.app.model.Product
import com.levelupgamer.app.model.Reward
import com.levelupgamer.app.model.UserAddress
import com.levelupgamer.app.util.AddressManager
import com.levelupgamer.app.util.CartManager
import com.levelupgamer.app.util.OrderManager
import com.levelupgamer.app.util.RewardsManager
import com.levelupgamer.app.util.ShippingManager

@Composable
fun CheckoutScreen(navController: NavController, cartItems: List<Product>, onCartCleared: () -> Unit) {
    val context = LocalContext.current
    val addresses by remember(navController.currentBackStackEntry) { mutableStateOf(AddressManager.getAddresses(context)) }
    val activeRewards by remember { mutableStateOf(RewardsManager.getActiveRewards(context)) }

    var selectedAddress by remember { mutableStateOf<UserAddress?>(addresses.find { it.isPrimary }) }
    var selectedPaymentMethod by remember { mutableStateOf<String?>(null) }
    var selectedReward by remember { mutableStateOf<Reward?>(null) }

    val subtotal = remember(cartItems) { cartItems.sumOf { it.price * (it.quantity ?: 1) } }
    val shippingInfo = selectedAddress?.let { ShippingManager.getShippingInfo(it.region) }
    
    val isFreeShipping = selectedReward?.id == "s-free"
    val shippingCost = if (isFreeShipping) 0 else shippingInfo?.cost ?: 0
    
    val discountAmount = when {
        selectedReward?.id?.startsWith("d-") == true -> selectedReward!!.pointsCost
        else -> (subtotal * RewardsManager.getAutomaticDiscount(context) / 100)
    }
    val discountPercentage = if (selectedReward == null) RewardsManager.getAutomaticDiscount(context) else 0

    val total = subtotal - discountAmount + shippingCost

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {
        Text("Finalizar Compra", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        Text("1. Selecciona una dirección de envío", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
            items(addresses, key = { it.id }) { address ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { selectedAddress = address }.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selectedAddress?.id == address.id, onClick = { selectedAddress = address })
                    Column {
                        Text("${address.street} ${address.numberOrApt}", fontWeight = FontWeight.Bold)
                        Text("${address.commune}, ${address.region}")
                    }
                }
            }
        }
        Button(onClick = { navController.navigate("address") }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Añadir Nueva Dirección")
        }

        if (shippingInfo != null && selectedAddress != null) {
            Text("Costo de Envío: $${shippingInfo.cost} (Estimado: ${shippingInfo.estimatedDays})", style = MaterialTheme.typography.bodyMedium)
        }

        if (activeRewards.isNotEmpty()) {
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text("2. Aplica una recompensa (Opcional)", style = MaterialTheme.typography.titleMedium)
            activeRewards.forEach { reward ->
                Row(modifier = Modifier.clickable { selectedReward = if (selectedReward == reward) null else reward }.padding(vertical = 4.dp)) {
                    RadioButton(selected = selectedReward?.id == reward.id, onClick = { selectedReward = if (selectedReward == reward) null else reward })
                    Text(reward.title)
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Text("3. Selecciona un método de pago", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { selectedPaymentMethod = "WebPay" }) { Text("WebPay") }
            Button(onClick = { selectedPaymentMethod = "MercadoPago" }) { Text("MercadoPago") }
        }

        Spacer(Modifier.weight(1f))

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Text("Subtotal: $$subtotal")
            Text("Envío: $$shippingCost")
            if (discountAmount > 0) {
                val discountLabel = if (selectedReward != null && selectedReward?.id != "s-free") selectedReward!!.title else "Descuento Nivel/Duoc ($discountPercentage%)"
                if(discountLabel.contains("Descuento")) Text("$discountLabel: -$${discountAmount}", color = MaterialTheme.colorScheme.primary)
            }
            Text("Total: $$total", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val userEmail = context.getSharedPreferences("user", 0).getString("email", "") ?: ""
                val newOrder = Order(
                    products = cartItems,
                    subtotal = subtotal,
                    shippingCost = shippingCost,
                    total = total,
                    userEmail = userEmail,
                    shippingAddress = selectedAddress!!,
                )
                OrderManager.addOrder(context, newOrder)

                selectedReward?.let { RewardsManager.useReward(context, it) }

                val pointsEarned = (subtotal / 1000) * 10
                RewardsManager.addPoints(context, pointsEarned)
                
                onCartCleared()
                navController.navigate("orderConfirmation/${newOrder.id}/$pointsEarned") { 
                    popUpTo(navController.graph.startDestinationId) { inclusive = true } 
                }
            },
            enabled = selectedAddress != null && selectedPaymentMethod != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pagar y Finalizar Pedido")
        }
    }
}