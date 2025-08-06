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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for TapCraft Studio
 */
@HiltViewModel
class StudioViewModel @Inject constructor() : ViewModel() {

    private val _zoomLevel = MutableStateFlow(1.0f)
    val zoomLevel: StateFlow<Float> = _zoomLevel.asStateFlow()

    private val _validationErrors = MutableStateFlow<List<String>>(emptyList())
    val validationErrors: StateFlow<List<String>> = _validationErrors.asStateFlow()

    private val _scriptName = MutableStateFlow("Untitled Script")
    val scriptName: StateFlow<String> = _scriptName.asStateFlow()

    private val _scriptDescription = MutableStateFlow("No description")
    val scriptDescription: StateFlow<String> = _scriptDescription.asStateFlow()

    private val _isScriptValid = MutableStateFlow(true)
    val isScriptValid: StateFlow<Boolean> = _isScriptValid.asStateFlow()

    // Undo/Redo system
    private val undoStack = mutableListOf<ScriptState>()
    private val redoStack = mutableListOf<ScriptState>()
    private var currentState = ScriptState()

    data class ScriptState(
        val nodes: List<String> = emptyList(),
        val connections: List<String> = emptyList(),
        val timestamp: Long = System.currentTimeMillis()
    )

    fun zoomIn() {
        val newZoom = (_zoomLevel.value * 1.2f).coerceAtMost(3.0f)
        _zoomLevel.value = newZoom
    }

    fun zoomOut() {
        val newZoom = (_zoomLevel.value / 1.2f).coerceAtLeast(0.1f)
        _zoomLevel.value = newZoom
    }

    fun resetZoom() {
        _zoomLevel.value = 1.0f
    }

    fun validateScript() {
        viewModelScope.launch {
            val errors = mutableListOf<String>()
            
            // Basic validation rules
            if (currentState.nodes.isEmpty()) {
                errors.add("Script must contain at least one node")
            }
            
            if (currentState.nodes.size == 1 && !currentState.nodes.contains("start")) {
                errors.add("Script must have a start node")
            }
            
            // Check for disconnected nodes
            val connectedNodes = currentState.connections.flatMap { 
                listOf(it.substringBefore("->"), it.substringAfter("->"))
            }.toSet()
            
            val disconnectedNodes = currentState.nodes.filter { it !in connectedNodes && it != "start" }
            if (disconnectedNodes.isNotEmpty()) {
                errors.add("Disconnected nodes found: ${disconnectedNodes.joinToString(", ")}")
            }
            
            _validationErrors.value = errors
            _isScriptValid.value = errors.isEmpty()
        }
    }

    fun saveState() {
        undoStack.add(currentState.copy())
        redoStack.clear()
        
        // Limit undo stack size
        if (undoStack.size > 50) {
            undoStack.removeAt(0)
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.add(currentState.copy())
            currentState = undoStack.removeAt(undoStack.size - 1)
            validateScript()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.add(currentState.copy())
            currentState = redoStack.removeAt(redoStack.size - 1)
            validateScript()
        }
    }

    fun updateScriptName(name: String) {
        _scriptName.value = name
    }

    fun updateScriptDescription(description: String) {
        _scriptDescription.value = description
    }

    fun addNode(nodeId: String) {
        saveState()
        currentState = currentState.copy(
            nodes = currentState.nodes + nodeId
        )
        validateScript()
    }

    fun removeNode(nodeId: String) {
        saveState()
        currentState = currentState.copy(
            nodes = currentState.nodes - nodeId,
            connections = currentState.connections.filter { 
                !it.contains(nodeId)
            }
        )
        validateScript()
    }

    fun addConnection(fromNode: String, toNode: String) {
        saveState()
        val connectionId = "$fromNode->$toNode"
        currentState = currentState.copy(
            connections = currentState.connections + connectionId
        )
        validateScript()
    }

    fun removeConnection(fromNode: String, toNode: String) {
        saveState()
        val connectionId = "$fromNode->$toNode"
        currentState = currentState.copy(
            connections = currentState.connections - connectionId
        )
        validateScript()
    }

    fun canUndo(): Boolean = undoStack.isNotEmpty()
    fun canRedo(): Boolean = redoStack.isNotEmpty()

    // Node property management
    private val nodeProperties = mutableMapOf<String, MutableMap<String, Any>>()

    fun updateNodeProperty(nodeId: String, propertyName: String, value: Any) {
        saveState()
        
        val properties = nodeProperties.getOrPut(nodeId) { mutableMapOf() }
        properties[propertyName] = value
        
        validateScript()
    }

    fun getNodeProperty(nodeId: String, propertyName: String): Any? {
        return nodeProperties[nodeId]?.get(propertyName)
    }

    fun getNodeProperties(nodeId: String): Map<String, Any> {
        return nodeProperties[nodeId] ?: emptyMap()
    }

    fun clearNodeProperties(nodeId: String) {
        nodeProperties.remove(nodeId)
    }

    fun getAllNodeProperties(): Map<String, Map<String, Any>> {
        return nodeProperties.mapValues { it.value.toMap() }
    }
}