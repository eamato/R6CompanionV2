package eamato.funn.r6companion.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.BuildConfig
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.databinding.FragmentSettingsAboutBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterOurTeam
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.viewmodels.settings.AboutViewModel

@AndroidEntryPoint
class FragmentSettingsAbout : ABaseFragment<FragmentSettingsAboutBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentSettingsAboutBinding::inflate

    private val aboutViewModel: AboutViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.tvLicence?.text = getString(
            R.string.licence_text_pattern,
            getString(R.string.app_name)
        )
        initOurTeamRecyclerView()
        applySystemInsetsIfNeeded()
        setObservers()
    }

    private fun initOurTeamRecyclerView() {
        binding?.rvOurTeam?.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val adapterOurTeam = AdapterOurTeam()
            adapter = adapterOurTeam

            PagerSnapHelper().attachToRecyclerView(this)
        }
    }

    private fun setObservers() {
        aboutViewModel.aboutInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Error -> {
                    showError(state.error)
                }
                is UiState.Success -> {
                    binding?.run {
                        val ourMissionText = state.data.ourMission.missionText
                            .takeIf { missionText -> missionText.isNotEmpty() }
                            ?: getString(R.string.our_mission_default_value)
                        tvOurMissionValue.text = ourMissionText
                        val versionText = getString(
                            R.string.version_pattern,
                            BuildConfig.VERSION_CODE,
                            BuildConfig.VERSION_NAME
                        )
                        tvAppVersion.text = versionText
                        rvOurTeam.adapter
                            ?.let { adapter -> adapter as? AdapterOurTeam }
                            ?.submitList(state.data.ourTeam.teamMembers)
                    }
                }

                else -> {}
            }
        }
    }

    private fun applySystemInsetsIfNeeded() {
        binding?.root?.applySystemInsetsIfNeeded { insets ->
            binding?.run {
                clContent.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = insets.top
                    leftMargin = insets.left
                    rightMargin = insets.right
                }
            }
        }
    }
}