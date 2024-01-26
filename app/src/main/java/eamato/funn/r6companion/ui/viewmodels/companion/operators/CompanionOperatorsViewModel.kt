package eamato.funn.r6companion.ui.viewmodels.companion.operators

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.utils.Result
import eamato.funn.r6companion.core.utils.ScrollToTopAdditionalEvent
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.domain.entities.EOperatorRoles
import eamato.funn.r6companion.domain.entities.companion.operators.Operator
import eamato.funn.r6companion.domain.mappers.companion.CompanionOperatorUseCaseMapper
import eamato.funn.r6companion.domain.usecases.OperatorsUseCase
import eamato.funn.r6companion.ui.entities.PopupContentItem
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanionOperatorsViewModel @Inject constructor(
    private val operatorsUseCase: OperatorsUseCase
) : ViewModel() {

    private val _operators = MutableLiveData<UiState<List<Operator>>>(null)
    val operators: LiveData<UiState<List<Operator>>> = _operators

    private var immutableOperators = emptyList<Operator>()
    private var nameFilter = ""
    private var roleFilter = EOperatorRoles.UNDEFINED

    init {
        getOperators()
    }

    fun filterOperatorsByName(nameFilter: String = "") {
        _operators.value = UiState.Progress

        this.nameFilter = nameFilter.trim()

        _operators.value = UiState.Success(applyFilters().toList(), ScrollToTopAdditionalEvent)
    }

    fun createFilterPopupContentItems(): List<PopupContentItem> {
        return listOf(
            PopupContentItem(
                icon = R.drawable.ic_all_operators_24,
                title = UiText.ResourceString(R.string.operators_all_filter),
                subTitle = null
            ) { filterOperatorsByRole(EOperatorRoles.UNDEFINED) },
            PopupContentItem(
                icon = R.drawable.ic_attackers_24,
                title = UiText.ResourceString(R.string.operators_attackers_filter),
                subTitle = null
            ) { filterOperatorsByRole(EOperatorRoles.ATTACKERS) },
            PopupContentItem(
                icon = R.drawable.ic_defenders_24,
                title = UiText.ResourceString(R.string.operators_defenders_filter),
                subTitle = null
            ) { filterOperatorsByRole(EOperatorRoles.DEFENDERS) },
        )
    }

    private fun filterOperatorsByRole(role: EOperatorRoles) {
        roleFilter = role
        _operators.value = UiState.Progress
        _operators.value = UiState.Success(applyFilters().toList(), ScrollToTopAdditionalEvent)
    }

    private fun applyFilters(): List<Operator> {
        var result = immutableOperators

        if (nameFilter.isEmpty() && roleFilter == EOperatorRoles.UNDEFINED) {
            return result
        }

        if (nameFilter.isNotEmpty()) {
            result = result.filter { operator -> operator.name.contains(nameFilter, true) }
        }

        if (roleFilter != EOperatorRoles.UNDEFINED) {
            result = result.filter { operator -> operator.role == roleFilter }
        }

        return result.map { operator -> operator.copy() }
    }

    private fun getOperators() {
        viewModelScope.launch {
            _operators.value = UiState.Progress

            when (val result = operatorsUseCase(CompanionOperatorUseCaseMapper)) {
                is Result.Success -> {
                    _operators.value = UiState.Success(result.data)
                    immutableOperators = result.data.map { it.copy() }.toList()
                }

                is Result.Error -> {
                    _operators.value = UiState.Error(result.error)
                    immutableOperators = emptyList()
                }
            }
        }
    }
}