package eamato.funn.r6companion.ui.viewmodels.roulette

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import eamato.funn.r6companion.domain.entities.roulette.Operator
import eamato.funn.r6companion.ui.fragments.roulette.FragmentRouletteResultArgs

class RollResultViewModel(state: SavedStateHandle) : ViewModel() {

    private val _winnerOperator = MutableLiveData<Operator>()
    val winnerOperator: LiveData<Operator> = _winnerOperator

    init {
        val rollCandidates = FragmentRouletteResultArgs.fromSavedStateHandle(state).rollCandidates
        getRollWinner(rollCandidates.toList())
    }

    private fun getRollWinner(rollCandidates: List<Operator>) {
        _winnerOperator.value = rollCandidates.random()
    }
}