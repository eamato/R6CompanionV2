package eamato.funn.r6companion.ui.entities.news.details

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp

class ContentViewImage(
    private val imageUrl: String
) : AContentView() {

    private var imageView: ImageView? = null

    override fun onCreateView(parent: ViewGroup): View {
        return ImageView(parent.context)
            .apply {
                GlideApp.with(this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .error(R.drawable.no_image_drawable.getDrawable(context))
                    .dontAnimate()
                    .into(this)

                layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 50, 0, 0) }

                imageView = this
            }
    }

    override fun onDestroy() {
        imageView?.setImageDrawable(null)
    }
}