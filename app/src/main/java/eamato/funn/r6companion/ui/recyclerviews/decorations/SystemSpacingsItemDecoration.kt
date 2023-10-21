package eamato.funn.r6companion.ui.recyclerviews.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SystemSpacingsItemDecoration(
    private val topSpacing: Int = 0,
    private val leftSpacing: Int = 0,
    private val bottomSpacing: Int = 0,
    private val rightSpacing: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            outRect.top = topSpacing
        }

        if (position == state.itemCount - 1) {
            outRect.bottom = bottomSpacing
        }

        outRect.right = rightSpacing
        outRect.left = leftSpacing
    }
}