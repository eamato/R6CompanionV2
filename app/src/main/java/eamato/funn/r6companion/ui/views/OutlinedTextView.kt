package eamato.funn.r6companion.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import eamato.funn.r6companion.R

class OutlinedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    @ColorInt
    private var strokeColor: Int = Color.WHITE

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OutlinedTextView,
            0,
            0
        )
            .apply {
                try {
                    strokeColor = getColor(R.styleable.OutlinedTextView_strokeColor, Color.WHITE)
                } finally {
                    recycle()
                }
            }
    }

    override fun onDraw(canvas: Canvas) {
        val textColor = textColors.defaultColor
        setTextColor(strokeColor)
        paint.strokeWidth = 6f
        paint.style = Paint.Style.STROKE
        super.onDraw(canvas)

        setTextColor(textColor)
        paint.strokeWidth = 0f
        paint.style = Paint.Style.FILL
        super.onDraw(canvas)
    }
}