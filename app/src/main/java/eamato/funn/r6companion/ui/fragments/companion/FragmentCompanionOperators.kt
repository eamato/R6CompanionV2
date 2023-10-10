package eamato.funn.r6companion.ui.fragments.companion

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDimension
import eamato.funn.r6companion.core.extenstions.isLandscape
import eamato.funn.r6companion.core.extenstions.onTrueInvoke
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.extenstions.setViewsEnabled
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.FragmentCompanionOperatorsBinding
import eamato.funn.r6companion.domain.entities.companion.operators.Operator
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

    private val dialogDefaultPopupManager: DialogDefaultPopupManager =
        DialogDefaultPopupManager(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)

        initCompanionButtons()
        initFilterOptions()
        initOperatorsRecyclerView()
        setObservers()
        initSearchView()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        dialogDefaultPopupManager.dismiss()
    }

    private fun initCompanionButtons() {
        binding?.buttons?.btnGoToOperators?.isEnabled = false

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

        binding?.buttons?.btnGoToMaps?.setOnClickListener {
            findNavController().navigate(
                resId = R.id.FragmentCompanionMaps,
                args = null,
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

            val spanCount = context.isLandscape().onTrueInvoke { 5 } ?: 3
            val operatorsLayoutManager = GridLayoutManager(context, spanCount)
            layoutManager = operatorsLayoutManager

            val adapterCompanionOperators = AdapterCompanionOperators { scrollToPosition(0) }
            adapter = adapterCompanionOperators

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
                    R.dimen.dp_1.getDimension(context)
                )
                .setBottomSpacingMultiplier(
                    R.dimen.dp_1.getDimension(context)
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
                    submitOperators(it.data)
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

    private fun initFilterOptions() {
        binding?.btnFilterOptions?.setOnClickListener {
            dialogDefaultPopupManager.create(it.context)
                .show(
                    childFragmentManager,
                    companionOperatorsViewModel.createFilterPopupContentItems()
                )
        }
    }

    private fun submitOperators(operators: List<Operator>) {
        binding?.rvOperators?.adapter
            ?.let { adapter -> adapter as? AdapterCompanionOperators }
            ?.submitList(operators)
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