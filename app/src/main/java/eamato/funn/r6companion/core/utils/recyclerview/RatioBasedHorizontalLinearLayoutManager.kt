package eamato.funn.r6companion.core.utils.recyclerview

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RatioBasedHorizontalLinearLayoutManager(
    context: Context,
    private val ratio: Int
) : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        if (lp == null) {
            return false
        }
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        lp.width = screenWidth - screenWidth / ratio
        return true
    }
}