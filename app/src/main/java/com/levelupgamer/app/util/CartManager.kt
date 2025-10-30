package com.levelupgamer.app.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.levelupgamer.app.model.Product

object CartManager {
    private const val KEY_CART = "cart_items"
    private val gson = Gson()

    private fun getPrefs(context: Context) = context.getSharedPreferences("cart", Context.MODE_PRIVATE)

    fun getItems(context: Context): MutableList<Product> {
        val json = getPrefs(context).getString(KEY_CART, "[]")
        val type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson<List<Product>>(json, type).toMutableList()
    }

    private fun save(context: Context, list: List<Product>) {
        getPrefs(context).edit().putString(KEY_CART, gson.toJson(list)).apply()
    }

    fun add(context: Context, product: Product): List<Product> {
        val current = getItems(context)
        val idx = current.indexOfFirst { it.code == product.code }
        if (idx == -1) {
            current.add(product.copy(quantity = 1))
        } else {
            val newQty = (current[idx].quantity ?: 1) + 1
            current[idx] = current[idx].copy(quantity = newQty)
        }
        save(context, current)
        return current
    }

    fun remove(context: Context, product: Product): List<Product> {
        val current = getItems(context)
        current.removeAll { it.code == product.code }
        save(context, current)
        return current
    }

    fun changeQty(context: Context, product: Product, delta: Int): List<Product> {
        val list = getItems(context)
        val idx = list.indexOfFirst { it.code == product.code }
        if (idx == -1) return list
        val newQty = (list[idx].quantity ?: 1) + delta
        if (newQty <= 0) {
            list.removeAt(idx)
        } else {
            list[idx] = list[idx].copy(quantity = newQty)
        }
        save(context, list)
        return list
    }

    fun clear(context: Context): List<Product> {
        val emptyList = mutableListOf<Product>()
        save(context, emptyList)
        return emptyList
    }

    fun getTotal(context: Context, items: List<Product>): Int {
        val total = items.sumOf { it.price * (it.quantity ?: 1) }
        // --- CORRECCIÓN: Usar el nombre de función correcto ---
        val discount = RewardsManager.getAutomaticDiscount(context)
        return total - (total * discount / 100)
    }
}