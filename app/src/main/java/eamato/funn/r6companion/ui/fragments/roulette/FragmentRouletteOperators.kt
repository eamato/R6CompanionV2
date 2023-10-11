package eamato.funn.r6companion.ui.fragments.roulette

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.OPERATORS_LIST_GRID_COUNT_LANDSCAPE
import eamato.funn.r6companion.core.OPERATORS_LIST_GRID_COUNT_PORTRAIT
import eamato.funn.r6companion.core.extenstions.getDimensionPixelSize
import eamato.funn.r6companion.core.extenstions.isLandscape
import eamato.funn.r6companion.core.extenstions.onTrueInvoke
import eamato.funn.r6companion.core.extenstions.removeAllItemDecorations
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.FragmentRouletteOperatorsBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterRouletteOperators
import eamato.funn.r6companion.ui.dialogs.DialogDefaultPopupManager
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.recyclerviews.decorations.BorderItemDecoration
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
import eamato.funn.r6companion.ui.viewmodels.roulette.RouletteOperatorsViewModel

@AndroidEntryPoint
class FragmentRouletteOperators : ABaseFragment<FragmentRouletteOperatorsBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentRouletteOperatorsBinding::inflate

    private val rouletteOperatorsViewModel: RouletteOperatorsViewModel by viewModels()

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        }

    private val dialogDefaultPopupManager: DialogDefaultPopupManager =
        DialogDefaultPopupManager(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)

        binding?.btnGoToRollResult?.setOnClickListener {
            findNavController().navigate(R.id.goToRollResult)
        }

        initOperatorsRecyclerView()
        setObservers()
        initSearchView()
        initSelectionOptions()
        initSortingOptions()
        applySystemInsetsInNeeded()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        dialogDefaultPopupManager.dismiss()

        binding?.rvOperators?.layoutManager = createOperatorsRecyclerViewLayoutManager()
        setOperatorsRecyclerViewItemDecorations()
    }

    private fun initOperatorsRecyclerView() {
        binding?.rvOperators?.run {
            setHasFixedSize(true)

            layoutManager = createOperatorsRecyclerViewLayoutManager()

            val adapterRouletteOperators = AdapterRouletteOperators { scrollToPosition(0) }
            adapter = adapterRouletteOperators

            setOperatorsRecyclerViewItemDecorations()

            val clickListener = RecyclerViewItemClickListener(
                this,
                object : RecyclerViewItemClickListener.OnItemTapListener {
                    override fun onItemClicked(view: View, position: Int) {
                        rouletteOperatorsViewModel.selectUnselectOperator(
                            adapterRouletteOperators.getItemAtPosition(position)
                        )
                    }
                }
            )

            setOnItemClickListener(clickListener)
        }
    }

    private fun createOperatorsRecyclerViewLayoutManager(): LayoutManager {
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
                .setTopSpacingMultiplier(R.dimen.dp_1.getDimensionPixelSize(context))
                .setBottomSpacingMultiplier(R.dimen.dp_11.getDimensionPixelSize(context))
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
        rouletteOperatorsViewModel.operators.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }

            showHideContentLoadingProgressBar(it is UiState.Progress)

            when (it) {
                is UiState.Error -> {}
                is UiState.Success -> {
                    binding?.rvOperators?.adapter
                        ?.let { adapter -> adapter as? AdapterRouletteOperators }
                        ?.submitList(it.data)

                    changeRollButton()
                }

                else -> {}
            }
        }
    }

    private fun initSearchView() {
        binding?.svOperators?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String?): Boolean {
                rouletteOperatorsViewModel.filterOperatorsByName(queryText ?: "")
                return true
            }

            override fun onQueryTextChange(queryText: String?): Boolean {
                rouletteOperatorsViewModel.filterOperatorsByName(queryText ?: "")
                return true
            }
        })
    }

    private fun initSelectionOptions() {
        binding?.btnSelectionOptions?.setOnClickListener {
            dialogDefaultPopupManager.create(it.context)
                .show(
                    childFragmentManager,
                    rouletteOperatorsViewModel.createSelectionPopupContentItems()
                )
        }
    }

    private fun initSortingOptions() {
        binding?.btnSortingOptions?.setOnClickListener {
            dialogDefaultPopupManager.create(it.context)
                .show(
                    childFragmentManager,
                    rouletteOperatorsViewModel.createSortingPopupContentItems()
                )
        }
    }

    private fun applySystemInsetsInNeeded() {
        binding?.run {
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                clHeaderButtons.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    leftMargin = insets.left
                    rightMargin = insets.right
                }

                WindowInsetsCompat.CONSUMED
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

    private fun handleBackPress() {
        if (binding?.svOperators?.isIconified == false) {
            binding?.svOperators?.isIconified = true
            return
        }
        onBackPressedCallback.isEnabled = false
        activity?.onBackPressedDispatcher?.onBackPressed()
    }

    private fun changeRollButton() {
        val selectedOperatorsCount = rouletteOperatorsViewModel.selectedOperatorsCount
        val canRoll = selectedOperatorsCount > 0
        binding?.btnGoToRollResult?.isEnabled = canRoll
        if (canRoll) {
            binding?.btnGoToRollResult?.text = getString(
                R.string.roll_counted_pattern,
                selectedOperatorsCount,
                rouletteOperatorsViewModel.allOperatorsCount
            )
        } else {
            binding?.btnGoToRollResult?.setText(R.string.roll)
        }
    }
}