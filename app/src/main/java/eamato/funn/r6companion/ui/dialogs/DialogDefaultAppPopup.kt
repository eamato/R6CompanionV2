package eamato.funn.r6companion.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.core.extenstions.getSerializableList
import eamato.funn.r6companion.core.extenstions.putSerializableList
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.DialogDefaultAppPopupBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterPopupContent
import eamato.funn.r6companion.ui.entities.PopupContentItem

@AndroidEntryPoint
class DialogDefaultAppPopup : ABaseBottomSheetDialog<DialogDefaultAppPopupBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        DialogDefaultAppPopupBinding::inflate

    companion object {
        private const val POPUP_ITEMS_LIST_KEY = "popup_items_list_key"

        fun getInstance(
            popupItems: List<PopupContentItem>
        ): DialogDefaultAppPopup = DialogDefaultAppPopup()
            .apply {
                val bundle = Bundle()
                bundle.putSerializableList(popupItems, POPUP_ITEMS_LIST_KEY)
                arguments = bundle
            }
    }

    private var popupItems: List<PopupContentItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        popupItems = arguments
            .getSerializableList(POPUP_ITEMS_LIST_KEY, PopupContentItem::class.java)
            ?: emptyList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvPopupItems?.run {
            layoutManager = LinearLayoutManager(context)
            val adapterPopupContent = AdapterPopupContent()
            adapter = adapterPopupContent.also { it.submitList(getPopupItems()) }

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

    override fun getPopupItems(): List<PopupContentItem> = popupItems
}