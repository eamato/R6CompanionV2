package eamato.funn.r6companion.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import eamato.funn.r6companion.ui.viewmodels.MainNavigationViewModel

abstract class ABaseFragment<VB : ViewBinding> : Fragment() {

    protected val mainNavigationViewModel: MainNavigationViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    protected var binding: VB? = null
        private set

    abstract val bindingInitializer: (LayoutInflater) -> ViewBinding

    @CallSuper
    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindingInitializer(layoutInflater).also { binding = it as? VB }.root

    @CallSuper
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}