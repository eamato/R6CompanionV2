package eamato.funn.r6companion.ui.fragments.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.SCREEN_NAME_IMAGE_VIEW
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.glide.IDoAfterTerminateGlide
import eamato.funn.r6companion.databinding.FragmentImageViewBinding
import eamato.funn.r6companion.ui.activities.ActivityMain
import eamato.funn.r6companion.ui.delegates.AnalyticsLogger
import eamato.funn.r6companion.ui.delegates.IAnalyticsLogger
import eamato.funn.r6companion.ui.fragments.ABaseFragment

class FragmentImageView : ABaseFragment<FragmentImageViewBinding>(),
    IAnalyticsLogger by AnalyticsLogger(FragmentImageView::class.java.simpleName, SCREEN_NAME_IMAGE_VIEW) {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentImageViewBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerLifecycleOwner(viewLifecycleOwner, view.context)

        val bundle = arguments ?: return
        val imageUrl = FragmentImageViewArgs.fromBundle(bundle).imageUrl

        val errorDrawable = R.drawable.no_image_drawable.getDrawable(requireContext())

        activity?.let { it as? ActivityMain }?.hideNavigation {
            binding?.ivImage?.run {
                GlideApp.with(this)
                    .asDrawable()
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(object : IDoAfterTerminateGlide {
                        override fun doAfterTerminate() {
                            binding?.clpbWaiting?.hide()
                        }
                    })
                    .error(errorDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(this)
            }
        }

        applySystemInsetsIfNeeded()
    }

    override fun onDestroy() {
        activity?.let { it as? ActivityMain }?.showNavigation()
        super.onDestroy()
    }

    private fun applySystemInsetsIfNeeded() {
        binding?.root?.applySystemInsetsIfNeeded { insets ->
            binding?.run {
                clpbWaiting.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                    leftMargin = insets.left
                    rightMargin = insets.right
                }
            }
        }
    }
}