package com.levelupgamer.app.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.levelupgamer.app.model.Order
import com.levelupgamer.app.model.OrderStatus

object OrderManager {
    private const val KEY_ORDERS = "user_orders"
    private val gson = Gson()

    private fun getPrefs(context: Context) = context.getSharedPreferences("orders", Context.MODE_PRIVATE)

    fun getOrders(context: Context): MutableList<Order> {
        val json = getPrefs(context).getString(KEY_ORDERS, "[]")
        val type = object : TypeToken<List<Order>>() {}.type
        return gson.fromJson<List<Order>>(json, type).toMutableList()
    }

    private fun saveOrders(context: Context, orders: List<Order>) {
        val json = gson.toJson(orders)
        getPrefs(context).edit().putString(KEY_ORDERS, json).apply()
    }

    fun addOrder(context: Context, order: Order) {
        val orders = getOrders(context)
        orders.add(order)
        saveOrders(context, orders)
    }

    fun updateOrderStatus(context: Context, orderId: String, newStatus: OrderStatus) {
        val orders = getOrders(context)
        val index = orders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            orders[index] = orders[index].copy(status = newStatus)
            saveOrders(context, orders)
        }
    }
}