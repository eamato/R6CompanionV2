package eamato.funn.r6companion.ui.viewmodels.roulette

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.R
import eamato.funn.r6companion.SavedOperator
import eamato.funn.r6companion.core.storage.SavedOperatorsManager
import eamato.funn.r6companion.core.utils.DialogContent
import eamato.funn.r6companion.core.utils.Result
import eamato.funn.r6companion.core.utils.ScrollToTopAdditionalEvent
import eamato.funn.r6companion.core.utils.SelectableObject
import eamato.funn.r6companion.core.utils.SingleLiveEvent
import eamato.funn.r6companion.core.utils.UiState
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.domain.entities.EOperatorRoles
import eamato.funn.r6companion.domain.entities.roulette.Operator
import eamato.funn.r6companion.domain.mappers.roulette.RouletteOperatorUseCaseMapper
import eamato.funn.r6companion.domain.usecases.OperatorsUseCase
import eamato.funn.r6companion.ui.entities.PopupContentItem
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouletteOperatorsViewModel @Inject constructor(
    private val operatorsUseCase: OperatorsUseCase,
    private val savedOperatorsManager: SavedOperatorsManager,
) : ViewModel() {

    private val _operators = MutableLiveData<UiState<List<SelectableObject<Operator>>>>(null)
    val operators: LiveData<UiState<List<SelectableObject<Operator>>>> = _operators

    private val _savingOperatorsResult = SingleLiveEvent<UiState<UiText>?>()
    val savingOperatorsResult: LiveData<UiState<UiText>?> = _savingOperatorsResult

    private val _showAlertDialog = SingleLiveEvent<DialogContent?>()
    val showAlertDialog: LiveData<DialogContent?> = _showAlertDialog

    private val _creatingSelectionMenuItemsState = SingleLiveEvent<UiState<Unit>>()
    val creatingSelectionMenuItemsState: LiveData<UiState<Unit>> = _creatingSelectionMenuItemsState

    private var visibleOperators = emptyList<Operator>()
    private var selectedOperators = emptyList<Operator>()
    private var immutableOperators = emptyList<Operator>()

    private var nameFilter = ""
    private var roleFilter = EOperatorRoles.UNDEFINED

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

        _operators.value = UiState.Success(filteredOperators, ScrollToTopAdditionalEvent)
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

    suspend fun createSelectionPopupContentItems(): List<PopupContentItem> {
        _creatingSelectionMenuItemsState.value = UiState.Progress

        val selectionMenuItems = listOf(
            PopupContentItem(
                icon = R.drawable.ic_select_all_24dp,
                title = UiText.ResourceString(R.string.select_all),
                subTitle = null
            ) { selectAllOperators() },
            PopupContentItem(
                icon = R.drawable.ic_select_attackers_24dp,
                title = UiText.ResourceString(R.string.select_attackers),
                subTitle = null
            ) { selectAttackerOperators() },
            PopupContentItem(
                icon = R.drawable.ic_select_defenders_24dp,
                title = UiText.ResourceString(R.string.select_defenders),
                subTitle = null
            ) { selectDefenderOperators() },
            PopupContentItem(
                icon = R.drawable.ic_clear_24dp,
                title = UiText.ResourceString(R.string.clear_selections),
                subTitle = null
            ) { clearSelected() },
            PopupContentItem(
                icon = R.drawable.ic_save_selected_24dp,
                title = UiText.ResourceString(R.string.save_selected),
                subTitle = null,
                isEnabled = selectedOperators.isNotEmpty()
            ) { tryToSaveSelectedOperators() },
            PopupContentItem(
                icon = R.drawable.ic_restore_saved_24dp,
                title = UiText.ResourceString(R.string.restore_saved),
                subTitle = null,
                isEnabled = getSavedOperators().isNotEmpty()
            ) { restoreSavedOperators() },
            PopupContentItem(
                icon = R.drawable.ic_clear_saved_24dp,
                title = UiText.ResourceString(R.string.remove_saved),
                subTitle = null,
                isEnabled = getSavedOperators().isNotEmpty()
            ) { clearSavedOperators() }
        )

        _creatingSelectionMenuItemsState.value = UiState.Success(Unit)

        return selectionMenuItems
    }

    fun createSortingPopupContentItems(): List<PopupContentItem> {
        return listOf(
            PopupContentItem(
                icon = R.drawable.ic_sort_alphabetically_ascending_white_24dp,
                title = UiText.ResourceString(R.string.alphabetic_sort_ascending),
                subTitle = null
            ) { sortByNameAscending() },
            PopupContentItem(
                icon = R.drawable.ic_sort_alphabetically_descending_white_24dp,
                title = UiText.ResourceString(R.string.alphabetic_sort_descending),
                subTitle = null
            ) { sortByNameDescending() },
            PopupContentItem(
                icon = R.drawable.ic_baseline_check_24,
                title = UiText.ResourceString(R.string.sort_selected),
                subTitle = null
            ) { sortSelected() }
        )
    }

    fun createFilterPopupContentItems(): List<PopupContentItem> {
        return listOf(
            PopupContentItem(
                icon = R.drawable.ic_all_operators_24,
                title = UiText.ResourceString(R.string.operators_all_filter),
                subTitle = null
            ) { filterByRole(EOperatorRoles.UNDEFINED) },
            PopupContentItem(
                icon = R.drawable.ic_attackers_24,
                title = UiText.ResourceString(R.string.operators_attackers_filter),
                subTitle = null
            ) { filterByRole(EOperatorRoles.ATTACKERS) },
            PopupContentItem(
                icon = R.drawable.ic_defenders_24,
                title = UiText.ResourceString(R.string.operators_defenders_filter),
                subTitle = null
            ) { filterByRole(EOperatorRoles.DEFENDERS) },
        )
    }

    fun getAllSelectedOperators() = selectedOperators

    private fun tryToSaveSelectedOperators() {
        _savingOperatorsResult.value = UiState.Progress

        if (selectedOperators.isEmpty()) {
            _savingOperatorsResult.value = null
            return
        }

        viewModelScope.launch {
            val savedOperators = getSavedOperators()
            if (savedOperators.isNotEmpty()) {
                _savingOperatorsResult.value = null

                _showAlertDialog.value = DialogContent.Builder
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(UiText.ResourceString(R.string.attention))
                    .setMessage(UiText.ResourceString(R.string.save_confirmation_message))
                    .setPositiveButton(UiText.ResourceString(R.string.yes)) {
                        viewModelScope.launch {
                            _savingOperatorsResult.value = UiState.Progress

                            saveSelectedOperators()
                            _savingOperatorsResult.value =
                                UiState.Success(UiText.ResourceString(R.string.saved_success_message))
                        }
                    }
                    .setNegativeButton(UiText.ResourceString(R.string.no))
                    .build()
                return@launch
            }

            saveSelectedOperators()
            _savingOperatorsResult.value =
                UiState.Success(UiText.ResourceString(R.string.saved_success_message))
        }
    }

    private suspend fun saveSelectedOperators() {
        savedOperatorsManager.saveOperators(selectedOperators)
    }

    private suspend fun getSavedOperators(): List<SavedOperator> {
        return savedOperatorsManager
            .savedOperators
            .firstOrNull()
            ?.savedOperatorsList
            ?: emptyList()
    }

    private fun restoreSavedOperators() {
        _savingOperatorsResult.value = UiState.Progress

        viewModelScope.launch {
            val savedOperators = getSavedOperators()
            if (savedOperators.isEmpty()) {
                _savingOperatorsResult.value = null
                return@launch
            }

            selectedOperators = visibleOperators
                .filter { operator ->
                    savedOperators.any { savedOperator ->
                        savedOperator.id == operator.id
                    }
                }
                .map { operator -> operator.copy() }
                .toList()

            exposeSelectedOperatorsToUI()

            _savingOperatorsResult.value =
                UiState.Success(UiText.ResourceString(R.string.restored_success_message))
        }
    }

    private fun clearSavedOperators() {
        _showAlertDialog.value = DialogContent.Builder
            .setIcon(R.mipmap.ic_launcher)
            .setTitle(UiText.ResourceString(R.string.attention))
            .setMessage(UiText.ResourceString(R.string.delete_saved_confirmation_message))
            .setPositiveButton(UiText.ResourceString(R.string.yes)) {
                viewModelScope.launch {
                    _savingOperatorsResult.value = UiState.Progress

                    savedOperatorsManager.clearSavedOperators()
                    _savingOperatorsResult.value =
                        UiState.Success(UiText.ResourceString(R.string.deleted_success_message))
                }
            }
            .setNegativeButton(UiText.ResourceString(R.string.no))
            .build()
    }

    private fun selectAllOperators() {
        selectedOperators = visibleOperators.map { operator -> operator.copy() }.toList()

        _operators.value = UiState.Success(
            visibleOperators
                .map { operator -> SelectableObject(data = operator, isSelected = true) }
                .toList()
        )
    }

    private fun selectAttackerOperators() {
        selectedOperators = immutableOperators
            .filter { operator -> operator.role == EOperatorRoles.ATTACKERS }
            .map { operator -> operator.copy() }
            .toList()

        exposeSelectedOperatorsToUI()
    }

    private fun selectDefenderOperators() {
        selectedOperators = immutableOperators
            .filter { operator -> operator.role == EOperatorRoles.DEFENDERS }
            .map { operator -> operator.copy() }
            .toList()

        exposeSelectedOperatorsToUI()
    }

    private fun exposeSelectedOperatorsToUI() {
        _operators.value = UiState.Success(
            visibleOperators
                .map { operator ->
                    SelectableObject(
                        data = operator,
                        isSelected = selectedOperators.contains(operator)
                    )
                }
                .toList()
        )
    }

    private fun clearSelected() {
        selectedOperators = emptyList()

        _operators.value = UiState.Success(
            visibleOperators
                .map { operator -> SelectableObject(data = operator, isSelected = false) }
                .toList()
        )
    }

    private fun sortByNameAscending() {
        visibleOperators = visibleOperators
            .sortedBy { operator -> operator.name }
            .toList()

        _operators.value = UiState.Success(
            visibleOperators.map { operator ->
                SelectableObject(
                    data = operator,
                    isSelected = selectedOperators.contains(operator)
                )
            },
            ScrollToTopAdditionalEvent
        )
    }

    private fun sortByNameDescending() {
        visibleOperators = visibleOperators
            .sortedByDescending { operator -> operator.name }
            .toList()

        _operators.value = UiState.Success(
            visibleOperators.map { operator ->
                SelectableObject(
                    data = operator,
                    isSelected = selectedOperators.contains(operator)
                )
            },
            ScrollToTopAdditionalEvent
        )
    }

    private fun sortSelected() {
        visibleOperators = visibleOperators
            .sortedByDescending { operator -> selectedOperators.contains(operator) }
            .toList()

        _operators.value = UiState.Success(
            visibleOperators.map { operator ->
                SelectableObject(
                    data = operator,
                    isSelected = selectedOperators.contains(operator)
                )
            },
            ScrollToTopAdditionalEvent
        )
    }

    private fun filterByRole(role: EOperatorRoles) {
        _operators.value = UiState.Progress

        roleFilter = role

        applyFilters()

        val filteredOperators = visibleOperators
            .map { operator ->
                SelectableObject(
                    data = operator,
                    isSelected = selectedOperators.contains(operator)
                )
            }
            .toList()

        _operators.value = UiState.Success(filteredOperators, ScrollToTopAdditionalEvent)
    }

    private fun applyFilters() {
        if (nameFilter.isEmpty() && roleFilter == EOperatorRoles.UNDEFINED) {
            visibleOperators = immutableOperators.map { operator -> operator.copy() }.toList()

            return
        }

        visibleOperators = if (roleFilter != EOperatorRoles.UNDEFINED) {
            immutableOperators
                .filter { operator -> operator.role == roleFilter }
                .map { operator -> operator.copy() }
        } else {
            immutableOperators.map { operator -> operator.copy() }
        }

        if (nameFilter.isNotEmpty()) {
            visibleOperators = visibleOperators
                .filter { operator -> operator.name.contains(nameFilter, true) }
                .map { operator -> operator.copy() }
        }
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