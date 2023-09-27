package eamato.funn.r6companion.ui.dialogs.roulette

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IntDef
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.databinding.DialogRouletteOperatorsSelectionOptionsBinding
import eamato.funn.r6companion.ui.dialogs.ABaseDialog

@AndroidEntryPoint
class DialogRouletteOperatorsSelectionOptions(private val onOptionSelected: IOnOptionSelected) :
    ABaseDialog<DialogRouletteOperatorsSelectionOptionsBinding>() {

    companion object {
        @IntDef(SELECT_ALL, CLEAR_SELECTION)
        @Retention(AnnotationRetention.SOURCE)
        annotation class SelectionOption

        const val SELECT_ALL = 1
        const val CLEAR_SELECTION = 2
    }

    interface IOnOptionSelected {
        fun onOptionSelected(@SelectionOption option: Int)
    }

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        DialogRouletteOperatorsSelectionOptionsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.selectAllOption?.run {
            ivItemIcon.setImageResource(R.drawable.ic_select_all_24dp)
            tvItemTitle.setText(R.string.select_all)
            tvItemSubtitle.setText(R.string.select_all_description)

            root.setOnClickListener {
                onOptionSelected.onOptionSelected(SELECT_ALL)
                dismiss()
            }
        }

        binding?.clearSelectionOption?.run {
            ivItemIcon.setImageResource(R.drawable.ic_clear_24dp)
            tvItemTitle.setText(R.string.clear_selections)
            tvItemSubtitle.setText(R.string.clear_selections_description)

            root.setOnClickListener {
                onOptionSelected.onOptionSelected(CLEAR_SELECTION)
                dismiss()
            }
        }
    }

    override fun getChildTag(): String {
        return this::class.java.name
    }
}