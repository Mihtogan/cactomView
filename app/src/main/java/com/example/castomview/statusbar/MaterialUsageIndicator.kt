package com.example.castomview.statusbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.example.castomview.R

class MaterialUsageIndicator @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleRes: Int = 0,
) : View(
    context,
    attributes,
    defStyleRes
) {
    companion object {
        private const val MIN_HEIGHT = 20
        private const val MIN_WIDTH = 40

        private const val DEFAULT_COLOR_BACKGROUND = Color.BLACK

        private const val DEFAULT_DRAW_MODE = 0

        private const val DEFAULT_CORNER_RADIUS = 0f
        private const val DEFAULT_GAP = 5f

        private const val DEFAULT_ANIMATION_TIME = 2000

        private const val IS_ROUNDING_STATUS_LINE = false
    }

    private val paintBackground = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val paintStatus = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    }

    private val path = Path()

    private var drawMode = DEFAULT_DRAW_MODE
    private val drawOptions by lazy {
        setOf(
            simpleRendering,
            synchronousAnimation,
            sequentialAnimation,
        )
    }
    private var animationTime = DEFAULT_ANIMATION_TIME

    private var heightView = MIN_HEIGHT
    private var widthView = MIN_WIDTH

    private var colorBackground = DEFAULT_COLOR_BACKGROUND

    private var cornerRadius = DEFAULT_CORNER_RADIUS
    private var gap = DEFAULT_GAP

    private var isRoundingStatusLine = IS_ROUNDING_STATUS_LINE

    private val statuses = mutableListOf<StatusElement>()
    private var statusSymValue = 0
    private val drawStat = mutableListOf<DrawStat>()

    init {
        context.withStyledAttributes(attributes, R.styleable.MaterialUsageIndicator) {
            drawMode = getInt(
                R.styleable.MaterialUsageIndicator_statusBar_drawMode,
                DEFAULT_DRAW_MODE
            )
            colorBackground = getColor(
                R.styleable.MaterialUsageIndicator_statusBar_colorBackground,
                DEFAULT_COLOR_BACKGROUND
            )
            cornerRadius = getDimension(
                R.styleable.MaterialUsageIndicator_statusBar_myRounding,
                DEFAULT_CORNER_RADIUS
            )
            gap = getDimension(
                R.styleable.MaterialUsageIndicator_statusBar_gapBetween,
                DEFAULT_GAP
            )
            animationTime = getInt(
                R.styleable.MaterialUsageIndicator_statusBar_animationTime,
                DEFAULT_ANIMATION_TIME
            )
            isRoundingStatusLine = getBoolean(
                R.styleable.MaterialUsageIndicator_statusBar_isRoundingStatusLine,
                IS_ROUNDING_STATUS_LINE
            )
        }
    }

    fun setStatuses(newStatuses: List<StatusElement>) {
        post {
            statuses.clear()
            statusSymValue = 0

            newStatuses.forEach {
                statuses.add(it)
                statusSymValue += it.value
            }

            reDraw()
        }
    }

    fun setAnimationTime(time: Int) {
        animationTime = time
    }

    fun setDrawMode(drawMod: Int) {
        drawMode = if (drawMod in drawOptions.indices) drawMod else 0
    }

    fun reDraw() {
        drawOptions.elementAt(drawMode).invoke()
        invalidate()
    }

    fun isRoundingStatusLine(isRouning: Boolean) {
        isRoundingStatusLine = isRouning
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        widthView = resolveSize(MIN_WIDTH, widthMeasureSpec) - paddingStart - paddingEnd
        heightView = resolveSize(MIN_HEIGHT, heightMeasureSpec) - paddingTop - paddingBottom

        setMeasuredDimension(
            widthView + paddingStart + paddingEnd,
            heightView + paddingTop + paddingBottom
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        path.reset()
        path.addRoundRect(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            (widthView + paddingStart).toFloat(),
            (heightView + paddingTop).toFloat(),
            cornerRadius,
            cornerRadius,
            Path.Direction.CW
        )

        canvas?.clipPath(path)

        paintBackground.color = colorBackground
        canvas?.drawRect(
            paddingStart.toFloat(),
            paddingTop.toFloat(),
            (widthView + paddingStart).toFloat(),
            (heightView + paddingTop).toFloat(),
            paintBackground
        )

        drawStat.forEach { element ->
            paintStatus.color = element.color

            canvas?.drawRoundRect(
                element.left,
                paddingTop.toFloat(),
                element.right,
                (heightView + paddingTop).toFloat(),
                if (isRoundingStatusLine) cornerRadius else 0f,
                if (isRoundingStatusLine) cornerRadius else 0f,
                paintStatus
            )
        }
    }

    private val simpleRendering: () -> Unit = {

        drawStat.clear()

        var left = paddingStart.toFloat()
        var right: Float
        val wid = widthView - (statuses.size - 1) * gap

        statuses.forEach { element ->

            right = left + wid * (element.value.toFloat() / statusSymValue.toFloat())

            drawStat.add(DrawStat(left, right, element.color))

            left = right + gap
        }
    }


    private val synchronousAnimation: () -> Unit = {
        drawOptions.elementAt(0).invoke()

        drawStat.forEach {
            it.rightForAnim = it.right
        }

        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = animationTime.toLong()

            addUpdateListener { animation ->
                drawStat.forEach {
                    it.right =
                        it.left + (it.rightForAnim - it.left) * (animation.animatedValue as Float)
                }

                invalidate()
            }
        }.start()
    }

    private val sequentialAnimation: () -> Unit = {
        drawOptions.elementAt(0).invoke()

        drawStat.forEach {
            it.rightForAnim = it.right
        }

        var aniRig: Float

        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = animationTime.toLong()

            addUpdateListener { animation ->
                aniRig = paddingStart + widthView * animation.animatedValue as Float

                drawStat.forEach {
                    it.right = when {
                        it.left > aniRig ->
                            it.left

                        aniRig in it.left..it.rightForAnim ->
                            aniRig

                        it.rightForAnim < aniRig ->
                            it.rightForAnim

                        else ->
                            it.left
                    }
                }
                invalidate()
            }
        }.start()
    }

    private data class DrawStat(
        var left: Float,
        var right: Float,
        val color: Int,
        var rightForAnim: Float = 0f,
    )
}