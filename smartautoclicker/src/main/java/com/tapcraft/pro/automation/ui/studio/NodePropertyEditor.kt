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
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tapcraft.pro.automation.R
import com.tapcraft.pro.automation.ui.components.VisualScriptCanvas

/**
 * Dynamic property editor for script nodes
 */
class NodePropertyEditor(private val context: Context) {

    interface PropertyChangeListener {
        fun onPropertyChanged(nodeId: String, propertyName: String, value: Any)
    }

    var propertyChangeListener: PropertyChangeListener? = null

    fun createPropertyEditor(
        container: ViewGroup,
        node: VisualScriptCanvas.SimpleNode
    ) {
        container.removeAllViews()
        
        when (node.title.lowercase()) {
            "tap" -> createTapProperties(container, node)
            "swipe" -> createSwipeProperties(container, node)
            "wait" -> createWaitProperties(container, node)
            "if/else" -> createConditionProperties(container, node)
            "loop" -> createLoopProperties(container, node)
            else -> createGenericProperties(container, node)
        }
    }

    private fun createTapProperties(container: ViewGroup, node: VisualScriptCanvas.SimpleNode) {
        addSectionHeader(container, "Tap Action Properties")
        
        // Position
        addCoordinateInput(container, "X Position", "x_position", "100") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "x_position", value)
        }
        
        addCoordinateInput(container, "Y Position", "y_position", "100") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "y_position", value)
        }
        
        // Duration
        addNumberInput(container, "Press Duration (ms)", "duration", "50") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "duration", value)
        }
        
        // Click type
        addSpinnerInput(container, "Click Type", "click_type", 
            listOf("Single", "Double", "Long Press")) { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "click_type", value)
        }
    }

    private fun createSwipeProperties(container: ViewGroup, node: VisualScriptCanvas.SimpleNode) {
        addSectionHeader(container, "Swipe Action Properties")
        
        // Start position
        addCoordinateInput(container, "Start X", "start_x", "100") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "start_x", value)
        }
        
        addCoordinateInput(container, "Start Y", "start_y", "100") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "start_y", value)
        }
        
        // End position
        addCoordinateInput(container, "End X", "end_x", "200") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "end_x", value)
        }
        
        addCoordinateInput(container, "End Y", "end_y", "200") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "end_y", value)
        }
        
        // Duration
        addNumberInput(container, "Swipe Duration (ms)", "duration", "300") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "duration", value)
        }
    }

    private fun createWaitProperties(container: ViewGroup, node: VisualScriptCanvas.SimpleNode) {
        addSectionHeader(container, "Wait Action Properties")
        
        addNumberInput(container, "Wait Duration (ms)", "duration", "1000") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "duration", value)
        }
        
        addCheckboxInput(container, "Random Delay", "random_delay", false) { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "random_delay", value)
        }
    }

    private fun createConditionProperties(container: ViewGroup, node: VisualScriptCanvas.SimpleNode) {
        addSectionHeader(container, "Condition Properties")
        
        addSpinnerInput(container, "Condition Type", "condition_type",
            listOf("Image Detection", "Color Detection", "Text Detection", "Custom")) { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "condition_type", value)
        }
        
        addTextInput(container, "Condition Value", "condition_value", "") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "condition_value", value)
        }
        
        addNumberInput(container, "Threshold (%)", "threshold", "80") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "threshold", value)
        }
    }

    private fun createLoopProperties(container: ViewGroup, node: VisualScriptCanvas.SimpleNode) {
        addSectionHeader(container, "Loop Properties")
        
        addSpinnerInput(container, "Loop Type", "loop_type",
            listOf("Fixed Count", "While Condition", "Infinite")) { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "loop_type", value)
        }
        
        addNumberInput(container, "Loop Count", "loop_count", "1") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "loop_count", value)
        }
        
        addNumberInput(container, "Delay Between Loops (ms)", "loop_delay", "100") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "loop_delay", value)
        }
    }

    private fun createGenericProperties(container: ViewGroup, node: VisualScriptCanvas.SimpleNode) {
        addSectionHeader(container, "Node Properties")
        
        addTextInput(container, "Node Name", "name", node.title) { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "name", value)
        }
        
        addTextInput(container, "Description", "description", "") { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "description", value)
        }
        
        addCheckboxInput(container, "Enabled", "enabled", true) { value ->
            propertyChangeListener?.onPropertyChanged(node.id, "enabled", value)
        }
    }

    private fun addSectionHeader(container: ViewGroup, title: String) {
        val textView = TextView(context).apply {
            text = title
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 24, 0, 16)
        }
        container.addView(textView)
    }

    private fun addTextInput(
        container: ViewGroup,
        label: String,
        key: String,
        defaultValue: String,
        onValueChanged: (String) -> Unit
    ) {
        val layout = TextInputLayout(context).apply {
            hint = label
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        
        val editText = TextInputEditText(context).apply {
            setText(defaultValue)
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onValueChanged(text.toString())
                }
            }
        }
        
        layout.addView(editText)
        container.addView(layout)
    }

    private fun addNumberInput(
        container: ViewGroup,
        label: String,
        key: String,
        defaultValue: String,
        onValueChanged: (Int) -> Unit
    ) {
        val layout = TextInputLayout(context).apply {
            hint = label
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        
        val editText = TextInputEditText(context).apply {
            setText(defaultValue)
            inputType = InputType.TYPE_CLASS_NUMBER
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val value = text.toString().toIntOrNull() ?: 0
                    onValueChanged(value)
                }
            }
        }
        
        layout.addView(editText)
        container.addView(layout)
    }

    private fun addCoordinateInput(
        container: ViewGroup,
        label: String,
        key: String,
        defaultValue: String,
        onValueChanged: (Int) -> Unit
    ) {
        addNumberInput(container, label, key, defaultValue, onValueChanged)
    }

    private fun addSpinnerInput(
        container: ViewGroup,
        label: String,
        key: String,
        options: List<String>,
        onValueChanged: (String) -> Unit
    ) {
        val textView = TextView(context).apply {
            text = label
            setPadding(0, 16, 0, 8)
        }
        container.addView(textView)
        
        val spinner = Spinner(context).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, options).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    onValueChanged(options[position])
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        
        container.addView(spinner)
    }

    private fun addCheckboxInput(
        container: ViewGroup,
        label: String,
        key: String,
        defaultValue: Boolean,
        onValueChanged: (Boolean) -> Unit
    ) {
        val checkBox = CheckBox(context).apply {
            text = label
            isChecked = defaultValue
            setOnCheckedChangeListener { _, isChecked ->
                onValueChanged(isChecked)
            }
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 16, 0, 16)
        }
        
        container.addView(checkBox)
    }
}