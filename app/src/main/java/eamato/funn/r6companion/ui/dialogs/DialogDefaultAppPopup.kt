package eamato.funn.r6companion.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.setItemDecoration
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.DialogDefaultAppPopupBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterPopupContent
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration

@AndroidEntryPoint
class DialogDefaultAppPopup : ABaseBottomSheetDialog<DialogDefaultAppPopupBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        DialogDefaultAppPopupBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvPopupItems?.run {
            layoutManager = LinearLayoutManager(context)
            val adapterPopupContent = AdapterPopupContent()
            adapter = adapterPopupContent.also { it.submitList(popupItems) }

            val spacingDecoration = SpacingItemDecoration
                .linear()
                .setSpacingRes(R.dimen.dp_2, R.dimen.dp_2, R.dimen.dp_2, R.dimen.dp_2)
                .create(context)
            setItemDecoration(spacingDecoration)

            val clickListener = RecyclerViewItemClickListener(
                this,
                object : RecyclerViewItemClickListener.OnItemTapListener {
                    override fun onItemClicked(view: View, position: Int) {
                        val selectedSettingsItem = adapterPopupContent.getItemAtPosition(position)
                        adapterPopupContent.getItemAtPosition(position).onClickListener?.invoke()
                        if (selectedSettingsItem.isEnabled) {
                            this@DialogDefaultAppPopup.dismiss()
                        }
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