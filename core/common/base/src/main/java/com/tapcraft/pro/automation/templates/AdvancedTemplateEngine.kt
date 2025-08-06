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
package com.tapcraft.pro.automation.templates

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TapCraft Pro Advanced Template Engine
 * Provides advanced template management and processing capabilities
 */
@Singleton
class AdvancedTemplateEngine @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val TAG = "AdvancedTemplateEngine"
    }
    
    private val templateScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val json = Json { 
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    @Serializable
    data class AutomationTemplate(
        val id: String,
        val name: String,
        val description: String,
        val category: String,
        val actions: List<TemplateAction> = emptyList(),
        val variables: Map<String, String> = emptyMap(),
        val metadata: TemplateMetadata = TemplateMetadata()
    )
    
    @Serializable
    data class TemplateAction(
        val type: String,
        val parameters: Map<String, String> = emptyMap(),
        val delay: Long = 0L
    )
    
    @Serializable
    data class TemplateMetadata(
        val version: String = "1.0",
        val author: String = "TapCraft Pro",
        val createdAt: Long = System.currentTimeMillis(),
        val tags: List<String> = emptyList()
    )
    
    private val templates = mutableMapOf<String, AutomationTemplate>()
    
    /**
     * Initialize template engine
     */
    fun initialize() {
        Log.d(TAG, "Advanced template engine initialized")
        loadDefaultTemplates()
    }
    
    /**
     * Load default templates
     */
    private fun loadDefaultTemplates() {
        templateScope.launch {
            try {
                val defaultTemplates = createDefaultTemplates()
                defaultTemplates.forEach { template ->
                    templates[template.id] = template
                }
                Log.d(TAG, "Loaded ${defaultTemplates.size} default templates")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading default templates", e)
            }
        }
    }
    
    /**
     * Create default templates
     */
    private fun createDefaultTemplates(): List<AutomationTemplate> {
        return listOf(
            AutomationTemplate(
                id = "basic_click",
                name = "Basic Click Template",
                description = "Simple click automation template",
                category = "Basic",
                actions = listOf(
                    TemplateAction(
                        type = "click",
                        parameters = mapOf("x" to "100", "y" to "200"),
                        delay = 1000L
                    )
                )
            ),
            AutomationTemplate(
                id = "swipe_gesture",
                name = "Swipe Gesture Template",
                description = "Swipe automation template",
                category = "Gestures",
                actions = listOf(
                    TemplateAction(
                        type = "swipe",
                        parameters = mapOf(
                            "startX" to "100",
                            "startY" to "200",
                            "endX" to "300",
                            "endY" to "400"
                        ),
                        delay = 500L
                    )
                )
            )
        )
    }
    
    /**
     * Get all templates
     */
    fun getAllTemplates(): List<AutomationTemplate> {
        return templates.values.toList()
    }
    
    /**
     * Get template by ID
     */
    fun getTemplate(id: String): AutomationTemplate? {
        return templates[id]
    }
    
    /**
     * Get templates by category
     */
    fun getTemplatesByCategory(category: String): List<AutomationTemplate> {
        return templates.values.filter { it.category == category }
    }
    
    /**
     * Save template
     */
    fun saveTemplate(template: AutomationTemplate) {
        templateScope.launch {
            try {
                templates[template.id] = template
                Log.d(TAG, "Template saved: ${template.name}")
            } catch (e: Exception) {
                Log.e(TAG, "Error saving template", e)
            }
        }
    }
    
    /**
     * Delete template
     */
    fun deleteTemplate(id: String): Boolean {
        return templates.remove(id) != null
    }
    
    /**
     * Export template to JSON
     */
    fun exportTemplate(template: AutomationTemplate): String {
        return try {
            json.encodeToString(AutomationTemplate.serializer(), template)
        } catch (e: Exception) {
            Log.e(TAG, "Error exporting template", e)
            ""
        }
    }
    
    /**
     * Import template from JSON
     */
    fun importTemplate(jsonString: String): AutomationTemplate? {
        return try {
            json.decodeFromString(AutomationTemplate.serializer(), jsonString)
        } catch (e: Exception) {
            Log.e(TAG, "Error importing template", e)
            null
        }
    }
    
    /**
     * Search templates
     */
    fun searchTemplates(query: String): List<AutomationTemplate> {
        return templates.values.filter { template ->
            template.name.contains(query, ignoreCase = true) ||
            template.description.contains(query, ignoreCase = true) ||
            template.category.contains(query, ignoreCase = true)
        }
    }
    
    /**
     * Clean up resources
     */
    fun cleanup() {
        templateScope.cancel()
        templates.clear()
        Log.d(TAG, "Advanced template engine cleaned up")
    }
}