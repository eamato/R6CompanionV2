package eamato.funn.r6companion.ui.fragments.companion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDimensionPixelSize
import eamato.funn.r6companion.core.extenstions.setItemDecoration
import eamato.funn.r6companion.databinding.FragmentOperatorDetailsBinding
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterOperatorDetails
import eamato.funn.r6companion.ui.entities.companion.operator.OperatorDetails.Companion.VIEW_TYPE_ABILITY_ENTITY
import eamato.funn.r6companion.ui.entities.companion.operator.OperatorDetails.Companion.VIEW_TYPE_LOAD_OUT_ENTITY
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
import eamato.funn.r6companion.ui.viewmodels.companion.operators.OperatorDetailsViewModel

class FragmentOperatorDetails : ABaseFragment<FragmentOperatorDetailsBinding>() {

    private val operatorDetailsViewModel: OperatorDetailsViewModel by viewModels()

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentOperatorDetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOperatorDetailsRecyclerView()
        setObservers()
    }

    private fun initOperatorDetailsRecyclerView() {
        binding?.rvOperatorDetails?.run {
            setHasFixedSize(true)

            val spanCount = 2
            val gridLayoutManager = GridLayoutManager(context, spanCount)
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter?.getItemViewType(position)) {
                        VIEW_TYPE_LOAD_OUT_ENTITY, VIEW_TYPE_ABILITY_ENTITY -> 1
                        else -> 2
                    }
                }
            }
            layoutManager = gridLayoutManager

            val adapterOperatorDetails = AdapterOperatorDetails()
            adapter = adapterOperatorDetails

            val spacingDecoration = SpacingItemDecoration
                .linear()
                .setSpacingRes(
                    R.dimen.dp_10,
                    R.dimen.dp_10,
                    R.dimen.dp_10,
                    R.dimen.dp_10,
                )
                .setTopSpacingMultiplier(
                    R.dimen.dp_2.getDimensionPixelSize(context)
                )
                .create(context)
            setItemDecoration(spacingDecoration)
        }
    }

    private fun setObservers() {
        operatorDetailsViewModel.operatorDetails.observe(viewLifecycleOwner) { operatorDetails ->
            binding?.rvOperatorDetails?.adapter
                ?.let { it as? AdapterOperatorDetails }
                ?.submitList(operatorDetails)
        }
    }
}