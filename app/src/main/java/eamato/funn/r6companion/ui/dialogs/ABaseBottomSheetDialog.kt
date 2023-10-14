package eamato.funn.r6companion.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import eamato.funn.r6companion.ui.entities.PopupContentItem

abstract class ABaseBottomSheetDialog<VB : ViewBinding> : BottomSheetDialogFragment(), IDialogDefault {

    protected var binding: VB? = null
        private set

    protected var popupItems: List<PopupContentItem> = emptyList()

    protected abstract val bindingInitializer: (LayoutInflater) -> ViewBinding

    abstract fun getChildTag(): String

    @CallSuper
    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindingInitializer(layoutInflater).also { binding = it as? VB }.root

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            dismiss()
        }
        super.onViewStateRestored(savedInstanceState)
    }

    @CallSuper
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun show(fragmentManager: FragmentManager, popupItems: List<PopupContentItem>) {
        if (popupItems.isEmpty()) {
            return
        }

        this.popupItems = popupItems

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