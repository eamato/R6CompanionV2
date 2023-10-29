package eamato.funn.r6companion.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding

abstract class ABaseDialog<VB : ViewBinding> : DialogFragment() {

    protected var binding: VB? = null
        private set

    abstract val bindingInitializer: (LayoutInflater) -> ViewBinding

    abstract fun getChildTag(): String

    @CallSuper
    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindingInitializer(layoutInflater).also { binding = it as? VB }.root

    @CallSuper
    override fun onStart() {
        super.onStart()

        dialog?.run {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    @CallSuper
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    fun show(fragmentManager: FragmentManager) {
        fragmentManager.executePendingTransactions()

        fragmentManager.findFragmentByTag(getChildTag())
            ?.let { it as? DialogFragment }
            ?.dismiss()

        try {
            show(fragmentManager.beginTransaction(), getChildTag())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}