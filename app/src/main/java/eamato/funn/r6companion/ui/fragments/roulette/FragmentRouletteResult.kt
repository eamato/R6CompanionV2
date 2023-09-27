package eamato.funn.r6companion.ui.fragments.roulette

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.databinding.FragmentRouletteResultBinding
import eamato.funn.r6companion.ui.fragments.ABaseFragment

@AndroidEntryPoint
class FragmentRouletteResult : ABaseFragment<FragmentRouletteResultBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding = FragmentRouletteResultBinding::inflate
}