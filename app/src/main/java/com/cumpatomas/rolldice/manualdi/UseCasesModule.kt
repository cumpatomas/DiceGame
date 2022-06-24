package com.cumpatomas.rolldice.manualdi

import com.cumpatomas.rolldice.domain.AssignFirstPlayerTurnUseCase
import com.cumpatomas.rolldice.domain.GetRandomDiceNumberUseCase

object UseCasesModule {
    private val assignFirstPlayerTurnUseCase = AssignFirstPlayerTurnUseCase()
    private val getRandomDiceNumberUseCase = GetRandomDiceNumberUseCase()

    fun getAssignFirstPlayerTurnUseCase() = assignFirstPlayerTurnUseCase
    fun getRandomDiceNumberUseCase() = getRandomDiceNumberUseCase
}