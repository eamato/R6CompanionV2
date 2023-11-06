package eamato.funn.r6companion.ui.fragments.companion

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.COMPANION_BUTTONS_ANIMATION_DURATION
import eamato.funn.r6companion.core.COMPANION_SCREEN_ID_MAPS
import eamato.funn.r6companion.core.COMPANION_SCREEN_ID_OPERATORS
import eamato.funn.r6companion.core.COMPANION_SCREEN_ID_WEAPONS
import eamato.funn.r6companion.core.OPERATORS_LIST_GRID_COUNT_LANDSCAPE
import eamato.funn.r6companion.core.OPERATORS_LIST_GRID_COUNT_PORTRAIT
import eamato.funn.r6companion.core.PROPERTY_NAME_TEXT_SIZE
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.core.extenstions.getDimension
import eamato.funn.r6companion.core.extenstions.isLandscape
import eamato.funn.r6companion.core.extenstions.onTrueInvoke
import eamato.funn.r6companion.core.extenstions.removeAllItemDecorations
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.extenstions.setViewsEnabled
import eamato.funn.r6companion.core.utils.ScrollToTopAdditionalEvent
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.FragmentCompanionOperatorsBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterCompanionOperators
import eamato.funn.r6companion.ui.dialogs.DialogDefaultPopupManager
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.recyclerviews.decorations.BorderItemDecoration
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
import eamato.funn.r6companion.ui.viewmodels.companion.operators.CompanionOperatorsViewModel

