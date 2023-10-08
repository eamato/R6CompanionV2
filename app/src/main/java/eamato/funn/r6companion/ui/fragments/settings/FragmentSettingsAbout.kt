package eamato.funn.r6companion.ui.fragments.settings

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.databinding.FragmentSettingsAboutBinding
import eamato.funn.r6companion.ui.fragments.ABaseFragment

@AndroidEntryPoint
class FragmentSettingsAbout : ABaseFragment<FragmentSettingsAboutBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentSettingsAboutBinding::inflate


}