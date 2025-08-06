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

import android.graphics.PointF
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tapcraft.pro.automation.R
import com.tapcraft.pro.automation.databinding.ActivityStudioBinding
import com.tapcraft.pro.automation.ui.components.VisualScriptCanvas
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * TapCraft Studio - Visual scripting interface for creating automation workflows
 */
@AndroidEntryPoint
class StudioActivity : AppCompatActivity(), VisualScriptCanvas.OnNodeInteractionListener, NodePropertyEditor.PropertyChangeListener, ScriptExecutionEngine.ExecutionListener, StudioServiceBridge.ServiceConnectionListener {

    private lateinit var binding: ActivityStudioBinding
    private val viewModel: StudioViewModel by viewModels()
    private lateinit var propertyEditor: NodePropertyEditor
    private lateinit var executionEngine: ScriptExecutionEngine
    private lateinit var serviceBridge: StudioServiceBridge
    
    private var nodeCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        binding = ActivityStudioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupCanvas()
        setupNodePalette()
        setupCanvasControls()
        setupPropertyEditor()
        setupExecutionEngine()
        setupServiceBridge()
        setupPlaybackControls()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "TapCraft Studio"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupCanvas() {
        binding.scriptCanvas.nodeInteractionListener = this
        
        // Add a sample start node
        val startNode = VisualScriptCanvas.SimpleNode(
            id = "start",
            title = "Start",
            position = PointF(100f, 100f),
            color = android.graphics.Color.parseColor("#FF4CAF50")
        )
        binding.scriptCanvas.addNode(startNode)
    }

    private fun setupNodePalette() {
        // Action nodes
        binding.addTapNodeButton.setOnClickListener {
            addNode("Tap", android.graphics.Color.parseColor("#FF4CAF50"))
        }
        
        binding.addSwipeNodeButton.setOnClickListener {
            addNode("Swipe", android.graphics.Color.parseColor("#FF4CAF50"))
        }
        
        binding.addWaitNodeButton.setOnClickListener {
            addNode("Wait", android.graphics.Color.parseColor("#FF4CAF50"))
        }
        
        // Logic nodes
        binding.addConditionNodeButton.setOnClickListener {
            addNode("If/Else", android.graphics.Color.parseColor("#FFFF5722"))
        }
        
        binding.addLoopNodeButton.setOnClickListener {
            addNode("Loop", android.graphics.Color.parseColor("#FFFF5722"))
        }
    }

    private fun setupCanvasControls() {
        binding.zoomInButton.setOnClickListener {
            viewModel.zoomIn()
        }
        
        binding.zoomOutButton.setOnClickListener {
            viewModel.zoomOut()
        }
        
        binding.resetZoomButton.setOnClickListener {
            viewModel.resetZoom()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.zoomLevel.collect { zoom ->
                binding.scriptCanvas.setZoom(zoom)
            }
        }
        
        lifecycleScope.launch {
            viewModel.validationErrors.collect { errors ->
                updateValidationStatus(errors)
            }
        }
    }

    private fun addNode(title: String, color: Int) {
        nodeCounter++
        val node = VisualScriptCanvas.SimpleNode(
            id = "node_$nodeCounter",
            title = title,
            position = PointF(200f + (nodeCounter * 50f), 200f + (nodeCounter * 30f)),
            color = color
        )
        binding.scriptCanvas.addNode(node)
        
        Toast.makeText(this, "$title node added", Toast.LENGTH_SHORT).show()
    }

