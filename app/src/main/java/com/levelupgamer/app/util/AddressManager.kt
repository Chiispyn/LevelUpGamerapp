package com.levelupgamer.app.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.levelupgamer.app.model.UserAddress

object AddressManager {
    private const val KEY_ADDRESSES = "user_addresses"
    private val gson = Gson()

    private fun getPrefs(context: Context) = context.getSharedPreferences("addresses", Context.MODE_PRIVATE)

    fun getAddresses(context: Context): MutableList<UserAddress> {
        val json = getPrefs(context).getString(KEY_ADDRESSES, "[]")
        val type = object : TypeToken<List<UserAddress>>() {}.type
        return gson.fromJson<List<UserAddress>>(json, type).toMutableList()
    }

    private fun saveAddresses(context: Context, addresses: List<UserAddress>) {
        val json = gson.toJson(addresses)
        getPrefs(context).edit().putString(KEY_ADDRESSES, json).apply()
    }

    fun addAddress(context: Context, address: UserAddress) {
        val addresses = getAddresses(context)
        addresses.add(address)
        saveAddresses(context, addresses)
    }

    fun updateAddress(context: Context, address: UserAddress) {
        val addresses = getAddresses(context)
        val index = addresses.indexOfFirst { it.id == address.id }
        if (index != -1) {
            addresses[index] = address
            saveAddresses(context, addresses)
        }
    }

    fun deleteAddress(context: Context, addressId: String) {
        val addresses = getAddresses(context)
        addresses.removeAll { it.id == addressId }
        saveAddresses(context, addresses)
    }
    
    fun getAddress(context: Context, addressId: String): UserAddress? {
        return getAddresses(context).find { it.id == addressId }
    }
}