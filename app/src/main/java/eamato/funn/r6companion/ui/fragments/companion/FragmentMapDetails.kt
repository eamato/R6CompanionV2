package eamato.funn.r6companion.ui.fragments.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.databinding.FragmentMapDetailsBinding
import eamato.funn.r6companion.domain.entities.companion.maps.MapDetails
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.viewmodels.companion.maps.CompanionMapDetailsViewModel

@AndroidEntryPoint
class FragmentMapDetails : ABaseFragment<FragmentMapDetailsBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentMapDetailsBinding::inflate

    private val companionMapDetailsViewModel: CompanionMapDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservables()
        applySystemInsetsIfNeeded()
    }

    private fun setObservables() {
        companionMapDetailsViewModel.mapDetails.observe(viewLifecycleOwner) { state ->
            if (state == null) {
                return@observe
            }

            showHideContentLoadingProgressBar(state is UiState.Progress)

            when (state) {
                is UiState.Error -> {}
                is UiState.Success -> {
                    showMapDetails(state.data)
                }

                else -> {}
            }
        }
    }

    private fun showMapDetails(data: MapDetails) {
        binding?.ivMapImage?.run {
            val errorDrawable = R.drawable.no_image_drawable.getDrawable(this.context)

            if (data.imageUrl == null) {
                this.setImageDrawable(errorDrawable)
            } else {
                GlideApp.with(this)
                    .asDrawable()
                    .load(data.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(errorDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(this)
            }
        }
    }

    private fun showHideContentLoadingProgressBar(show: Boolean = false) {
        if (show) {
            binding?.clpbWaiting?.show()
            return
        }

        binding?.clpbWaiting?.hide()
    }

    private fun applySystemInsetsIfNeeded() {
        binding?.root?.applySystemInsetsIfNeeded { insets ->
            binding?.run {
                clpbWaiting.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                    leftMargin = insets.left
                    rightMargin = insets.right
                }

                ivMapImage.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                    leftMargin = insets.left
                    rightMargin = insets.right
                }
            }
        }
    }
}