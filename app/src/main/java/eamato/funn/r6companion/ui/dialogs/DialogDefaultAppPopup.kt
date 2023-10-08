package eamato.funn.r6companion.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.DialogDefaultAppPopupBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterPopupContent
import eamato.funn.r6companion.ui.entities.PopupContentItem

@AndroidEntryPoint
class DialogDefaultAppPopup(popupItems: List<PopupContentItem>) :
    ABaseBottomSheetDialog<DialogDefaultAppPopupBinding>(popupItems) {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        DialogDefaultAppPopupBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvPopupItems?.run {
            layoutManager = LinearLayoutManager(context)
            val adapterPopupContent = AdapterPopupContent()
            adapter = adapterPopupContent.also { it.submitList(popupItems) }

            val clickListener = RecyclerViewItemClickListener(
                this,
                object : RecyclerViewItemClickListener.OnItemTapListener {
                    override fun onItemClicked(view: View, position: Int) {
                        adapterPopupContent.getItemAtPosition(position).onClickListener?.invoke()
                        this@DialogDefaultAppPopup.dismiss()
                    }
                }
            )

            setOnItemClickListener(clickListener)
        }
    }

    override fun getChildTag(): String {
        return this::class.java.name
    }
}