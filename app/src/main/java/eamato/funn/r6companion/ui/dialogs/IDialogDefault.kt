package eamato.funn.r6companion.ui.dialogs

import androidx.fragment.app.FragmentManager
import eamato.funn.r6companion.ui.entities.PopupContentItem

interface IDialogDefault {
    fun show(fragmentManager: FragmentManager, popupItems: List<PopupContentItem>)
    fun dismiss()
}