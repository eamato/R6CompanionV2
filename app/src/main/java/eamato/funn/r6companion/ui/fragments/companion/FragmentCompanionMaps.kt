package eamato.funn.r6companion.ui.fragments.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.MAPS_LIST_GRID_COUNT_LANDSCAPE
import eamato.funn.r6companion.core.MAPS_LIST_GRID_COUNT_PORTRAIT
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.core.extenstions.isLandscape
import eamato.funn.r6companion.core.extenstions.onTrueInvoke
import eamato.funn.r6companion.core.extenstions.setItemDecoration
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.databinding.FragmentCompanionMapsBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterCompanionMaps
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
import eamato.funn.r6companion.ui.viewmodels.companion.maps.CompanionMapsViewModel

@AndroidEntryPoint
class FragmentCompanionMaps : ABaseFragment<FragmentCompanionMapsBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentCompanionMapsBinding::inflate

    private val companionMapsViewModel: CompanionMapsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCompanionButtons()
        initMapsRecyclerView()
        setObservables()
        applySystemInsetsIfNeeded()
    }

    private fun initCompanionButtons() {
        binding?.buttons?.btnGoToOperators?.setOnClickListener {
            findNavController().navigate(
                resId = R.id.FragmentCompanionOperators,
                args = null,
                navOptions = navOptions {
                    popUpTo(R.id.companion_navigation) {
                        inclusive = true
                    }
                }
            )
        }

        binding?.buttons?.btnGoToWeapons?.setOnClickListener {
            findNavController().navigate(
                resId = R.id.FragmentCompanionWeapons,
                args = null,
                navOptions = navOptions {
                    popUpTo(R.id.companion_navigation) {
                        inclusive = true
                    }
                }
            )
        }

        binding?.buttons?.btnGoToMaps?.isEnabled = false
    }

    private fun setObservables() {
        companionMapsViewModel.maps.observe(viewLifecycleOwner) { state ->
            if (state == null) {
                return@observe
            }

            showHideContentLoadingProgressBar(state is UiState.Progress)

            when (state) {
                is UiState.Error -> {}
                is UiState.Success -> {
                    binding?.rvMaps?.adapter
                        ?.let { adapter -> adapter as? AdapterCompanionMaps }
                        ?.submitList(state.data)
                }

                else -> {}
            }
        }
    }

    private fun initMapsRecyclerView() {
        binding?.rvMaps?.run {
            setHasFixedSize(true)

            layoutManager = createMapsRecyclerViewLayoutManager()

            val adapterCompanionMaps = AdapterCompanionMaps()
            adapter = adapterCompanionMaps

            setMapsRecyclerViewItemDecorations()
        }
    }

    private fun createMapsRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val spanCount = context
            .isLandscape()
            .onTrueInvoke { MAPS_LIST_GRID_COUNT_LANDSCAPE }
            ?: MAPS_LIST_GRID_COUNT_PORTRAIT

        return GridLayoutManager(context, spanCount)
    }

    private fun setMapsRecyclerViewItemDecorations() {
        binding?.rvMaps?.run {
            val spacingDecoration = SpacingItemDecoration
                .linear()
                .setSpacingRes(R.dimen.dp_4, R.dimen.dp_4, R.dimen.dp_4, R.dimen.dp_4)
                .create(context)

            setItemDecoration(spacingDecoration)
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
            binding?.clHeaderButtons?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
                leftMargin = insets.left
                rightMargin = insets.right
            }
        }
    }
}