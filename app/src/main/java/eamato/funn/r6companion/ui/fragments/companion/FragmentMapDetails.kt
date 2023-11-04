package eamato.funn.r6companion.ui.fragments.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.extenstions.setItemDecoration
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.ChipTextViewBinding
import eamato.funn.r6companion.databinding.FragmentMapDetailsBinding
import eamato.funn.r6companion.domain.entities.companion.maps.MapDetails
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterMapBlueprints
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
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
                    binding?.tvBlueprints?.visibility = View.VISIBLE
                    showMapDetails(state.data)
                }

                else -> {
                    binding?.tvBlueprints?.visibility = View.GONE
                }
            }
        }
    }

    private fun showMapDetails(data: MapDetails) {
        binding?.run {
            ivMapImage.run {
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

            tvMapName.text = data.name
            tvMapContent.text = data.content

            rvMapBlueprints.run {
                setHasFixedSize(true)

                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

                val adapterMapBlueprints = AdapterMapBlueprints()
                adapter = adapterMapBlueprints
                adapterMapBlueprints.submitList(data.blueprints)

                val spacingDecoration = SpacingItemDecoration
                    .linear()
                    .setSpacingRes(
                        R.dimen.dp_5,
                        R.dimen.dp_5,
                        R.dimen.dp_5,
                        R.dimen.dp_5,
                    )
                    .create(context)
                setItemDecoration(spacingDecoration)

                PagerSnapHelper().attachToRecyclerView(this)

                val clickListener = RecyclerViewItemClickListener(
                    this,
                    object : RecyclerViewItemClickListener.OnItemTapListener {
                        override fun onItemClicked(view: View, position: Int) {
                            val selectedBlueprint = adapterMapBlueprints.getItemAtPosition(position)
                            val imageUrl = selectedBlueprint.imageUrl ?: return

                            findNavController().navigate(
                                R.id.FragmentImageView,
                                FragmentImageViewArgs(imageUrl).toBundle()
                            )
                        }
                    }
                )

                setOnItemClickListener(clickListener)
            }

            flowMapPlaylists.run {
                val layoutInflater = LayoutInflater.from(context)
                val ids = data.playlists
                    .map { playlist ->
                        playlist.replaceFirstChar { char -> char.uppercaseChar() }
                    }
                    .map { playlist ->
                        ChipTextViewBinding.inflate(layoutInflater).root
                            .apply {
                                id = View.generateViewId()
                                text = playlist
                                binding?.clContent?.addView(this)
                            }.id
                    }
                referencedIds = ids.toIntArray()
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

                clContent.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                    leftMargin = insets.left
                    rightMargin = insets.right
                }
            }
        }
    }
}