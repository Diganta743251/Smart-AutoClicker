/*
 * Copyright (C) 2024 Kevin Buzeau
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tapcraft.pro.automation.premium

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TapCraft Pro Premium Feature Manager
 * Manages premium features and licensing
 */
@Singleton
class PremiumFeatureManager @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val TAG = "PremiumFeatureManager"
        private const val PREFS_NAME = "tapcraft_premium"
        private const val KEY_PREMIUM_STATUS = "premium_status"
    }
    
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val premiumScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    /**
     * Check if premium features are available
     */
    fun isPremiumActive(): Boolean {
        return preferences.getBoolean(KEY_PREMIUM_STATUS, false)
    }
    
    /**
     * Enable premium features
     */
    fun enablePremium() {
        preferences.edit()
            .putBoolean(KEY_PREMIUM_STATUS, true)
            .apply()
        Log.d(TAG, "Premium features enabled")
    }
    
    /**
     * Disable premium features
     */
    fun disablePremium() {
        preferences.edit()
            .putBoolean(KEY_PREMIUM_STATUS, false)
            .apply()
        Log.d(TAG, "Premium features disabled")
    }
    
    /**
     * Initialize premium feature manager
     */
    fun initialize() {
        Log.d(TAG, "Premium feature manager initialized")
        checkPremiumStatus()
    }
    
    /**
     * Check premium status
     */
    private fun checkPremiumStatus() {
        premiumScope.launch {
            try {
                val isPremium = isPremiumActive()
                Log.d(TAG, "Premium status: $isPremium")
            } catch (e: Exception) {
                Log.e(TAG, "Error checking premium status", e)
            }
        }
    }
    
    /**
     * Get premium features list
     */
    fun getPremiumFeatures(): List<String> {
        return listOf(
            "Advanced automation scripts",
            "Cloud synchronization",
            "Team collaboration",
            "Priority support",
            "Advanced analytics"
        )
    }
    
    /**
     * Clean up resources
     */
    fun cleanup() {
        premiumScope.cancel()
        Log.d(TAG, "Premium feature manager cleaned up")
    }
}