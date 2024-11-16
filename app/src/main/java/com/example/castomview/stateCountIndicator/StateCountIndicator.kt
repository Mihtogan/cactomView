package com.example.castomview.stateCountIndicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.example.castomview.R

class StateCountIndicator @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?
) : View(
    context,
    attrs
) {

    companion object {
        private const val defHeight = 800
        private const val defWidth = 70

        private const val defColorCircul = Color.BLUE
        private const val defColorStatusDescription = Color.GRAY
        private const val defColorStatusQuantity = Color.BLACK
        private const val defColorStatusTotalQuantity = Color.GRAY

        private const val defTextSiz = 40

        private const val defTextStatusDescription = "состояние"
        private const val defQuantity = 4
        private const val defTotalQuantity = 10
    }

    private var widthView = defWidth
    private var heightView = defHeight

    private var colorCircul = defColorCircul
    private var colorStatusDescription = defColorStatusDescription
    private var colorStatusQuantity = defColorStatusQuantity
    private var colorStatusTotalQuantity = defColorStatusTotalQuantity

    private var textSize = defTextSiz

    private var textStatusDescription = defTextStatusDescription
    private var quantity = defQuantity
    private var totalQuantity = defTotalQuantity

    private val paintCircul = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintText = Paint().apply {

    }

    init {
        context?.withStyledAttributes(attrs, R.styleable.StateCountIndicator) {
            colorCircul = getColor(
                R.styleable.StateCountIndicator_colorCircul,
                defColorCircul
            )
            colorStatusDescription = getColor(
                R.styleable.StateCountIndicator_colorStatusDescription,
                defColorStatusDescription
            )
            colorStatusQuantity = getColor(
                R.styleable.StateCountIndicator_colorStatusQuantity,
                defColorStatusQuantity
            )
            colorStatusTotalQuantity = getColor(
                R.styleable.StateCountIndicator_colorStatusTotalQuantity,
                defColorStatusTotalQuantity
            )
            textSize = getInt(
                R.styleable.StateCountIndicator_textSize,
                defTextSiz
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        widthView = resolveSize(defWidth, widthMeasureSpec)
        heightView = resolveSize(defHeight, heightMeasureSpec)

        setMeasuredDimension(widthView, heightView)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}