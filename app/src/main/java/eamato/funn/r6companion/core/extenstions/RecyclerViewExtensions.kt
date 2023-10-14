package eamato.funn.r6companion.core.extenstions

import androidx.recyclerview.widget.RecyclerView
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener

fun RecyclerView.removeAllItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(itemDecorationCount.dec())
    }
}

fun RecyclerView.setItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
    removeAllItemDecorations()

    addItemDecoration(itemDecoration)
}

fun RecyclerView.replaceItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
    for (index in 0 until itemDecorationCount) {
        if (getItemDecorationAt(index)::class.java == itemDecoration::class.java) {
            removeItemDecorationAt(index)
            break
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
