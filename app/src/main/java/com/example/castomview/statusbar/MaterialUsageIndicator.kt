package com.example.castomview.statusbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
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
        private const val defHeight = 60
        private const val defWidth = 500

        private const val defColorBackground = Color.BLACK

        private const val defDrawMode = 0

        private const val defCornerRadius = 0f
        private const val defGap = 5f

        private const val defAnimationTime = 2000
    }

    private val paintBack = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val paintStatus = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    }

    private val rect = RectF()
    private val path = Path()

    private var drawMode = defDrawMode
    private val drawOptions by lazy {
        setOf(
            drawOption1,
            drawOptionAnimat1,
            drawOptionAnimat2,
        )
    }
    private var animationTime = defAnimationTime

    private var heightView = defHeight
    private var widthView = defWidth

    private var colorBackground = defColorBackground

    private var cornerRadius = defCornerRadius
    private var gap = defGap

    private val statuses = mutableListOf<StatusElement>()
    private var statusSymValue = 0
    private val drawStat = mutableListOf<DrawStat>()

    init {
        context.withStyledAttributes(attributes, R.styleable.StatusBarView) {
            drawMode =
                getInt(R.styleable.StatusBarView_drawMode, defDrawMode)
            colorBackground =
                getColor(R.styleable.StatusBarView_colorBackground, defColorBackground)
            cornerRadius =
                getDimension(R.styleable.StatusBarView_myRounding, defCornerRadius)
            gap =
                getDimension(R.styleable.StatusBarView_gapBetween, defGap)
            animationTime =
                getInt(R.styleable.StatusBarView_animationTime, defAnimationTime)
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

            drawOptions.elementAt(drawMode).invoke()
            invalidate()
        }
    }

    fun setAnimationTime(time: Int) {
        animationTime = time
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        widthView = resolveSize(defWidth, widthMeasureSpec)
        heightView = resolveSize(defHeight, heightMeasureSpec)

        setMeasuredDimension(widthView, heightView)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        rect.set(0f, 0f, widthView.toFloat(), heightView.toFloat())
        path.reset()
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)

        canvas?.clipPath(path)

        paintBack.color = colorBackground
        canvas?.drawRect(rect, paintBack)

        drawStat.forEach { element ->
            paintStatus.color = element.color

            canvas?.drawRect(
                element.left,
                0f,
                element.right,
                heightView.toFloat(),
                paintStatus
            )
        }
    }

    private val drawOption1: () -> Unit = {

        drawStat.clear()

        var left = 0f
        var right = 0f
        val wid = widthView - (statuses.size - 1) * gap

        statuses.forEach { element ->

            right = left + wid * (element.value.toFloat() / statusSymValue.toFloat())

            drawStat.add(DrawStat(left, right, element.color))

            left = right + gap
        }
    }


    private val drawOptionAnimat1: () -> Unit = {
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

    private val drawOptionAnimat2: () -> Unit = {
        drawOptions.elementAt(0).invoke()

        drawStat.forEach {
            it.rightForAnim = it.right
        }

        var aniRig = 0f

        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = animationTime.toLong()

            addUpdateListener { animation ->
                aniRig = widthView * animation.animatedValue as Float

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
}