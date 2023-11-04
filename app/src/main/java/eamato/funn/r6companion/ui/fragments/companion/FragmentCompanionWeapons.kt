package eamato.funn.r6companion.ui.fragments.companion

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.COMPANION_BUTTONS_ANIMATION_DURATION
import eamato.funn.r6companion.core.COMPANION_SCREEN_ID_MAPS
import eamato.funn.r6companion.core.COMPANION_SCREEN_ID_OPERATORS
import eamato.funn.r6companion.core.COMPANION_SCREEN_ID_WEAPONS
import eamato.funn.r6companion.core.PROPERTY_NAME_TEXT_SIZE
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.databinding.FragmentCompanionWeaponsBinding
import eamato.funn.r6companion.ui.fragments.ABaseFragment

@AndroidEntryPoint
class FragmentCompanionWeapons : ABaseFragment<FragmentCompanionWeaponsBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding = FragmentCompanionWeaponsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCompanionButtons()
        applySystemInsetsIfNeeded()
    }

    private fun initCompanionButtons() {
        val transactionCallerId = arguments
            ?.let { FragmentCompanionWeaponsArgs.fromBundle(it) }
            ?.transactionCallerId
            ?: -1

        when (transactionCallerId) {
            COMPANION_SCREEN_ID_OPERATORS -> {
                binding?.buttons?.btnGoToOperators?.run {
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

        binding?.buttons?.btnGoToOperators?.setOnClickListener {
            findNavController().navigate(
                resId = R.id.FragmentCompanionOperators,
                args = FragmentCompanionOperatorsArgs(COMPANION_SCREEN_ID_WEAPONS).toBundle(),
                navOptions = navOptions {
                    popUpTo(R.id.companion_navigation) {
                        inclusive = true
                    }
                }
            )
        }

        binding?.buttons?.btnGoToWeapons?.run {
            isEnabled = false
            ObjectAnimator.ofFloat(this, PROPERTY_NAME_TEXT_SIZE, 12f, 15f)
                .apply {
                    duration = COMPANION_BUTTONS_ANIMATION_DURATION
                    start()
                }
        }

        binding?.buttons?.btnGoToMaps?.setOnClickListener {
            findNavController().navigate(
                resId = R.id.FragmentCompanionMaps,
                args = FragmentCompanionMapsArgs(COMPANION_SCREEN_ID_WEAPONS).toBundle(),
                navOptions = navOptions {
                    popUpTo(R.id.companion_navigation) {
                        inclusive = true
                    }
                }
            )
        }
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