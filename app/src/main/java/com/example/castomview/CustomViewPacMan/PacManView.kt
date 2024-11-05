package com.example.castomview.CustomViewPacMan

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.castomview.R
import java.lang.Integer.min
import kotlin.math.PI
import kotlin.math.atan2


class PacManView @JvmOverloads constructor(
    context: Context, attributes: AttributeSet
) : View(context, attributes) {
    private val colorPacManRed = ContextCompat.getColor(context, R.color.paManR)
    private val colorPacManDefoliate = ContextCompat.getColor(context, R.color.pacManBody)


    private val paintPacManBody = Paint().apply {
        color = colorPacManDefoliate
        style = Paint.Style.FILL
    }

    private var mouthWidth = 60f
    private var directionEyes = 180f
    private val sizePacMan = 200f
    private var positionPacManX = 0f
    private var positionPacManY = 0f


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 500 // Предполагаемая ширина View
        val desiredHeight = 500 // Предполагаемая высота View

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize // Задан конкретный размер для ширины
            MeasureSpec.AT_MOST -> min(
                desiredWidth,
                widthSize
            ) // Размер не должен превышать заданный размер
            else -> desiredWidth // Задать предпочтительный размер, если точного или максимального размера не задано
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize // Задан конкретный размер для высоты
            MeasureSpec.AT_MOST -> min(
                desiredHeight,
                heightSize
            ) // Размер не должен превышать заданный размер
            else -> desiredHeight // Задать предпочтительный размер, если точного или максимального размера не задано
        }

        positionPacManX = width / 2f
        positionPacManY = height / 2f

        setMeasuredDimension(width, height) // Устанавливаем фактический размер View
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        canvas?.drawArc(
            positionPacManX - sizePacMan / 2,
            positionPacManY - sizePacMan / 2,
            positionPacManX + sizePacMan / 2,
            positionPacManY + sizePacMan / 2,
            directionEyes + mouthWidth / 2,
            360f - mouthWidth,
            true,
            paintPacManBody
        )

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val xEvent = event.x
        val yEvent = event.y

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                paintPacManBody.color = colorPacManRed
                turnTo(xEvent, yEvent)

                animator.start()
                invalidate()
                true
            }

            MotionEvent.ACTION_MOVE -> {
                turnTo(xEvent, yEvent)
                invalidate()
                true
            }

            MotionEvent.ACTION_UP -> {
                paintPacManBody.color = colorPacManDefoliate

                animator.end()
                invalidate()
                true
            }

            else -> super.onTouchEvent(event)
        }
    }

    private val animator = ValueAnimator
        .ofFloat(60f, 0f, 60f).apply {
            duration = 500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART

            addUpdateListener { animation ->
                mouthWidth = animation.animatedValue as Float
                invalidate()
            }
        }

    private fun turnTo(x: Float, y: Float) {
        val xRelative = positionPacManX - x
        val yRelative = positionPacManY - y

        directionEyes = ((atan2(yRelative, xRelative) + PI) / 2f / PI * 360f).toFloat()
    }
}