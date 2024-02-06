package eamato.funn.r6companion.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getColor
import eamato.funn.r6companion.ui.viewmodels.MainNavigationViewModel

abstract class ABaseFragment<VB : ViewBinding> : Fragment() {

    protected val mainNavigationViewModel: MainNavigationViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    protected var binding: VB? = null
        private set

    protected abstract val bindingInitializer: (LayoutInflater) -> ViewBinding

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

    protected fun showError(error: Throwable, anchorView: View? = null) {
        val view = binding?.root ?: return
        val message = error.message ?: return

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .apply {
            setBackgroundTint(R.color.colorPrimary.getColor(view.context))
            setTextColor(R.color.white.getColor(view.context))
            anchorView?.run { setAnchorView(this) }
        }
        .show()
    }

    protected fun showMessage(message: String?, anchorView: View? = null) {
        if (message == null) {
            return
        }

        val view = binding?.root ?: return
        Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .apply {
                setBackgroundTint(R.color.colorPrimary.getColor(view.context))
                setTextColor(R.color.white.getColor(view.context))
                anchorView?.run { setAnchorView(this) }
            }
            .show()
    }
}