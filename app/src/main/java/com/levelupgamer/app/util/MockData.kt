package com.levelupgamer.app.util

import com.levelupgamer.app.model.Product

object MockData {
    // La clave es que la lista ahora sea MUTABLE
    val products = mutableListOf(
        Product(
            code = "JM001", name = "Catan", category = "Juegos de Mesa", price = 29990,
            description = "Juego de estrategia para 3-4 jugadores",
            imageUrl = "https://m.media-amazon.com/images/I/81+okt4tVpL._AC_SL1500_.jpg",
            rating = 4.0, ratingCount = 156
        ),
        Product(
            code = "AC001", name = "Control Xbox", category = "Accesorios", price = 59990,
            description = "Inalámbrico con botones mapeables",
            imageUrl = "https://m.media-amazon.com/images/I/61vG1ceJzGL._AC_SL1500_.jpg",
            rating = 4.0, ratingCount = 215
        ),
        Product(
            code = "CO001", name = "PlayStation 5", category = "Consolas", price = 549990,
            description = "Consola next-gen con SSD ultra-rápido",
            imageUrl = "https://m.media-amazon.com/images/I/71RBvxZkPFL._AC_SL1500_.jpg",
            rating = 4.0, ratingCount = 350
        ),
        Product(
            code = "CG001", name = "PC ASUS ROG", category = "Computadores", price = 1299990,
            description = "RTX 4070, 16 GB RAM, SSD 1 TB",
            imageUrl = "https://m.media-amazon.com/images/I/81V4Tnf7CCL._AC_SL1500_.jpg",
            rating = 4.5, ratingCount = 85
        ),
        Product(
            code = "SG001", name = "Silla Secretlab", category = "Sillas", price = 349990,
            description = "Ergonómica, ajustable, soporte lumbar",
            imageUrl = "https://m.media-amazon.com/images/I/61gX8Y11ttL._AC_SL1500_.jpg",
            rating = 4.8, ratingCount = 124
        )
    )
}