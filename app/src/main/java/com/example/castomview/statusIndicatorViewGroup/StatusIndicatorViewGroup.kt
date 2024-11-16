package com.example.castomview.statusIndicatorViewGroup

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.updateLayoutParams
import com.example.castomview.R

class StatusIndicatorViewGroup @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private companion object {
        const val DEFAULT_SIZ = 20F
        const val DEFAULT_COLOR = Color.BLUE
    }

    private var indicatorView: View? = null
    private var statusTextView: TextView? = null
    private var countTextView: TextView? = null
    private var totalCountTextView: TextView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_status_indicator, this, true)

        indicatorView = findViewById(R.id.indicatorView)
        statusTextView = findViewById(R.id.statusTextView)
        countTextView = findViewById(R.id.countTextView)
        totalCountTextView = findViewById(R.id.totalCountTextView)

        context?.withStyledAttributes(attrs, R.styleable.StatusIndicatorViewGroup) {
            indicatorView?.backgroundTintList = ColorStateList.valueOf(
                getColor(
                    R.styleable.StatusIndicatorViewGroup_statusIndicatorViewGroup_indicatorColor,
                    DEFAULT_COLOR
                )
            )

            val indicatorSiz = getDimension(
                R.styleable.StatusIndicatorViewGroup_statusIndicatorViewGroup_indicatorSize,
                DEFAULT_SIZ
            ).toInt()

            indicatorView?.updateLayoutParams {
                width = indicatorSiz
                height = indicatorSiz
            }

            statusTextView?.apply {
                setTextColor(
                    getColor(
                        R.styleable.StatusIndicatorViewGroup_statusIndicatorViewGroup_statusTextColor,
                        DEFAULT_COLOR
                    )
                )
                text = getText(
                    R.styleable.StatusIndicatorViewGroup_statusIndicatorViewGroup_statusText
                )
                textSize = getDimension(
                    R.styleable.StatusIndicatorViewGroup_statusIndicatorViewGroup_textSizeStatus,
                    DEFAULT_SIZ
                )
            }

            countTextView?.apply {
                setTextColor(
                    getColor(
                        R.styleable.StatusIndicatorViewGroup_statusIndicatorViewGroup_countColor,
                        DEFAULT_COLOR
                    )
                )
                textSize = getDimension(
                    R.styleable.StatusIndicatorViewGroup_statusIndicatorViewGroup_textSizeCount,
                    DEFAULT_SIZ
                )
            }

            totalCountTextView?.apply {
                setTextColor(
                    getColor(
                        R.styleable.StatusIndicatorViewGroup_statusIndicatorViewGroup_totalCountColor,
                        DEFAULT_COLOR
                    )
                )
                textSize = getDimension(
                    R.styleable.StatusIndicatorViewGroup_statusIndicatorViewGroup_textSizeTotalCount,
                    DEFAULT_SIZ
                )
            }
        }
    }

    fun setIndicatorColor(color: Int) {
        val background = indicatorView?.background as GradientDrawable
        background.setColor(color)
    }

    fun setStatusText(text: String) {
        statusTextView?.text = text
    }

    fun setCount(count: Int) {
        countTextView?.text = "$count"
    }

    fun setTotalCount(count: Int) {
        totalCountTextView?.text = "/$count"
    }
}