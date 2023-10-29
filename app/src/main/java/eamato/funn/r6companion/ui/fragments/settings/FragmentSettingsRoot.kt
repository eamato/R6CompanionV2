package eamato.funn.r6companion.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.navigation.createGraph
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.SETTINGS_ITEM_SCREEN_FRAGMENT_TAG
import eamato.funn.r6companion.core.SETTINGS_ITEM_SCREEN_ROUTE_NAME
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.core.extenstions.isLandscape
import eamato.funn.r6companion.core.extenstions.isPortrait
import eamato.funn.r6companion.core.extenstions.replaceItemDecoration
import eamato.funn.r6companion.core.extenstions.setItemDecoration
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.SelectableObject
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.DialogDefaultAppPopupBinding
import eamato.funn.r6companion.databinding.FragmentSettingsRootBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterPopupContent
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterSettingsItems
import eamato.funn.r6companion.ui.dialogs.DialogDefaultPopupManager
import eamato.funn.r6companion.ui.entities.settings.SettingsItem
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
import eamato.funn.r6companion.ui.recyclerviews.decorations.SystemSpacingsItemDecoration
import eamato.funn.r6companion.ui.viewmodels.settings.SettingsViewModel

@AndroidEntryPoint
class FragmentSettingsRoot : ABaseFragment<FragmentSettingsRootBinding>() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentSettingsRootBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.setSelectedSettingsItem(null)

        setObservers()
        initSettingsRecyclerView()
        applySystemInsetsToListIfNeeded()
    }

    private fun setObservers() {
        settingsViewModel.settingsItems.observe(viewLifecycleOwner) { settingsItems ->
            DefaultAppLogger.getInstance().i(Message.message {
                clazz = this@FragmentSettingsRoot::class.java
                message = "settings items received, count = ${settingsItems?.size}"
            })
            binding?.rvSettings
                ?.adapter
                ?.let { it as? AdapterSettingsItems }
                ?.submitList(settingsItems)
        }

        settingsViewModel.clearContainerEvent.observe(viewLifecycleOwner) {
            if (requireContext().isLandscape()) {
                clearContainer()
            }
        }

        settingsViewModel.showContentForSettingsItem.observe(viewLifecycleOwner) { settingsItem ->
            if (requireContext().isLandscape()) {
                showSettingsItemContent(settingsItem)
            }
        }
    }

    private fun initSettingsRecyclerView() {
        binding?.rvSettings?.run {
            setHasFixedSize(true)

            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager

            val adapterSettingsItems = AdapterSettingsItems()
            adapter = adapterSettingsItems

            val spacingDecoration = SpacingItemDecoration
                .linear()
                .setSpacingRes(R.dimen.dp_2, R.dimen.dp_2, R.dimen.dp_2, R.dimen.dp_2)
                .create(context)
            setItemDecoration(spacingDecoration)

            val clickListener = RecyclerViewItemClickListener(
                this,
                object : RecyclerViewItemClickListener.OnItemTapListener {
                    override fun onItemClicked(view: View, position: Int) {
                        adapterSettingsItems.getItemAtPosition(position)
                            .takeIf { isSettingsItemSelectable(it) }
                            ?.run { onSettingsItemSelected(data) }
                    }
                }
            )

            setOnItemClickListener(clickListener)
        }
    }

    private fun applySystemInsetsToListIfNeeded() {
        binding?.root?.applySystemInsetsIfNeeded { insets ->
            val spacingDecoration = SystemSpacingsItemDecoration(
                insets.top, insets.left, insets.bottom, insets.right
            )
            binding?.rvSettings?.replaceItemDecoration(spacingDecoration)
            binding?.flSettingsItemContentContainer?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
                rightMargin = insets.right
            }
        }
    }

    private fun isSettingsItemSelectable(selectedSettingsItem: SelectableObject<SettingsItem>): Boolean {
        if (!selectedSettingsItem.data.isEnabled) {
            return false
        }

        if (requireContext().isPortrait()) {
            return true
        }

        return !selectedSettingsItem.isSelected
    }

    private fun onSettingsItemSelected(selectedSettingsItem: SettingsItem) {
        if (requireContext().isLandscape()) {
            settingsViewModel.setSelectedSettingsItem(selectedSettingsItem)
        }

        showSettingsItemContent(selectedSettingsItem)
    }

    private fun showSettingsItemContent(selectedSettingsItem: SettingsItem) {
        val context = requireContext()
        val isLandscape = context.isLandscape()

        when (selectedSettingsItem) {
            is SettingsItem.SettingsItemPopup -> {
                if (isLandscape) {
                    showSettingsItemPopupContentInContainer(selectedSettingsItem)
                } else {
                    showSettingsItemPopup(selectedSettingsItem)
                }
            }

            is SettingsItem.SettingsItemScreen -> {
                if (isLandscape) {
                    showSettingsItemScreenContent(selectedSettingsItem)
                } else {
                    goToSettingsItemScreen(selectedSettingsItem)
                }
            }

            else -> {}
        }
    }

    private fun showSettingsItemPopupContentInContainer(
        selectedSettingsItem: SettingsItem.SettingsItemPopup
    ) {
        binding?.flSettingsItemContentContainer?.run {
            val popupBinding = DialogDefaultAppPopupBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
            )
            popupBinding.rvPopupItems.layoutManager = LinearLayoutManager(context)
            val adapterPopupContent = AdapterPopupContent()
            popupBinding.rvPopupItems.adapter = adapterPopupContent
                .also { adapter -> adapter.submitList(selectedSettingsItem.popupContentItems) }

            val spacingDecoration = SpacingItemDecoration
                .linear()
                .setSpacingRes(R.dimen.dp_2, R.dimen.dp_2, R.dimen.dp_2, R.dimen.dp_2)
                .create(context)
            popupBinding.rvPopupItems.setItemDecoration(spacingDecoration)

            val clickListener = RecyclerViewItemClickListener(
                popupBinding.rvPopupItems,
                object : RecyclerViewItemClickListener.OnItemTapListener {
                    override fun onItemClicked(view: View, position: Int) {
                        adapterPopupContent.getItemAtPosition(position).onClickListener?.invoke()
                    }
                }
            )

            popupBinding.rvPopupItems.setOnItemClickListener(clickListener)
        }
    }

    private fun showSettingsItemPopup(selectedSettingsItem: SettingsItem.SettingsItemPopup) {
        val context = requireContext()
        DialogDefaultPopupManager.create(context)
            .show(childFragmentManager, selectedSettingsItem.popupContentItems)
    }

    private fun showSettingsItemScreenContent(selectedSettingsItem: SettingsItem.SettingsItemScreen) {
        val navHostFragment = NavHostFragment()
        childFragmentManager
            .beginTransaction()
            .replace(
                R.id.fl_settings_item_content_container,
                navHostFragment,
                SETTINGS_ITEM_SCREEN_FRAGMENT_TAG
            )
            .runOnCommit {
                val navigator = FragmentNavigator(
                    requireContext(),
                    childFragmentManager,
                    R.id.fl_settings_item_content_container
                )
                val destinationBuilder = FragmentNavigatorDestinationBuilder(
                    navigator,
                    SETTINGS_ITEM_SCREEN_ROUTE_NAME,
                    selectedSettingsItem.destinationClass
                )
                val graph = navHostFragment.navController.createGraph(
                    startDestination = SETTINGS_ITEM_SCREEN_ROUTE_NAME,
                    builder = { destination(destinationBuilder) }
                )
                navHostFragment.navController.setGraph(graph, selectedSettingsItem.args)
            }
            .commit()
    }

    private fun goToSettingsItemScreen(selectedSettingsItem: SettingsItem.SettingsItemScreen) {
        findNavController().navigate(selectedSettingsItem.destinationId, selectedSettingsItem.args)
    }

    private fun clearContainer() {
        val currentSettingsItemScreenFragment = childFragmentManager
            .findFragmentByTag(SETTINGS_ITEM_SCREEN_FRAGMENT_TAG)
        if (currentSettingsItemScreenFragment != null) {
            childFragmentManager
                .beginTransaction()
                .remove(currentSettingsItemScreenFragment)
                .commitNow()
        }

        binding?.flSettingsItemContentContainer?.removeAllViews()
    }
}