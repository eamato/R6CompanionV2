package eamato.funn.r6companion.ui.fragments.roulette

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDimensionPixelSize
import eamato.funn.r6companion.core.extenstions.isLandscape
import eamato.funn.r6companion.core.extenstions.onTrueInvoke
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.FragmentRouletteOperatorsBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterRouletteOperators
import eamato.funn.r6companion.ui.dialogs.DialogDefaultAppPopup
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
    }

    private fun initOperatorsRecyclerView() {
        binding?.rvOperators?.run {
            setHasFixedSize(true)

            val spanCount = context.isLandscape().onTrueInvoke { 5 } ?: 3
            val operatorsLayoutManager = GridLayoutManager(context, spanCount)
            layoutManager = operatorsLayoutManager

            val adapterRouletteOperators = AdapterRouletteOperators { scrollToPosition(0) }
            adapter = adapterRouletteOperators

            val spacingDecoration = SpacingItemDecoration
                .grid()
                .setIncludeEdge(true)
                .setSpanCount(spanCount)
                .setSpacingRes(
                    R.dimen.dp_2,
                    R.dimen.dp_2,
                    R.dimen.dp_2,
                    R.dimen.dp_2,
                )
                .setTopSpacingMultiplier(
                    R.dimen.dp_1.getDimensionPixelSize(context)
                )
                .setBottomSpacingMultiplier(
                    R.dimen.dp_11.getDimensionPixelSize(context)
                )
                .create(context)
            addItemDecoration(spacingDecoration)

            val borderDecorations = BorderItemDecoration(
                R.color.operators_border,
                R.dimen.dp_1,
                context
            )
            addItemDecoration(borderDecorations)

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
            DialogDefaultAppPopup(rouletteOperatorsViewModel.createSelectionPopupContentItems())
                .show(childFragmentManager)
        }
    }

    private fun initSortingOptions() {
        binding?.btnSortingOptions?.setOnClickListener {
            DialogDefaultAppPopup(rouletteOperatorsViewModel.createSortingPopupContentItems())
                .show(childFragmentManager)
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