package eamato.funn.r6companion.ui.fragments.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.databinding.FragmentCompanionWeaponsBinding
import eamato.funn.r6companion.ui.fragments.ABaseFragment

@AndroidEntryPoint
class FragmentCompanionWeapons : ABaseFragment<FragmentCompanionWeaponsBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding = FragmentCompanionWeaponsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCompanionButtons()
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

        binding?.buttons?.btnGoToWeapons?.isEnabled = false

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
}