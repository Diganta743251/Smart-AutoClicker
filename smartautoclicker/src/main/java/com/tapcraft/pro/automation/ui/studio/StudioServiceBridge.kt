/*
 * Copyright (C) 2025 TapCraft Pro
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
package com.tapcraft.pro.automation.ui.studio

import android.content.Context
import android.content.Intent
import android.util.Log
import com.tapcraft.pro.automation.SmartAutoClickerService


/**
 * Bridge between TapCraft Studio and SmartAutoClicker service
 */
class StudioServiceBridge(
    private val context: Context
) {

    companion object {
        private const val TAG = "StudioServiceBridge"
    }

    interface ServiceConnectionListener {
        fun onServiceConnected(service: SmartAutoClickerService)
        fun onServiceDisconnected()
    }

    private var connectionListener: ServiceConnectionListener? = null

    fun setConnectionListener(listener: ServiceConnectionListener) {
        this.connectionListener = listener
    }

    fun bindToService(): Boolean {
        // Since the service doesn't support binding, we'll just check if it's running
        Log.d(TAG, "Checking service status...")
        return true
    }

    fun unbindFromService() {
        // No-op since we don't actually bind
        Log.d(TAG, "Service unbind requested")
    }

    fun isServiceConnected(): Boolean {
        // Check if accessibility service is enabled
        return isAccessibilityServiceEnabled()
    }

    fun startService() {
        val intent = Intent(context, SmartAutoClickerService::class.java)
        try {
            context.startForegroundService(intent)
            Log.d(TAG, "Service start requested")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting service", e)
        }
    }

    fun stopService() {
        val intent = Intent(context, SmartAutoClickerService::class.java)
        try {
            context.stopService(intent)
            Log.d(TAG, "Service stop requested")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping service", e)
        }
    }

    /**
     * Check if accessibility service is enabled
     */
    fun isAccessibilityServiceEnabled(): Boolean {
        // Check if the accessibility service is enabled by checking system settings
        return try {
            val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as android.view.accessibility.AccessibilityManager
            val enabledServices = android.provider.Settings.Secure.getString(
                context.contentResolver,
                android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            enabledServices?.contains(context.packageName) == true
        } catch (e: Exception) {
            Log.e(TAG, "Error checking accessibility service status", e)
            false
        }
    }

    /**
     * Request accessibility service permissions
     */
    fun requestAccessibilityPermission() {
        val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening accessibility settings", e)
        }
    }

    fun cleanup() {
        connectionListener = null
    }
}