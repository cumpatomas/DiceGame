package com.cumpatomas.rolldice.domain

class AssignFirstPlayerTurn {
    operator fun invoke() = (1 .. 2).shuffled().random()
}