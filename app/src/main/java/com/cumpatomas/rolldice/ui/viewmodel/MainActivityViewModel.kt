package com.cumpatomas.rolldice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.rolldice.R
import com.cumpatomas.rolldice.manualdi.UseCasesModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {

    private val assignFirstPlayerTurnUseCase = UseCasesModule.getAssignFirstPlayerTurnUseCase()
    private val getRandomDiceNumberUseCase = UseCasesModule.getRandomDiceNumberUseCase()

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

    /** [setPlayerOneScore] and [setPlayerTwoScore] both receive a [ScoreEvent] argument.
     * This argument will help us to determine if the score has to be increased or re-set
     * when there is a winner.
     * Check [setRounds] as example.
     * */

    fun setPlayerOneScore(scoreEvent: ScoreEvent) {


    }

    fun setPlayerTwoScore(scoreEvent: ScoreEvent) {

    }

     /** As you can see, [setRounds] is called when you need to either [RoundsEvent.DecreaseRound] to rest a round or
      * when you need to [RoundsEvent.ResetRounds] for when the game is over. */

    fun setRounds(roundsEvent: RoundsEvent) {
        when(roundsEvent) {
            RoundsEvent.DecreaseRound -> {
                _rounds -= 1
            }
            RoundsEvent.ResetRounds -> {
                _rounds = 6
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
    object ResetRounds: RoundsEvent()
    object DecreaseRound: RoundsEvent()
}

sealed class ScoreEvent {
    data class IncreaseScore(val score: Int): ScoreEvent()
    object ResetScore: ScoreEvent()
}