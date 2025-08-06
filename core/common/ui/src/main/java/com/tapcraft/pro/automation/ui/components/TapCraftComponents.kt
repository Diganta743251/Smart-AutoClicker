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
package com.tapcraft.pro.automation.ui.components

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Modern glassmorphism card component
 */
class GlassmorphismCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val blurRadius = 20f
    private val cornerRadius = 24f

    init {
        setWillNotDraw(false)
        setupGlassmorphism()
    }

    private fun setupGlassmorphism() {
        // Create glassmorphism background
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = this@GlassmorphismCard.cornerRadius
            setColor(Color.parseColor("#40FFFFFF"))
            setStroke(1, Color.parseColor("#60FFFFFF"))
        }
        background = drawable

        // Add elevation for depth
        elevation = 8f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Draw subtle inner glow
        paint.apply {
            shader = RadialGradient(
                width / 2f, height / 2f, width / 2f,
                intArrayOf(
                    Color.parseColor("#20FFFFFF"),
                    Color.parseColor("#00FFFFFF")
                ),
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
    }
}

/**
 * Visual script canvas for drag-and-drop node editing
 */
class VisualScriptCanvas @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Simplified node data class
    data class SimpleNode(
        val id: String,
        val title: String,
        val position: PointF,
        val color: Int = Color.parseColor("#2196F3"),
        val isSelected: Boolean = false
    )
    
    // Simplified connection data class
    data class SimpleConnection(
        val fromNodeId: String,
        val toNodeId: String,
        val isActive: Boolean = false
    )

    private val nodes = mutableListOf<SimpleNode>()
    private val connections = mutableListOf<SimpleConnection>()
    
    private val nodePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val connectionPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private var selectedNode: SimpleNode? = null
    private var isDragging = false
    private var dragOffset = PointF()
    private var canvasOffset = PointF(0f, 0f)
    private var zoomLevel = 1.0f
    
    // Node dimensions
    private val nodeWidth = 120f
    private val nodeHeight = 80f
    private val nodeCornerRadius = 12f
    private val portRadius = 8f
    
    interface OnNodeInteractionListener {
        fun onNodeSelected(node: SimpleNode)
        fun onNodeMoved(node: SimpleNode, newPosition: PointF)
        fun onConnectionCreated(fromNode: String, toNode: String)
    }
    
    var nodeInteractionListener: OnNodeInteractionListener? = null

    init {
        setupPaints()
    }

    private fun setupPaints() {
        nodePaint.apply {
            style = Paint.Style.FILL
            setShadowLayer(4f, 0f, 2f, Color.parseColor("#40000000"))
        }
        
        connectionPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            pathEffect = CornerPathEffect(8f)
        }
        
        textPaint.apply {
            textSize = 24f
            textAlign = Paint.Align.CENTER
            color = Color.WHITE
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    fun addNode(node: SimpleNode) {
        nodes.add(node)
        invalidate()
    }

    fun removeNode(nodeId: String) {
        nodes.removeAll { it.id == nodeId }
        connections.removeAll { it.fromNodeId == nodeId || it.toNodeId == nodeId }
        invalidate()
    }

    fun addConnection(connection: SimpleConnection) {
        connections.add(connection)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        canvas.save()
        canvas.translate(canvasOffset.x, canvasOffset.y)
        canvas.scale(zoomLevel, zoomLevel)
        
        // Draw grid background
        drawGrid(canvas)
        
        // Draw connections
        drawConnections(canvas)
        
        // Draw nodes
        drawNodes(canvas)
        
        canvas.restore()
    }

    private fun drawGrid(canvas: Canvas) {
        val gridSize = 50f
        val gridPaint = Paint().apply {
            color = Color.parseColor("#20FFFFFF")
            strokeWidth = 1f
        }
        
        val startX = (-canvasOffset.x / zoomLevel / gridSize).toInt() * gridSize
        val startY = (-canvasOffset.y / zoomLevel / gridSize).toInt() * gridSize
        val endX = startX + (width / zoomLevel) + gridSize
        val endY = startY + (height / zoomLevel) + gridSize
        
        var x = startX
        while (x <= endX) {
            canvas.drawLine(x, startY, x, endY, gridPaint)
            x += gridSize
        }
        
        var y = startY
        while (y <= endY) {
            canvas.drawLine(startX, y, endX, y, gridPaint)
            y += gridSize
        }
    }

    private fun drawConnections(canvas: Canvas) {
        connections.forEach { connection ->
            val fromNode = nodes.find { it.id == connection.fromNodeId }
            val toNode = nodes.find { it.id == connection.toNodeId }
            
            if (fromNode != null && toNode != null) {
                val fromPoint = PointF(
                    fromNode.position.x + nodeWidth,
                    fromNode.position.y + nodeHeight / 2
                )
                val toPoint = PointF(
                    toNode.position.x,
                    toNode.position.y + nodeHeight / 2
                )
                
                drawBezierConnection(canvas, fromPoint, toPoint, connection.isActive)
            }
        }
    }

    private fun drawBezierConnection(canvas: Canvas, from: PointF, to: PointF, isActive: Boolean) {
        val path = Path()
        val controlOffset = abs(to.x - from.x) * 0.5f
        
        path.moveTo(from.x, from.y)
        path.cubicTo(
            from.x + controlOffset, from.y,
            to.x - controlOffset, to.y,
            to.x, to.y
        )
        
        connectionPaint.color = if (isActive) {
            Color.parseColor("#FF4CAF50")
        } else {
            Color.parseColor("#80FFFFFF")
        }
        
        canvas.drawPath(path, connectionPaint)
    }

    private fun drawNodes(canvas: Canvas) {
        nodes.forEach { node ->
            drawNode(canvas, node)
        }
    }

    private fun drawNode(canvas: Canvas, node: SimpleNode) {
        val rect = RectF(
            node.position.x,
            node.position.y,
            node.position.x + nodeWidth,
            node.position.y + nodeHeight
        )
        
        // Node background
        nodePaint.color = if (node.isSelected) {
            adjustBrightness(node.color, 1.2f)
        } else {
            node.color
        }
        
        canvas.drawRoundRect(rect, nodeCornerRadius, nodeCornerRadius, nodePaint)
        
        // Node border for selection
        if (node.isSelected) {
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 3f
                color = Color.WHITE
            }
            canvas.drawRoundRect(rect, nodeCornerRadius, nodeCornerRadius, borderPaint)
        }
        
        // Node title
        canvas.drawText(
            node.title,
            rect.centerX(),
            rect.centerY() + textPaint.textSize / 3,
            textPaint
        )
    }

    private fun findNodeAtPosition(position: PointF): SimpleNode? {
        return nodes.find { node ->
            position.x >= node.position.x &&
            position.x <= node.position.x + nodeWidth &&
            position.y >= node.position.y &&
            position.y <= node.position.y + nodeHeight
        }
    }

    private fun adjustBrightness(color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = (hsv[2] * factor).coerceIn(0f, 1f)
        return Color.HSVToColor(hsv)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val touchPoint = PointF(
                    (event.x - canvasOffset.x) / zoomLevel,
                    (event.y - canvasOffset.y) / zoomLevel
                )
                
                selectedNode = findNodeAtPosition(touchPoint)
                selectedNode?.let { node ->
                    isDragging = true
                    dragOffset.set(
                        touchPoint.x - node.position.x,
                        touchPoint.y - node.position.y
                    )
                    nodeInteractionListener?.onNodeSelected(node)
                    invalidate()
                } ?: run {
                    // Start panning if no node is selected
                    isDragging = false
                    dragOffset.set(event.x, event.y)
                }
                return true
            }
            
            MotionEvent.ACTION_MOVE -> {
                if (isDragging && selectedNode != null) {
                    // Node dragging
                    val newPosition = PointF(
                        (event.x - canvasOffset.x) / zoomLevel - dragOffset.x,
                        (event.y - canvasOffset.y) / zoomLevel - dragOffset.y
                    )
                    
                    selectedNode = selectedNode!!.copy(position = newPosition)
                    val index = nodes.indexOfFirst { it.id == selectedNode!!.id }
                    if (index >= 0) {
                        nodes[index] = selectedNode!!
                    }
                    
                    nodeInteractionListener?.onNodeMoved(selectedNode!!, newPosition)
                    invalidate()
                } else if (!isDragging && selectedNode == null) {
                    // Canvas panning
                    val deltaX = event.x - dragOffset.x
                    val deltaY = event.y - dragOffset.y
                    panCanvas(deltaX, deltaY)
                    dragOffset.set(event.x, event.y)
                }
                return true
            }
            
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragging = false
                selectedNode = null
                return true
            }
            
            MotionEvent.ACTION_POINTER_DOWN -> {
                // Multi-touch for zooming (basic implementation)
                if (event.pointerCount == 2) {
                    isDragging = false
                    selectedNode = null
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }



    fun setZoom(zoom: Float) {
        zoomLevel = zoom.coerceIn(0.1f, 3.0f)
        invalidate()
    }

    fun panCanvas(deltaX: Float, deltaY: Float) {
        canvasOffset.offset(deltaX, deltaY)
        invalidate()
    }

    fun getNodes(): List<SimpleNode> {
        return nodes.toList()
    }

    fun getConnections(): List<SimpleConnection> {
        return connections.toList()
    }

    fun highlightNode(nodeId: String) {
        // Find and highlight the node
        val nodeIndex = nodes.indexOfFirst { it.id == nodeId }
        if (nodeIndex >= 0) {
            val node = nodes[nodeIndex]
            // Create a highlighted version of the node
            nodes[nodeIndex] = node.copy(isSelected = true)
            invalidate()
            
            // Remove highlight after a short delay
            postDelayed({
                nodes[nodeIndex] = node.copy(isSelected = false)
                invalidate()
            }, 1000)
        }
    }
}

