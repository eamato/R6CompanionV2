package eamato.funn.r6companion.ui.recyclerviews.decorations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import eamato.funn.r6companion.core.extenstions.getColor
import eamato.funn.r6companion.core.extenstions.getDimension

class BorderItemDecoration(
    @ColorRes borderColor: Int, @DimenRes borderWidth: Int,
    context: Context
) : RecyclerView.ItemDecoration() {

    private var borderWidth = -1
    private var paint: Paint? = null

    init {
        this.borderWidth = borderWidth.getDimension(context)

        paint = Paint().apply {
            color = borderColor.getColor(context)
            style = Paint.Style.STROKE
            strokeWidth = this@BorderItemDecoration.borderWidth.toFloat()
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (paint == null || borderWidth <= 0) {
            return
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            drawBorder(c, child)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect[borderWidth, borderWidth, borderWidth] = borderWidth
    }

    private fun drawBorder(canvas: Canvas, view: View) {
        val borderPaint = paint ?: return

        if (borderWidth <= 0) {
            return
        }

        val left = view.left - borderWidth / 2
        val top = view.top - borderWidth / 2
        val right = view.right + borderWidth / 2
        val bottom = view.bottom + borderWidth / 2

        canvas.drawRect(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            bottom.toFloat(),
            borderPaint
        )
    }
}