@AndroidEntryPoint
class FragmentCompanionOperators : ABaseFragment<FragmentCompanionOperatorsBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentCompanionOperatorsBinding::inflate

    private val companionOperatorsViewModel: CompanionOperatorsViewModel by viewModels()

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)

        initCompanionButtons()
        initFilterOptions()
        initOperatorsRecyclerView()
        setObservers()
        initSearchView()
        applySystemInsetsIfNeeded()
    }

    private fun initCompanionButtons() {
        val transactionCallerId = arguments
            ?.let { FragmentCompanionOperatorsArgs.fromBundle(it) }
            ?.transactionCallerId
            ?: -1

        when (transactionCallerId) {
            COMPANION_SCREEN_ID_WEAPONS -> {
                binding?.buttons?.btnGoToWeapons?.run {
                    ObjectAnimator.ofFloat(this, PROPERTY_NAME_TEXT_SIZE, 15f, 12f)
                        .apply {
                            duration = COMPANION_BUTTONS_ANIMATION_DURATION
                            start()
                        }
                }
            }
            COMPANION_SCREEN_ID_MAPS -> {
                binding?.buttons?.btnGoToMaps?.run {
                    ObjectAnimator.ofFloat(this, PROPERTY_NAME_TEXT_SIZE, 15f, 12f)
                        .apply {
                            duration = COMPANION_BUTTONS_ANIMATION_DURATION
                            start()
                        }
                }
            }
        }

        binding?.buttons?.btnGoToOperators?.run {
            isEnabled = false
            ObjectAnimator.ofFloat(this, PROPERTY_NAME_TEXT_SIZE, 12f, 15f)
                .apply {
                    duration = COMPANION_BUTTONS_ANIMATION_DURATION
                    start()
                }
        }

        binding?.buttons?.btnGoToWeapons?.setOnClickListener {
            findNavController().navigate(
                resId = R.id.FragmentCompanionWeapons,
                args = FragmentCompanionWeaponsArgs(COMPANION_SCREEN_ID_OPERATORS).toBundle(),
                navOptions = navOptions {
                    popUpTo(R.id.companion_navigation) {
                        inclusive = true
                    }
                }
            )
        }

        binding?.buttons?.btnGoToMaps?.setOnClickListener {
            findNavController().navigate(
                resId = R.id.FragmentCompanionMaps,
                args = FragmentCompanionMapsArgs(COMPANION_SCREEN_ID_OPERATORS).toBundle(),
                navOptions = navOptions {
                    popUpTo(R.id.companion_navigation) {
                        inclusive = true
                    }
                }
            )
        }
    }

    private fun initOperatorsRecyclerView() {
        binding?.rvOperators?.run {
            setHasFixedSize(true)

            layoutManager = createOperatorsRecyclerViewLayoutManager()

            val adapterCompanionOperators = AdapterCompanionOperators()
            adapter = adapterCompanionOperators

            setOperatorsRecyclerViewItemDecorations()

            val clickListener = RecyclerViewItemClickListener(
                this,
                object : RecyclerViewItemClickListener.OnItemTapListener {
                    override fun onItemClicked(view: View, position: Int) {
                        val selectedOperator = adapterCompanionOperators.getItemAtPosition(position)

                        findNavController().navigate(
                            R.id.FragmentOperatorDetails,
                            FragmentOperatorDetailsArgs(selectedOperator).toBundle()
                        )
                    }
                }
            )

            setOnItemClickListener(clickListener)
        }
    }

    private fun createOperatorsRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val spanCount = context
            .isLandscape()
            .onTrueInvoke { OPERATORS_LIST_GRID_COUNT_LANDSCAPE }
            ?: OPERATORS_LIST_GRID_COUNT_PORTRAIT

        return GridLayoutManager(context, spanCount)
    }

    private fun setOperatorsRecyclerViewItemDecorations() {
        binding?.rvOperators?.run {
            removeAllItemDecorations()

            val spanCount = layoutManager
                ?.let { layoutManager -> layoutManager as? GridLayoutManager }
                ?.spanCount
                ?: context.isLandscape().onTrueInvoke { OPERATORS_LIST_GRID_COUNT_LANDSCAPE }
                ?: OPERATORS_LIST_GRID_COUNT_PORTRAIT

            val spacingDecoration = SpacingItemDecoration
                .grid()
                .setIncludeEdge(true)
                .setSpanCount(spanCount)
                .setSpacingRes(R.dimen.dp_2, R.dimen.dp_2, R.dimen.dp_2, R.dimen.dp_2)
                .setTopSpacingMultiplier(R.dimen.dp_1.getDimension(context))
                .setBottomSpacingMultiplier(R.dimen.dp_1.getDimension(context))
                .create(context)
            addItemDecoration(spacingDecoration)

            val borderDecorations = BorderItemDecoration(
                R.color.operators_border,
                R.dimen.dp_1,
                context
            )
            addItemDecoration(borderDecorations)
        }
    }

    private fun setObservers() {
        companionOperatorsViewModel.operators.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }

            showHideContentLoadingProgressBar(it is UiState.Progress)
            disableEnableViews(it is UiState.Progress)

            when (it) {
                is UiState.Error -> {}
                is UiState.Success -> {
                    val adapter = binding?.rvOperators?.adapter
                        ?.let { adapter -> adapter as? AdapterCompanionOperators }
                        ?: return@observe

                    when (it.additionalEvent) {
                        is ScrollToTopAdditionalEvent -> {
                            adapter.submitList(it.data) {
                                binding?.rvOperators?.scrollToPosition(0)
                            }
                        }
                        else -> adapter.submitList(it.data)
                    }
                }

                else -> {}
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

    private fun disableEnableViews(isProgress: Boolean = true) {
        binding?.svOperators?.setViewsEnabled(isProgress.not())
        binding?.btnFilterOptions?.isEnabled = isProgress.not()
    }

    private fun initSearchView() {
        binding?.svOperators?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String?): Boolean {
                companionOperatorsViewModel.filterOperatorsByName(queryText ?: "")
                return true
            }

            override fun onQueryTextChange(queryText: String?): Boolean {
                companionOperatorsViewModel.filterOperatorsByName(queryText ?: "")
                return true
            }
        })
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

    private fun initFilterOptions() {
        binding?.btnFilterOptions?.setOnClickListener {
            DialogDefaultPopupManager.create(it.context)
                .show(
                    childFragmentManager,
                    companionOperatorsViewModel.createFilterPopupContentItems()
                )
        }
    }

    private fun handleBackPress() {
        if (binding?.svOperators?.isIconified == false) {
            binding?.svOperators?.isIconified = true
            return
        }
        onBackPressedCallback.isEnabled = false
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}