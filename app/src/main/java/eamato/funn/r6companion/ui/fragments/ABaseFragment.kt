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

    protected fun showError(error: Throwable) {
        binding?.root?.run nonNullRootView@ {
            error.message?.run nonNullErrorMessage@ {
                Snackbar
                    .make(
                        this@nonNullRootView,
                        this@nonNullErrorMessage,
                        Snackbar.LENGTH_SHORT
                    )
                    .apply {
                        setBackgroundTint(
                            R.color.colorPrimary.getColor(this@nonNullRootView.context)
                        )
                        setTextColor(
                            R.color.white.getColor(this@nonNullRootView.context)
                        )
                    }
                    .show()
            }
        }
    }

    protected fun showMessage(message: String?) {
        if (message == null) {
            return
        }

        binding?.root?.run nonNullRootView@ {
            Snackbar
                .make(this@nonNullRootView, message, Snackbar.LENGTH_SHORT)
                .apply {
                    setBackgroundTint(R.color.colorPrimary.getColor(this@nonNullRootView.context))
                    setTextColor(R.color.white.getColor(this@nonNullRootView.context))
                }
                .show()
        }
    }
}