    private fun updateValidationStatus(errors: List<String>) {
        if (errors.isEmpty()) {
            binding.validationErrorsText.visibility = android.view.View.GONE
        } else {
            binding.validationErrorsText.apply {
                visibility = android.view.View.VISIBLE
                text = errors.joinToString("\n")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.studio_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_save -> {
                saveScript()
                true
            }
            R.id.action_load -> {
                loadScript()
                true
            }
            R.id.action_undo -> {
                viewModel.undo()
                true
            }
            R.id.action_redo -> {
                viewModel.redo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveScript() {
        // TODO: Implement script saving
        Toast.makeText(this, "Script saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadScript() {
        // TODO: Implement script loading
        Toast.makeText(this, "Script loaded", Toast.LENGTH_SHORT).show()
    }

    private fun setupPropertyEditor() {
        propertyEditor = NodePropertyEditor(this)
        propertyEditor.propertyChangeListener = this
    }

    private fun setupExecutionEngine() {
        executionEngine = ScriptExecutionEngine()
        executionEngine.executionListener = this
    }

    private fun setupServiceBridge() {
        serviceBridge = StudioServiceBridge(this)
        serviceBridge.setConnectionListener(this)
        serviceBridge.bindToService()
    }

    private fun setupPlaybackControls() {
        binding.playButton?.setOnClickListener {
            executeScript()
        }
        
        binding.pauseButton?.setOnClickListener {
            executionEngine.pauseExecution()
        }
        
        binding.stopButton?.setOnClickListener {
            executionEngine.stopExecution()
        }
    }

    // VisualScriptCanvas.OnNodeInteractionListener implementation
    override fun onNodeSelected(node: VisualScriptCanvas.SimpleNode) {
        binding.nodePropertiesPanel.visibility = android.view.View.VISIBLE
        binding.nodeTypeText.text = "Type: ${node.title}"
        
        // Show dynamic node properties
        propertyEditor.createPropertyEditor(binding.nodePropertiesContainer, node)
    }

    override fun onNodeMoved(node: VisualScriptCanvas.SimpleNode, newPosition: PointF) {
        // Node position updated in canvas, could trigger validation
        viewModel.validateScript()
    }

    override fun onConnectionCreated(fromNode: String, toNode: String) {
        val connection = VisualScriptCanvas.SimpleConnection(
            fromNodeId = fromNode,
            toNodeId = toNode,
            isActive = true
        )
        binding.scriptCanvas.addConnection(connection)
        
        Toast.makeText(this, "Connection created", Toast.LENGTH_SHORT).show()
    }

    private fun executeScript() {
        val nodes = binding.scriptCanvas.getNodes()
        val connections = binding.scriptCanvas.getConnections()
        val nodeProperties = viewModel.getAllNodeProperties()
        
        val context = ScriptExecutionEngine.ExecutionContext(
            nodes = nodes,
            connections = connections,
            nodeProperties = nodeProperties
        )
        
        executionEngine.executeScript(context)
    }

    // NodePropertyEditor.PropertyChangeListener implementation
    override fun onPropertyChanged(nodeId: String, propertyName: String, value: Any) {
        // Update node properties in the view model
        viewModel.updateNodeProperty(nodeId, propertyName, value)
        
        // Trigger validation
        viewModel.validateScript()
        
        Toast.makeText(this, "Property $propertyName updated", Toast.LENGTH_SHORT).show()
    }

    // ScriptExecutionEngine.ExecutionListener implementation
    override fun onExecutionStarted() {
        runOnUiThread {
            binding.playButton?.isEnabled = false
            binding.pauseButton?.isEnabled = true
            binding.stopButton?.isEnabled = true
            Toast.makeText(this, "Script execution started", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onExecutionPaused() {
        runOnUiThread {
            binding.playButton?.isEnabled = true
            binding.pauseButton?.isEnabled = false
            binding.stopButton?.isEnabled = true
            Toast.makeText(this, "Script execution paused", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onExecutionStopped() {
        runOnUiThread {
            binding.playButton?.isEnabled = true
            binding.pauseButton?.isEnabled = false
            binding.stopButton?.isEnabled = false
            Toast.makeText(this, "Script execution stopped", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onExecutionError(error: String) {
        runOnUiThread {
            binding.playButton?.isEnabled = true
            binding.pauseButton?.isEnabled = false
            binding.stopButton?.isEnabled = false
            Toast.makeText(this, "Execution error: $error", Toast.LENGTH_LONG).show()
        }
    }

    override fun onNodeExecuted(nodeId: String) {
        runOnUiThread {
            // Highlight the executed node in the canvas
            binding.scriptCanvas.highlightNode(nodeId)
        }
    }

    // StudioServiceBridge.ServiceConnectionListener implementation
    override fun onServiceConnected(service: com.tapcraft.pro.automation.SmartAutoClickerService) {
        runOnUiThread {
            Toast.makeText(this, "Accessibility service is enabled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onServiceDisconnected() {
        runOnUiThread {
            Toast.makeText(this, "Accessibility service is disabled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executionEngine.cleanup()
        serviceBridge.cleanup()
    }
}