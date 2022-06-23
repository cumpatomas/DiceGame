package com.cumpatomas.rolldice.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.rolldice.domain.AssignFirstPlayerTurn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {

    private val assignFirstPlayerTurn = AssignFirstPlayerTurn()

    private val _mutableStateFlow = MutableStateFlow(3)
    val mutableStateFlow = _mutableStateFlow.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    private val _firstPlayerTurn = Channel<Int>()
    val firstPlayerTurn = _firstPlayerTurn.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            _firstPlayerTurn.send(assignFirstPlayerTurn.invoke())
        }
    }

    fun getRandomTurn() = viewModelScope.launch { _firstPlayerTurn.send(assignFirstPlayerTurn.invoke()) }


}