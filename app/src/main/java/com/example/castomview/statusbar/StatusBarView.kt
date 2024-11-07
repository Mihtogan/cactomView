package com.example.castomview.statusbar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import com.example.castomview.R
import java.lang.Integer.min

class StatusBarView @JvmOverloads constructor(
    context: Context, attributes: AttributeSet? = null, defStyleRes: Int = 0,
) : View(context, attributes, defStyleRes) {

    private val defHeight = 60
    private val defWidth = 500

    private val defColorBackground = Color.BLACK

    private val defDrawMode = 0

    private val defRounding = 1f
    private val defGap = 0.01f

    private val defStatuses = listOf(
        StatusElement(Color.GREEN, 0.5f),
        StatusElement(Color.YELLOW, 0.3f),
        StatusElement(Color.RED, 0.2f)
    )

    private val paintBack = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val paintStatus = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    }

    private var drawMode = defDrawMode
    private val drawOptions by lazy { setOf(drawOption1()) }

    private var heightView = defHeight
    private var widthView = defWidth

    private var colorBackground = defColorBackground

    private var rounding = defRounding
    private var round = heightView / 2 * rounding
    private var gap = defGap

    private val statuses = defStatuses.toMutableList()

    init {
        val attrs = context.obtainStyledAttributes(attributes, R.styleable.StatusBarView)

        drawMode =
            attrs.getInt(R.styleable.StatusBarView_drawMode, defDrawMode)
        colorBackground =
            attrs.getColor(R.styleable.StatusBarView_colorBackground, defColorBackground)
        rounding =
            attrs.getFloat(R.styleable.StatusBarView_myRounding, defRounding)
        gap =
            attrs.getFloat(R.styleable.StatusBarView_gapBetween, defGap)

        attrs.recycle()
    }

    fun setStatuses(newStatuses: List<StatusElement>) {
        statuses.clear()
        statuses.addAll(newStatuses)

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize // Задан конкретный размер для ширины
            MeasureSpec.AT_MOST -> min(
                defWidth,
                widthSize
            ) // Размер не должен превышать заданный размер
            else -> defWidth // Задать предпочтительный размер, если точного или максимального размера не задано
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize // Задан конкретный размер для высоты
            MeasureSpec.AT_MOST -> min(
                defHeight,
                heightSize
            ) // Размер не должен превышать заданный размер
            else -> defHeight // Задать предпочтительный размер, если точного или максимального размера не задано
        }

        widthView = width
        heightView = height

        round = heightView / 2 * rounding

        setMeasuredDimension(width, height) // Устанавливаем фактический размер View
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val myBitmap = drawOptions.elementAt(drawMode)

        canvas?.drawBitmap(myBitmap, 0f, 0f, paintBack)
    }

    private fun drawOption1(): Bitmap {
        return createBitmap(widthView, heightView, Bitmap.Config.ARGB_8888).applyCanvas {

            paintBack.color = colorBackground
            drawRoundRect(
                0f,
                0f,
                widthView.toFloat(),
                heightView.toFloat(),
                round,
                round,
                paintBack
            )

            var left = 0f
            val wid = widthView * (1 - (statuses.size - 1) * gap)

            statuses.forEach { element ->
                paintStatus.color = element.color

                drawRect(
                    left,
                    0f,
                    left + wid * element.value,
                    heightView.toFloat(),
                    paintStatus
                )
                left += wid * element.value + widthView * gap
            }
        }
    }
}