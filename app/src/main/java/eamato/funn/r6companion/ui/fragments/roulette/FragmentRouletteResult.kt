package eamato.funn.r6companion.ui.fragments.roulette

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.databinding.FragmentRouletteResultBinding
import eamato.funn.r6companion.ui.fragments.ABaseFragment

@AndroidEntryPoint
class FragmentRouletteResult : ABaseFragment<FragmentRouletteResultBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding = FragmentRouletteResultBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.hui?.setOnClickListener {
            val oper = FragmentRouletteResultArgs.fromBundle(requireArguments()).rollCandidates.first()
            val uri = Uri.parse("android-app://eamato.funn.r6companion/operator/details/${oper.id}")
            findNavController().navigate(uri)
        }
    }
}