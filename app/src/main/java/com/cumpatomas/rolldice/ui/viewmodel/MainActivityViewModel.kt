package com.cumpatomas.rolldice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.rolldice.R
import com.cumpatomas.rolldice.domain.AssignFirstPlayerTurnUseCase
import com.cumpatomas.rolldice.domain.GetRandomDiceNumberUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {

    private val assignFirstPlayerTurnUseCase = AssignFirstPlayerTurnUseCase()
    private val getRandomDiceNumberUseCase = GetRandomDiceNumberUseCase()

    private val _startPlayerTurn = Channel<Int>()
    val startPlayerTurn = _startPlayerTurn.receiveAsFlow()

    private var _playerTurn = 0
    val playerTurn: Int
        get() = _playerTurn

    private var _playerOneScore = 0
    val playerOneScore: Int
        get() = _playerOneScore

    private var _playerTwoScore = 0
    val playerTwoScore: Int
        get() = _playerTwoScore

    private var _rounds = 6
    val rounds: Int
        get() = _rounds

    private var _randomNumber = 0
    val randomNumber: Int
        get() = _randomNumber


    init {
        viewModelScope.launch(Dispatchers.Main) {
            _startPlayerTurn.send(assignFirstPlayerTurnUseCase.invoke())
        }
    }

    fun getRandomTurn() = viewModelScope.launch { _startPlayerTurn.send(assignFirstPlayerTurnUseCase.invoke()) }

    fun setPlayerTurn(turn: Int) {
        _playerTurn = turn
    }

    fun setPlayerOneScore(scoreEvent: ScoreEvent) {

    }

    fun setPlayerTwoScore(scoreEvent: ScoreEvent) {
        _playerTwoScore = score
    }

    fun setRounds(roundsEvent: RoundsEvent) {
        when(roundsEvent) {
            RoundsEvent.DecreaseRound -> {
                _rounds -= 1
            }
            is RoundsEvent.SetRounds -> {
                _rounds = roundsEvent.rounds
            }
        }
    }

    @JvmName("getRandomNumber1")
    fun getRandomNumber(): Int {
        _randomNumber = getRandomDiceNumberUseCase.invoke()
        return when(_randomNumber) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    }

}
sealed class RoundsEvent {
    data class SetRounds(val rounds: Int): RoundsEvent()
    object DecreaseRound: RoundsEvent()
}

sealed class ScoreEvent {
    data class IncreaseScore(val score: Int): ScoreEvent()
    object SetScore: ScoreEvent()
}