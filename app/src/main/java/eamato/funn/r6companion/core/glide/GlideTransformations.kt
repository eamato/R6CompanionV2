package eamato.funn.r6companion.core.glide

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import eamato.funn.r6companion.core.NEGATIVE
import java.security.MessageDigest


class ImageResizeTransformation(
    private val resultWidth: Int,
    private val resultHeight: Int
) : BitmapTransformation() {

    private val ID = "eamato.funn.r6companion.utils.glide.ImageResizeTransformation"
    private val ID_BYTES = ID.toByteArray()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return Bitmap.createBitmap(toTransform, 0, 0, resultWidth, resultHeight)
    }

    override fun equals(other: Any?): Boolean {
        return other is ImageResizeTransformation
    }

    override fun hashCode() = ID.hashCode()
}

class ImageInverseColorsTransformation : BitmapTransformation() {

    private val ID = "eamato.funn.r6companion.core.glide.ImageInverseColorsTransformation"
    private val ID_BYTES = ID.toByteArray()

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val bitmap = pool[toTransform.width, toTransform.height, toTransform.config]
        bitmap.density = toTransform.density

        val paint = Paint()
        paint.isAntiAlias = true
        paint.colorFilter = ColorMatrixColorFilter(ColorMatrix(NEGATIVE))

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        return bitmap
    }

    override fun equals(other: Any?): Boolean {
        return other is ImageInverseColorsTransformation
    }

    override fun hashCode() = ID.hashCode()
}