/**
 * Modern floating action button with ripple effect
 */
class ModernFloatingActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val ripplePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rippleRadius = 0f
    private var isPressed = false
    
    var onClickListener: (() -> Unit)? = null

    init {
        setupPaints()
        isClickable = true
    }

    private fun setupPaints() {
        paint.apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#FF4CAF50")
            setShadowLayer(8f, 0f, 4f, Color.parseColor("#40000000"))
        }
        
        ripplePaint.apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#40FFFFFF")
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 2f - 8f
        
        // Draw main button
        canvas.drawCircle(centerX, centerY, radius, paint)
        
        // Draw ripple effect
        if (isPressed && rippleRadius > 0) {
            canvas.drawCircle(centerX, centerY, rippleRadius, ripplePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                startRippleAnimation()
                return true
            }
            MotionEvent.ACTION_UP -> {
                isPressed = false
                onClickListener?.invoke()
                invalidate()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                isPressed = false
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun startRippleAnimation() {
        // Simple ripple animation (in a real implementation, use ValueAnimator)
        rippleRadius = minOf(width, height) / 4f
        invalidate()
    }
}

/**
 * Script execution progress indicator
 */
class ScriptProgressIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    var progress: Float = 0f
        set(value) {
            field = value.coerceIn(0f, 1f)
            invalidate()
        }
    
    var statusText: String = ""
        set(value) {
            field = value
            invalidate()
        }

    init {
        setupPaints()
    }

    private fun setupPaints() {
        backgroundPaint.apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#20FFFFFF")
        }
        
        progressPaint.apply {
            style = Paint.Style.FILL
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                intArrayOf(
                    Color.parseColor("#FF4CAF50"),
                    Color.parseColor("#FF2196F3")
                ),
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        
        textPaint.apply {
            textSize = 32f
            textAlign = Paint.Align.CENTER
            color = Color.WHITE
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val cornerRadius = height / 2f
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        
        // Draw background
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, backgroundPaint)
        
        // Draw progress
        val progressWidth = width * progress
        val progressRect = RectF(0f, 0f, progressWidth, height.toFloat())
        canvas.drawRoundRect(progressRect, cornerRadius, cornerRadius, progressPaint)
        
        // Draw status text
        if (statusText.isNotEmpty()) {
            canvas.drawText(
                statusText,
                width / 2f,
                height / 2f + textPaint.textSize / 3,
                textPaint
            )
        }
        
        // Draw percentage
        val percentageText = "${(progress * 100).toInt()}%"
        canvas.drawText(
            percentageText,
            width / 2f,
            height / 2f - textPaint.textSize / 2,
            textPaint
        )
    }
}