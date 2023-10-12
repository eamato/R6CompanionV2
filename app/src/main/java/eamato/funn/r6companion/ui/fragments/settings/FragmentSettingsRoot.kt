package eamato.funn.r6companion.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDimensionPixelSize
import eamato.funn.r6companion.core.extenstions.setItemDecoration
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.FragmentSettingsRootBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterSettingsItems
import eamato.funn.r6companion.ui.dialogs.DialogDefaultPopupManager
import eamato.funn.r6companion.ui.entities.settings.SettingsItem
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
import eamato.funn.r6companion.ui.viewmodels.settings.SettingsViewModel

@AndroidEntryPoint
class FragmentSettingsRoot : ABaseFragment<FragmentSettingsRootBinding>() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentSettingsRootBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        initSettingsRecyclerView()
    }

    private fun setObservers() {
        settingsViewModel.settingsChangedEvent.observe(viewLifecycleOwner) {
            settingsViewModel.initSettingsList()
        }

        settingsViewModel.settingsItems.observe(viewLifecycleOwner) { settingsItems ->
            binding?.rvSettings
                ?.adapter
                ?.let { it as? AdapterSettingsItems }
                ?.submitList(settingsItems)
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
                .setTopSpacingMultiplier(R.dimen.dp_7.getDimensionPixelSize(context))
                .create(context)
            setItemDecoration(spacingDecoration)

            val clickListener = RecyclerViewItemClickListener(
                this,
                object : RecyclerViewItemClickListener.OnItemTapListener {
                    override fun onItemClicked(view: View, position: Int) {
                        val selectedItem = adapterSettingsItems.getItemAtPosition(position)
                            .takeIf { it.isEnabled }
                        when (selectedItem) {
                            is SettingsItem.SettingsItemPopup -> {
                                DialogDefaultPopupManager.create(context)
                                    .show(childFragmentManager, selectedItem.popupContentItems)
                            }

                            is SettingsItem.SettingsItemScreen -> {
                                findNavController().navigate(
                                    selectedItem.destinationId,
                                    selectedItem.args
                                )
                            }

                            else -> {}
                        }
                    }
                }
            )

            setOnItemClickListener(clickListener)
        }
    }
}