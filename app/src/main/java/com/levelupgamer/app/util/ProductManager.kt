package com.levelupgamer.app.util

import com.levelupgamer.app.model.Product

// Ahora este Manager opera directamente sobre la lista mutable de MockData,
// convirtiéndola en la "única fuente de la verdad".
object ProductManager {

    fun getProducts(): List<Product> {
        return MockData.products
    }

    fun getProduct(code: String): Product? {
        return MockData.products.find { it.code == code }
    }

    fun addProduct(product: Product) {
        if (MockData.products.none { it.code == product.code }) {
            MockData.products.add(product)
        }
    }

    fun updateProduct(updatedProduct: Product) {
        val index = MockData.products.indexOfFirst { it.code == updatedProduct.code }
        if (index != -1) {
            MockData.products[index] = updatedProduct
        }
    }

    fun deleteProduct(code: String) {
        MockData.products.removeAll { it.code == code }
    }
}