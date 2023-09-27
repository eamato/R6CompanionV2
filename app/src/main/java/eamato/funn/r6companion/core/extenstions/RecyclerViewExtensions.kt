package eamato.funn.r6companion.core.extenstions

import androidx.recyclerview.widget.RecyclerView
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener

fun RecyclerView.setItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
    if (itemDecorationCount > 0) {
        for (index in 0..itemDecorationCount) {
            removeItemDecorationAt(index)
        }
    }

    addItemDecoration(itemDecoration)
}

fun RecyclerView?.setOnItemClickListener(listener: RecyclerViewItemClickListener) {
    if (this == null)
        return

    removeOnItemTouchListener(listener)
    addOnItemTouchListener(listener)
}
