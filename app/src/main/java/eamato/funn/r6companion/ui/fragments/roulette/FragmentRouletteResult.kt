package eamato.funn.r6companion.ui.fragments.roulette

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.WINNER_OPERATOR_IMAGE_HEIGHT
import eamato.funn.r6companion.core.WINNER_OPERATOR_IMAGE_WIDTH
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.glide.IDoAfterTerminateGlide
import eamato.funn.r6companion.core.glide.ImageResizeTransformation
import eamato.funn.r6companion.databinding.FragmentRouletteResultBinding
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.viewmodels.roulette.RollResultViewModel

@AndroidEntryPoint
class FragmentRouletteResult : ABaseFragment<FragmentRouletteResultBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentRouletteResultBinding::inflate

    private val rollResultViewModel: RollResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
    }

    private fun setObservers() {
        rollResultViewModel.winnerOperator.observe(viewLifecycleOwner) { winner ->
            binding?.run {
                GlideApp.with(ivWinnerImage)
                    .load(winner.imgLink)
                    .override(WINNER_OPERATOR_IMAGE_WIDTH, WINNER_OPERATOR_IMAGE_HEIGHT)
                    .transform(ImageResizeTransformation(
                        WINNER_OPERATOR_IMAGE_WIDTH,
                        WINNER_OPERATOR_IMAGE_HEIGHT
                    ))
                    .placeholder(R.drawable.transparent_300)
                    .error(R.drawable.no_image_drawable)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(object : IDoAfterTerminateGlide {
                        override fun doAfterTerminate() {

                        }
                    })
                    .dontAnimate()
                    .into(ivWinnerImage)

                tvWinnerName.text = winner.name

                btnShowOperatorDetails.setOnClickListener {
                    val uri = Uri.parse("android-app://eamato.funn.r6companion/operator/details/${winner.id}")
                    findNavController().navigate(uri)
                }
            }
        }
    }
}