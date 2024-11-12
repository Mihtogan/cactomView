package com.example.castomview.statusIndicatorViewGroup

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.size
import com.example.castomview.R

class StatusIndicatorViewGroup @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var indicatorView: View? = null
    private var statusTextView: TextView? = null
    private var countTextView: TextView? = null

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CLIP_VERTICAL

        LayoutInflater.from(context).inflate(R.layout.view_status_indicator, this, true)

        indicatorView = findViewById(R.id.indicatorView)
        statusTextView = findViewById(R.id.statusTextView)
        countTextView = findViewById(R.id.countTextView)

        context?.withStyledAttributes(attrs, R.styleable.StatusIndicatorViewGroup) {
            indicatorView?.setBackgroundColor(
                getColor(R.styleable.StatusIndicatorViewGroup_indicatorColor, Color.BLUE)
            )
            statusTextView?.setTextColor(
                getColor(R.styleable.StateCountIndicator_colorStatusQuantity, Color.BLUE)
            )
            countTextView?.setTextColor(
                getColor(R.styleable.StatusIndicatorViewGroup_countColor, Color.BLUE)
            )

            statusTextView?.apply {
                text = getText(R.styleable.StatusIndicatorViewGroup_statusText)
                textSize = getDimension(R.styleable.StatusIndicatorViewGroup_android_textSize, 20F)
            }
            countTextView?.apply {
                text = getInt(R.styleable.StatusIndicatorViewGroup_count, 0).toString()
                textSize = getDimension(R.styleable.StatusIndicatorViewGroup_android_textSize, 20F)
            }
        }
    }

    fun setIndicatorColor(color: Int) {
        indicatorView?.setBackgroundColor(color)
    }

    fun setStatusText(text: String) {
        statusTextView?.text = text
    }

    fun setCount(count: Int) {
        countTextView?.text = "$count"
    }
}