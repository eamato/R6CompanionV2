package eamato.funn.r6companion.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.sidesheet.SideSheetDialog
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.core.extenstions.replaceItemDecoration
import eamato.funn.r6companion.core.extenstions.setItemDecoration
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.DialogDefaultAppPopupBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterPopupContent
import eamato.funn.r6companion.ui.entities.PopupContentItem
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
import eamato.funn.r6companion.ui.recyclerviews.decorations.SystemSpacingsItemDecoration

class DialogDefaultAppPopupSide(context: Context) : SideSheetDialog(context), IDialogDefault {

    private var binding: DialogDefaultAppPopupBinding? = null

    private var popupItems: List<PopupContentItem> = emptyList()

    init {
        binding = DialogDefaultAppPopupBinding.inflate(LayoutInflater.from(context))
        setContentView(binding?.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

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
                        adapterPopupContent.getItemAtPosition(position).onClickListener?.invoke()
                        dismiss()
                    }
                }
            )

            setOnItemClickListener(clickListener)
        }

        applySystemInsetsToListIfNeeded()
    }

    override fun show(fragmentManager: FragmentManager, popupItems: List<PopupContentItem>) {
        if (popupItems.isEmpty()) {
            return
        }

        this.popupItems = popupItems
        this.show()
    }

    private fun applySystemInsetsToListIfNeeded() {
        window?.decorView?.applySystemInsetsIfNeeded { insets ->
            val spacingDecoration = SystemSpacingsItemDecoration(
                insets.top, 0, insets.bottom, insets.right
            )
            binding?.rvPopupItems?.replaceItemDecoration(spacingDecoration)
        }
    }
}