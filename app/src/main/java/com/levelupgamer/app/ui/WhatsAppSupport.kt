package com.levelupgamer.app.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Composable que lanza WhatsApp con un mensaje pre-cargado.
 * Uso: WhatsAppSupportButton()
 */
@Composable
fun WhatsAppSupportButton() {
    val context = LocalContext.current
    val numero = "+56912345678" // ← cambia al real
    val mensaje = "Hola, necesito ayuda con mi pedido en Level-Up Gamer"

    Button(onClick = { abrirWhatsApp(context, numero, mensaje) }) {
        Text("Soporte por WhatsApp")
    }
}

private fun abrirWhatsApp(context: Context, telefono: String, mensaje: String) {
    val uri = Uri.parse(
        "https://wa.me/${telefono.replace("+", "")}?text=${Uri.encode(mensaje)}"
    )
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}