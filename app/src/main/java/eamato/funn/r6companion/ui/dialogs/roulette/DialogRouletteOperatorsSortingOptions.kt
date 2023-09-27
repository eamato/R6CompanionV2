package eamato.funn.r6companion.ui.dialogs.roulette

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IntDef
import androidx.viewbinding.ViewBinding
import eamato.funn.r6companion.R
import eamato.funn.r6companion.databinding.DialogRouletteOperatorsSortingOptionsBinding
import eamato.funn.r6companion.ui.dialogs.ABaseDialog

class DialogRouletteOperatorsSortingOptions(private val onOptionSelected: IOnOptionSelected) :
    ABaseDialog<DialogRouletteOperatorsSortingOptionsBinding>() {

    companion object {
        @IntDef(SORT_ALPHABETICALLY_ASCENDING, SORT_ALPHABETICALLY_DESCENDING, SORT_SELECTED_FIRST)
        @Retention(AnnotationRetention.SOURCE)
        annotation class SortingOption

        const val SORT_ALPHABETICALLY_ASCENDING = 1
        const val SORT_ALPHABETICALLY_DESCENDING = 2
        const val SORT_SELECTED_FIRST = 3
    }

    interface IOnOptionSelected {
        fun onOptionSelected(@SortingOption option: Int)
    }

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        DialogRouletteOperatorsSortingOptionsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.sortAlphabeticallyAscending?.run {
            ivItemIcon.setImageResource(R.drawable.ic_sort_alphabetically_ascending_white_24dp)
            tvItemTitle.setText(R.string.alphabetic_sort_ascending)
            tvItemSubtitle.setText(R.string.alphabetic_sort_ascending_description)

            root.setOnClickListener {
                onOptionSelected.onOptionSelected(SORT_ALPHABETICALLY_ASCENDING)
                dismiss()
            }
        }

        binding?.sortAlphabeticallyDescending?.run {
            ivItemIcon.setImageResource(R.drawable.ic_sort_alphabetically_descending_white_24dp)
            tvItemTitle.setText(R.string.alphabetic_sort_descending)
            tvItemSubtitle.setText(R.string.alphabetic_sort_descending_description)

            root.setOnClickListener {
                onOptionSelected.onOptionSelected(SORT_ALPHABETICALLY_DESCENDING)
                dismiss()
            }
        }

        binding?.sortSelectedFirst?.run {
            ivItemIcon.setImageResource(R.drawable.ic_baseline_check_24)
            tvItemTitle.setText(R.string.sort_selected)
            tvItemSubtitle.setText(R.string.sort_selected_description)

            root.setOnClickListener {
                onOptionSelected.onOptionSelected(SORT_SELECTED_FIRST)
                dismiss()
            }
        }
    }

    override fun getChildTag(): String {
        return this::class.java.name
    }
}