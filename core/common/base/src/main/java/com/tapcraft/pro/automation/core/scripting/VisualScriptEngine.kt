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
package com.tapcraft.pro.automation.core.scripting

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.util.Log

/**
 * TapCraft Pro Visual Script Engine
 * Provides visual scripting capabilities with drag-and-drop nodes
 */
class VisualScriptEngine(
    private val context: Context
) {
    
    companion object {
        private const val TAG = "VisualScriptEngine"
    }
    
    /**
     * Data types for visual scripting
     */
    enum class DataType(val color: Int, val displayName: String) {
        EXECUTION(Color.parseColor("#FF9800"), "Execution"),
        NUMBER(Color.parseColor("#2196F3"), "Number"),
        TEXT(Color.parseColor("#4CAF50"), "Text"),
        BOOLEAN(Color.parseColor("#F44336"), "Boolean"),
        POINT(Color.parseColor("#9C27B0"), "Point"),
        IMAGE(Color.parseColor("#FF5722"), "Image")
    }
    
    /**
     * Node types for visual scripting
     */
    enum class NodeType(val color: Int, val displayName: String) {
        START(Color.parseColor("#4CAF50"), "Start"),
        CLICK(Color.parseColor("#2196F3"), "Click"),
        SWIPE(Color.parseColor("#FF9800"), "Swipe"),
        WAIT(Color.parseColor("#9C27B0"), "Wait"),
        CONDITION(Color.parseColor("#F44336"), "Condition"),
        LOOP(Color.parseColor("#607D8B"), "Loop"),
        END(Color.parseColor("#795548"), "End")
    }
    
    /**
     * Input port for a script node
     */
    data class InputPort(
        val type: DataType,
        val name: String,
        val isConnected: Boolean = false
    )
    
    /**
     * Output port for a script node
     */
    data class OutputPort(
        val type: DataType,
        val name: String
    )
    
    /**
     * Visual script node
     */
    data class ScriptNode(
        val id: String,
        val type: NodeType,
        val position: PointF,
        val inputs: List<InputPort> = emptyList(),
        val outputs: List<OutputPort> = emptyList(),
        val isSelected: Boolean = false,
        val isExecuting: Boolean = false,
        val parameters: Map<String, Any> = emptyMap()
    )
    
    /**
     * Connection between two nodes
     */
    data class NodeConnection(
        val fromNodeId: String,
        val fromOutputIndex: Int,
        val toNodeId: String,
        val toInputIndex: Int,
        val isActive: Boolean = false
    )
    
    /**
     * Visual script containing nodes and connections
     */
    data class VisualScript(
        val id: String,
        val name: String,
        val nodes: List<ScriptNode> = emptyList(),
        val connections: List<NodeConnection> = emptyList(),
        val metadata: Map<String, Any> = emptyMap()
    )
    
    private val scripts = mutableMapOf<String, VisualScript>()
    
    /**
     * Initialize the visual script engine
     */
    fun initialize() {
        Log.d(TAG, "Visual script engine initialized")
    }
    
    /**
     * Create a new script node
     */
    fun createNode(type: NodeType, position: PointF): ScriptNode {
        val nodeId = "node_${System.currentTimeMillis()}"
        
        val inputs = when (type) {
            NodeType.START -> emptyList()
            NodeType.CLICK -> listOf(
                InputPort(DataType.EXECUTION, "Execute"),
                InputPort(DataType.POINT, "Position")
            )
            NodeType.SWIPE -> listOf(
                InputPort(DataType.EXECUTION, "Execute"),
                InputPort(DataType.POINT, "Start"),
                InputPort(DataType.POINT, "End")
            )
            NodeType.WAIT -> listOf(
                InputPort(DataType.EXECUTION, "Execute"),
                InputPort(DataType.NUMBER, "Duration")
            )
            NodeType.CONDITION -> listOf(
                InputPort(DataType.EXECUTION, "Execute"),
                InputPort(DataType.BOOLEAN, "Condition")
            )
            NodeType.LOOP -> listOf(
                InputPort(DataType.EXECUTION, "Execute"),
                InputPort(DataType.NUMBER, "Count")
            )
            NodeType.END -> listOf(
                InputPort(DataType.EXECUTION, "Execute")
            )
        }
        
        val outputs = when (type) {
            NodeType.START -> listOf(
                OutputPort(DataType.EXECUTION, "Start")
            )
            NodeType.CLICK -> listOf(
                OutputPort(DataType.EXECUTION, "Complete")
            )
            NodeType.SWIPE -> listOf(
                OutputPort(DataType.EXECUTION, "Complete")
            )
            NodeType.WAIT -> listOf(
                OutputPort(DataType.EXECUTION, "Complete")
            )
            NodeType.CONDITION -> listOf(
                OutputPort(DataType.EXECUTION, "True"),
                OutputPort(DataType.EXECUTION, "False")
            )
            NodeType.LOOP -> listOf(
                OutputPort(DataType.EXECUTION, "Loop"),
                OutputPort(DataType.EXECUTION, "Complete")
            )
            NodeType.END -> emptyList()
        }
        
        return ScriptNode(
            id = nodeId,
            type = type,
            position = position,
            inputs = inputs,
            outputs = outputs
        )
    }
    
    /**
     * Create a new visual script
     */
    fun createScript(name: String): VisualScript {
        val scriptId = "script_${System.currentTimeMillis()}"
        val script = VisualScript(
            id = scriptId,
            name = name
        )
        scripts[scriptId] = script
        return script
    }
    
    /**
     * Get script by ID
     */
    fun getScript(id: String): VisualScript? {
        return scripts[id]
    }
    
    /**
     * Save script
     */
    fun saveScript(script: VisualScript) {
        scripts[script.id] = script
        Log.d(TAG, "Script saved: ${script.name}")
    }
    
    /**
     * Execute a visual script
     */
    fun executeScript(scriptId: String) {
        val script = scripts[scriptId]
        if (script != null) {
            Log.d(TAG, "Executing script: ${script.name}")
            // Implementation would execute the script nodes in order
        } else {
            Log.w(TAG, "Script not found: $scriptId")
        }
    }
    
    /**
     * Validate script connections
     */
    fun validateScript(script: VisualScript): List<String> {
        val errors = mutableListOf<String>()
        
        // Check for start node
        if (script.nodes.none { it.type == NodeType.START }) {
            errors.add("Script must have a start node")
        }
        
        // Check for end node
        if (script.nodes.none { it.type == NodeType.END }) {
            errors.add("Script must have an end node")
        }
        
        // Check for disconnected nodes
        script.nodes.forEach { node ->
            if (node.type != NodeType.START && node.inputs.none { it.isConnected }) {
                errors.add("Node ${node.id} has no input connections")
            }
        }
        
        return errors
    }
    
    /**
     * Clean up resources
     */
    fun cleanup() {
        scripts.clear()
        Log.d(TAG, "Visual script engine cleaned up")
    }
}