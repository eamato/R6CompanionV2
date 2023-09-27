package eamato.funn.r6companion.ui.dialogs.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IntDef
import androidx.viewbinding.ViewBinding
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getColor
import eamato.funn.r6companion.databinding.DialogCompanionOperatorsFilterOptionsBinding
import eamato.funn.r6companion.ui.dialogs.ABaseDialog

class DialogCompanionOperatorsFilterOptions(private val onFilterSelected: IOnFilterSelected) :
    ABaseDialog<DialogCompanionOperatorsFilterOptionsBinding>() {

    companion object {
        @IntDef(SELECT_ALL, SELECT_DEFENDERS, SELECT_ATTACKERS)
        @Retention(AnnotationRetention.SOURCE)
        annotation class FilterOptions

        const val SELECT_ALL = 1
        const val SELECT_DEFENDERS = 2
        const val SELECT_ATTACKERS = 3
    }

    interface IOnFilterSelected {
        fun onFilterSelected(@FilterOptions filter: Int)
    }

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        DialogCompanionOperatorsFilterOptionsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.allOperators?.run {
            ivItemIcon.setImageResource(R.drawable.ic_operators_24dp)
            tvItemTitle.setText(R.string.companion_operators_all_filter)
            tvItemSubtitle.setText(R.string.companion_operators_all_filter_description)

            root.setOnClickListener {
                onFilterSelected.onFilterSelected(SELECT_ALL)
                dismiss()
            }
        }

        binding?.attackOperators?.run {
            ivItemIcon.setImageResource(R.drawable.ic_attack_role)
            ivItemIcon.setColorFilter(
                R.color.white.getColor(ivItemIcon.context),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            tvItemTitle.setText(R.string.companion_operators_attackers_filter)
            tvItemSubtitle.setText(R.string.companion_operators_attackers_filter_description)

            root.setOnClickListener {
                onFilterSelected.onFilterSelected(SELECT_ATTACKERS)
                dismiss()
            }
        }

        binding?.defendOperators?.run {
            ivItemIcon.setImageResource(R.drawable.ic_defend_role)
            ivItemIcon.setColorFilter(
                R.color.white.getColor(ivItemIcon.context),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            tvItemTitle.setText(R.string.companion_operators_defenders_filter)
            tvItemSubtitle.setText(R.string.companion_operators_defenders_filter_description)

            root.setOnClickListener {
                onFilterSelected.onFilterSelected(SELECT_DEFENDERS)
                dismiss()
            }
        }
    }

    override fun getChildTag(): String {
        return this::class.java.name
    }
}