package eamato.funn.r6companion.ui.viewmodels.roulette

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.core.utils.Result
import eamato.funn.r6companion.core.utils.SelectableObject
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.domain.entities.roulette.Operator
import eamato.funn.r6companion.domain.mappers.roulette.RouletteOperatorUseCaseMapper
import eamato.funn.r6companion.domain.usecases.OperatorsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouletteOperatorsViewModel @Inject constructor(
    private val operatorsUseCase: OperatorsUseCase
) : ViewModel() {

    private val _operators = MutableLiveData<UiState<List<SelectableObject<Operator>>>>(null)
    val operators: LiveData<UiState<List<SelectableObject<Operator>>>> = _operators

    private var visibleOperators = emptyList<Operator>()
    private var selectedOperators = emptyList<Operator>()
    private var immutableOperators = emptyList<Operator>()

    private var nameFilter = ""

    val selectedOperatorsCount
        get() = selectedOperators.size
    val allOperatorsCount
        get() = immutableOperators.size
    val visibleOperatorsCount
        get() = visibleOperators.size

    init {
        getOperators()
    }

    fun filterOperatorsByName(nameFilter: String = "") {
        _operators.value = UiState.Progress

        this.nameFilter = nameFilter.trim()

        applyFilters()

        val filteredOperators = visibleOperators
            .map { operator ->
                SelectableObject(
                    data = operator,
                    isSelected = selectedOperators.contains(operator)
                )
            }
            .toList()

        _operators.value = UiState.Success(filteredOperators)
    }

    fun selectUnselectOperator(selectedOperator: SelectableObject<Operator>) {
        val changedData = visibleOperators
            .map { operator ->
                if (operator == selectedOperator.data) {
                    SelectableObject(
                        data = operator,
                        isSelected = selectedOperator.isSelected.not()
                    )
                        .also { addRemoveSelectedOperator(it) }
                } else {
                    SelectableObject(
                        data = operator,
                        isSelected = selectedOperators.contains(operator)
                    )
                }
            }
            .toList()

        _operators.value = UiState.Success(changedData)
    }

    fun selectAllOperators() {
        selectedOperators = visibleOperators.map { operator -> operator.copy() }.toList()

        _operators.value = UiState.Success(
            visibleOperators
                .map { operator -> SelectableObject(data = operator, isSelected = true) }
                .toList()
        )
    }

    fun clearSelected() {
        selectedOperators = emptyList()

        _operators.value = UiState.Success(
            visibleOperators
                .map { operator -> SelectableObject(data = operator, isSelected = false) }
                .toList()
        )
    }

    fun sortByNameAscending() {
        visibleOperators = visibleOperators
            .sortedBy { operator -> operator.name }
            .toList()

        _operators.value = UiState.Success(
            visibleOperators.map { operator ->
                SelectableObject(
                    data = operator,
                    isSelected = selectedOperators.contains(operator)
                )
            }
        )
    }

    fun sortByNameDescending() {
        visibleOperators = visibleOperators
            .sortedByDescending { operator -> operator.name }
            .toList()

        _operators.value = UiState.Success(
            visibleOperators.map { operator ->
                SelectableObject(
                    data = operator,
                    isSelected = selectedOperators.contains(operator)
                )
            }
        )
    }

    fun sortSelected() {
        visibleOperators = visibleOperators
            .sortedByDescending { operator -> selectedOperators.contains(operator) }
            .toList()

        _operators.value = UiState.Success(
            visibleOperators.map { operator ->
                SelectableObject(
                    data = operator,
                    isSelected = selectedOperators.contains(operator)
                )
            }
        )
    }

    private fun applyFilters() {
        if (nameFilter.isEmpty()) {
            visibleOperators = immutableOperators.map { operator -> operator.copy() }.toList()
        }

        visibleOperators =  visibleOperators
            .filter { operator -> operator.name.contains(nameFilter, true) }
            .map { operator -> operator.copy() }
    }

    private fun getOperators() {
        viewModelScope.launch {
            _operators.value = UiState.Progress

            selectedOperators = emptyList()
            immutableOperators = emptyList()
            visibleOperators = emptyList()

            when (val result = operatorsUseCase(RouletteOperatorUseCaseMapper)) {
                is Result.Success -> {
                    visibleOperators = result.data
                        .map { operator -> operator.copy() }
                        .toList()

                    immutableOperators = result.data
                        .map { operator -> operator.copy() }
                        .toList()

                    _operators.value = UiState.Success(
                        result.data
                            .map { operator -> SelectableObject(data = operator) }
                            .toList()
                    )
                }
                is Result.Error -> {
                    _operators.value = UiState.Error(result.error)
                }
            }
        }
    }

    private fun addRemoveSelectedOperator(selectedOperator: SelectableObject<Operator>) {
        val selectedOperators: List<Operator> = if (selectedOperator.isSelected) {
            selectedOperators
                .toMutableList()
                .apply { add(selectedOperator.data) }
                .toList()
        } else {
            selectedOperators
                .toMutableList()
                .apply { remove(selectedOperator.data) }
                .toList()
        }

        this.selectedOperators = selectedOperators
    }
}