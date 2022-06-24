package com.cumpatomas.rolldice.domain

class AssignFirstPlayerTurnUseCase {
    operator fun invoke() = (1 .. 2).shuffled().random()
}