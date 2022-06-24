package com.cumpatomas.rolldice.domain

class GetRandomDiceNumberUseCase {
    operator fun invoke() = (1 .. 6).shuffled().random()
}