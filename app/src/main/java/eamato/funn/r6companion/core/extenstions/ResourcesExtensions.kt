package eamato.funn.r6companion.core.extenstions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

fun Int.getDimension(context: Context): Int {
    return context.resources.getDimension(this).toInt()
}

fun Int.getDimensionPixelSize(context: Context): Int {
    return context.resources.getDimensionPixelSize(this)
}

@ColorInt
fun Int.getColor(context: Context): Int {
    return ContextCompat.getColor(context, this)
}

fun Int.getDrawable(context: Context, width: Int, height: Int): Drawable? {
    return ContextCompat.getDrawable(context, this)
        ?.apply { setBounds(0, 0, width, height) }
}

private val drawableCache = mutableMapOf<Int, Drawable>()

fun Int.getDrawable(context: Context): Drawable? {
    var drawable = drawableCache[this]
    if (drawable != null) {
        return drawable
    }
    drawable = ContextCompat.getDrawable(context, this)
    if (drawable != null) {
        drawableCache[this] = drawable
    }

    return drawable
}