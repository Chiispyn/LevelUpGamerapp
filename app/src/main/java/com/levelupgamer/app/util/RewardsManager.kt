package com.levelupgamer.app.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.levelupgamer.app.model.Reward
import com.levelupgamer.app.model.UserLevel

object RewardsManager {
    private const val KEY_USER_POINTS = "user_points"
    private const val KEY_REFERRAL_CODE = "user_referral_code"
    private const val KEY_ACTIVE_REWARDS = "active_rewards"
    private val gson = Gson()

    private fun getPrefs(context: Context) = context.getSharedPreferences("rewards", Context.MODE_PRIVATE)
    private fun getUserPrefs(context: Context) = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    // --- Gestión de Puntos ---
    fun getUserPoints(context: Context): Int = getPrefs(context).getInt(KEY_USER_POINTS, 0)
    fun addPoints(context: Context, pointsToAdd: Int) {
        val currentPoints = getUserPoints(context)
        getPrefs(context).edit().putInt(KEY_USER_POINTS, currentPoints + pointsToAdd).apply()
    }

    // --- Gestión de Recompensas Activas (Cupones) ---
    fun getActiveRewards(context: Context): MutableList<Reward> {
        val json = getPrefs(context).getString(KEY_ACTIVE_REWARDS, "[]")
        val type = object : TypeToken<List<Reward>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveActiveRewards(context: Context, rewards: List<Reward>) {
        getPrefs(context).edit().putString(KEY_ACTIVE_REWARDS, gson.toJson(rewards)).apply()
    }

    fun redeemReward(context: Context, reward: Reward): Boolean {
        val currentPoints = getUserPoints(context)
        if (currentPoints < reward.pointsCost) return false

        // Restar puntos y añadir la recompensa a la lista de activas
        getPrefs(context).edit().putInt(KEY_USER_POINTS, currentPoints - reward.pointsCost).apply()
        val activeRewards = getActiveRewards(context)
        activeRewards.add(reward)
        saveActiveRewards(context, activeRewards)
        return true
    }

    fun useReward(context: Context, reward: Reward) {
        val activeRewards = getActiveRewards(context)
        activeRewards.removeAll { it.id == reward.id }
        saveActiveRewards(context, activeRewards)
    }

    // --- Lógica Principal ---
    fun addPointsForPurchase(context: Context, purchaseTotal: Int) {
        val points = (purchaseTotal / 1000) * 10
        addPoints(context, points)
    }

    fun generateAndSaveReferralCode(context: Context) {
        val name = getUserPrefs(context).getString("name", "user")?.take(4)?.uppercase() ?: "USER"
        val randomSuffix = (100..999).random()
        val referralCode = "$name$randomSuffix"
        getPrefs(context).edit().putString(KEY_REFERRAL_CODE, referralCode).apply()
    }

    fun getUserReferralCode(context: Context): String = getPrefs(context).getString(KEY_REFERRAL_CODE, "") ?: ""

    fun applyReferralCode(context: Context, code: String) {
        if (code.isNotBlank()) addPoints(context, 100)
    }

    fun getUserLevel(context: Context): UserLevel {
        val points = getUserPoints(context)
        return when {
            points >= UserLevel.VIP.requiredPoints -> UserLevel.VIP
            points >= UserLevel.GOLD.requiredPoints -> UserLevel.GOLD
            points >= UserLevel.SILVER.requiredPoints -> UserLevel.SILVER
            else -> UserLevel.BRONZE
        }
    }

    fun getAutomaticDiscount(context: Context): Int {
        val email = getUserPrefs(context).getString("email", "") ?: ""
        val duocDiscount = if (email.endsWith("@duocuc.cl")) 20 else 0
        val levelDiscount = getUserLevel(context).discountPercentage
        return maxOf(duocDiscount, levelDiscount)
    }
}