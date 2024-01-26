package eamato.funn.r6companion.ui.viewmodels.companion.operators

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.utils.Result
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.domain.entities.EOperatorRoles
import eamato.funn.r6companion.domain.entities.companion.operators.Operator
import eamato.funn.r6companion.domain.mappers.companion.CompanionOperatorUseCaseMapper
import eamato.funn.r6companion.domain.usecases.OperatorByIdUseCase
import eamato.funn.r6companion.ui.entities.companion.operator.OperatorDetails
import eamato.funn.r6companion.ui.fragments.companion.FragmentOperatorDetailsArgs
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OperatorDetailsViewModel @Inject constructor(
    state: SavedStateHandle,
    private val operatorByIdUseCase: OperatorByIdUseCase
) : ViewModel() {

    private val _operatorDetails = MutableLiveData<List<OperatorDetails>?>(null)
    val operatorDetails: LiveData<List<OperatorDetails>?> = _operatorDetails

    init {
        parseArgsFromState(state)
    }

    private fun parseArgsFromState(state: SavedStateHandle) {
        val args = FragmentOperatorDetailsArgs.fromSavedStateHandle(state)
        val operator = args.operator
        val operatorId = args.operatorId
        if (operator != null) {
            createListOfDetailsFor(operator)
            return
        }

        if (operatorId > 0) {
            viewModelScope.launch {
                val operatorById = getOperatorById(operatorId)
                if (operatorById != null) {
                    createListOfDetailsFor(operatorById)
                }
            }
        }
    }

    private fun createListOfDetailsFor(operator: Operator) {
        val operatorDetails = mutableListOf<OperatorDetails>()

        operator.wideImgLink
            .let { OperatorDetails.OperatorDetailsImage(UiText.SimpleString(it)) }
            .run { operatorDetails.add(this) }
        operatorDetails.add(
            OperatorDetails.OperatorDetailsTitle(
                UiText.ResourceString(R.string.operator_details_organization)
            )
        )
        operator.squad
            .let {
                OperatorDetails.OrganizationEntity(
                    UiText.SimpleString(it.name),
                    UiText.SimpleString(it.iconLink)
                )
            }
            .run { operatorDetails.add(this) }
        operatorDetails.add(OperatorDetails.OperatorDetailsDivider)

        operatorDetails.add(
            OperatorDetails.OperatorDetailsTitle(
                UiText.ResourceString(R.string.operator_details_role)
            )
        )
        operator.role
            .let {
                val role = when (it) {
                    EOperatorRoles.DEFENDERS -> UiText.ResourceString(
                        R.string.operator_details_role_defender
                    )

                    EOperatorRoles.ATTACKERS -> UiText.ResourceString(
                        R.string.operator_details_role_attacker
                    )

                    else -> UiText.SimpleString("")
                }
                OperatorDetails.OperatorDetailsText(role)
            }
            .run { operatorDetails.add(this) }
        operatorDetails.add(OperatorDetails.OperatorDetailsDivider)

        operatorDetails.add(
            OperatorDetails.OperatorDetailsTitle(
                UiText.ResourceString(R.string.operator_details_stats)
            )
        )
        operator.speedRating
            .let {
                OperatorDetails.OperatorDetailsStat(
                    UiText.ResourceString(R.string.operator_details_speed),
                    it
                )
            }
            .run { operatorDetails.add(this) }
        operator.armorRating
            .let {
                OperatorDetails.OperatorDetailsStat(
                    UiText.ResourceString(R.string.operator_details_armor),
                    it
                )
            }
            .run { operatorDetails.add(this) }
        operatorDetails.add(OperatorDetails.OperatorDetailsDivider)

        operatorDetails.add(
            OperatorDetails.OperatorDetailsTitle(
                UiText.ResourceString(R.string.operator_details_loadout)
            )
        )
        operatorDetails.add(
            OperatorDetails.OperatorDetailsSubtitle(
                UiText.ResourceString(R.string.operator_details_primaries)
            )
        )
        operator.equipment
            .let {
                it.primaries.map { primaries ->
                    OperatorDetails.OperatorDetailsLoadOutEntity(
                        UiText.SimpleString(primaries.name),
                        UiText.SimpleString(primaries.iconLink),
                        UiText.SimpleString(primaries.typeText)
                    )
                }
            }
            .run { operatorDetails.addAll(this) }
        operatorDetails.add(
            OperatorDetails.OperatorDetailsSubtitle(
                UiText.ResourceString(R.string.operator_details_secondaries)
            )
        )
        operator.equipment
            .let {
                it.secondaries.map { secondaries ->
                    OperatorDetails.OperatorDetailsLoadOutEntity(
                        UiText.SimpleString(secondaries.name),
                        UiText.SimpleString(secondaries.iconLink),
                        UiText.SimpleString(secondaries.typeText)
                    )
                }
            }
            .run { operatorDetails.addAll(this) }
        operatorDetails.add(
            OperatorDetails.OperatorDetailsSubtitle(
                UiText.ResourceString(R.string.operator_details_gadgets)
            )
        )
        operator.equipment
            .let {
                it.devices.map { gadgets ->
                    OperatorDetails.OperatorDetailsLoadOutEntity(
                        UiText.SimpleString(gadgets.name),
                        UiText.SimpleString(gadgets.iconLink)
                    )
                }
            }
            .run { operatorDetails.addAll(this) }
        operatorDetails.add(
            OperatorDetails.OperatorDetailsSubtitle(
                UiText.ResourceString(R.string.operator_details_ability)
            )
        )
        operator.equipment.skill
            .let {
                OperatorDetails.OperatorDetailsAbilityEntity(
                    UiText.SimpleString(it.name),
                    UiText.SimpleString(it.iconLink)
                )
            }
            .run { operatorDetails.add(this) }

        _operatorDetails.value = operatorDetails.toList()
    }

    private suspend fun getOperatorById(operatorId: Int): Operator? {
        return when (val result = operatorByIdUseCase(operatorId, CompanionOperatorUseCaseMapper)) {
            is Result.Success -> {
                result.data
            }

            is Result.Error -> {
                null
            }
        }
    }
}