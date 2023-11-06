package eamato.funn.r6companion.ui.fragments.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.databinding.FragmentImageViewBinding
import eamato.funn.r6companion.ui.activities.ActivityMain
import eamato.funn.r6companion.ui.fragments.ABaseFragment

class FragmentImageView : ABaseFragment<FragmentImageViewBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentImageViewBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { it as? ActivityMain }?.hideNavigation()

        val bundle = arguments ?: return
        val imageUrl = FragmentImageViewArgs.fromBundle(bundle).imageUrl

        val errorDrawable = R.drawable.no_image_drawable.getDrawable(requireContext())

        binding?.ivImage?.run {
            GlideApp.with(this)
                .asDrawable()
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(errorDrawable)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(this)
        }
    }

    override fun onDestroy() {
        activity?.let { it as? ActivityMain }?.showNavigation()
        super.onDestroy()
    }
}