package eamato.funn.r6companion.ui.viewmodels.companion.operators

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.utils.Result
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.core.utils.UiText
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

    enum class ERoleFilter {
        ALL, DEFENDERS, ATTACKERS
    }

    private val _operators = MutableLiveData<UiState<List<Operator>>>(null)
    val operators: LiveData<UiState<List<Operator>>> = _operators

    private var immutableOperators = emptyList<Operator>()
    private var nameFilter = ""
    private var roleFilter = ERoleFilter.ALL

    init {
        getOperators()
    }

    fun filterOperatorsByName(nameFilter: String = "") {
        _operators.value = UiState.Progress

        this.nameFilter = nameFilter.trim()

        _operators.value = UiState.Success(applyFilters().toList())
    }

    fun createFilterPopupContentItems(): List<PopupContentItem> {
        return listOf(
            PopupContentItem(
                icon = R.drawable.ic_all_operators_24,
                title = UiText.ResourceString(R.string.companion_operators_all_filter),
                subTitle = null
            ) { filterOperatorsByRole(ERoleFilter.ALL) },
            PopupContentItem(
                icon = R.drawable.ic_attackers_24,
                title = UiText.ResourceString(R.string.companion_operators_attackers_filter),
                subTitle = null
            ) { filterOperatorsByRole(ERoleFilter.ATTACKERS) },
            PopupContentItem(
                icon = R.drawable.ic_defenders_24,
                title = UiText.ResourceString(R.string.companion_operators_defenders_filter),
                subTitle = null
            ) { filterOperatorsByRole(ERoleFilter.DEFENDERS) },
        )
    }

    private fun filterOperatorsByRole(role: ERoleFilter) {
        roleFilter = role
        _operators.value = UiState.Progress
        _operators.value = UiState.Success(applyFilters().toList())
    }

    private fun applyFilters(): List<Operator> {
        var result = immutableOperators

        if (nameFilter.isEmpty() && roleFilter == ERoleFilter.ALL) {
            return result
        }

        if (nameFilter.isNotEmpty()) {
            result = result.filter { operator -> operator.name.contains(nameFilter, true) }
        }

        if (roleFilter != ERoleFilter.ALL) {
            val role = when (roleFilter) {
                ERoleFilter.DEFENDERS -> Operator.ROLE_DEFENDER
                ERoleFilter.ATTACKERS -> Operator.ROLE_ATTACKER
                else -> null
            } ?: return result.map { operator -> operator.copy() }

            result = result.filter { operator -> operator.role == role }
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