package com.cumpatomas.rolldice.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.cumpatomas.rolldice.R
import com.cumpatomas.rolldice.databinding.ActivityMainBinding
import com.cumpatomas.rolldice.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainActivity.kt"
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var playerTurn = 0
    private var playerOneScore = 0
    private var playerTwoScore = 0
    private var rounds = 6
    private var randomNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setDefaultImage()
        initListeners()
        initObservers()
        setInitialScores()
        setInitialRounds()

    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.firstPlayerTurn.collectLatest { turn ->
                assignFirstPlayerTurn(turn)
            }
        }
    }

    private fun setInitialRounds() {
        binding.tvRoundsLeft.text = resources.getString(R.string.rounds_left, rounds.toString())
    }

    private fun setInitialScores() {
        binding.tvScorePlayerOne.text = resources.getString(R.string.score_placeholder, playerOneScore.toString())
        binding.tvScorePlayerTwo.text = resources.getString(R.string.score_placeholder, playerTwoScore.toString())
    }

    private fun assignFirstPlayerTurn(turn: Int) {
        when(turn) {
            1 -> {
                playerTurn = 1
                binding.playerOneContainer.background = AppCompatResources.getDrawable(this, R.drawable.active_turn_background)
                binding.playerTwoContainer.background = AppCompatResources.getDrawable(this, R.drawable.player_background)
                binding.tvPlayer1Turn.isVisible = true
                binding.tvPlayer2Turn.isGone = true

            }
            2 -> {
                playerTurn = 2
                binding.playerTwoContainer.background = AppCompatResources.getDrawable(this, R.drawable.active_turn_background)
                binding.playerOneContainer.background = AppCompatResources.getDrawable(this, R.drawable.player_background)
                binding.tvPlayer2Turn.isVisible = true
                binding.tvPlayer1Turn.isGone = true
            }
        }
    }

    private fun initListeners() {
        binding.ivDice.setOnClickListener { rollDice() }
        binding.btnRestart.setOnClickListener { restartGame() }
    }

    private fun restartGame() {
        viewModel.getRandomTurn()
        playerOneScore = 0
        playerTwoScore = 0
        setInitialScores()
        rounds = 5
        setInitialRounds()
        binding.ivWinnerPlayerTwo.isGone = true
        binding.ivWinnerPlayerOne.isGone = true
        binding.btnRestart.isGone = true
        binding.ivDice.isVisible = true
        binding.tvIntro.text = resources.getString(R.string.tap_the_dice_to_start)
        binding.tvIntro.isVisible = true
        binding.confetti.isGone = true

    }

    private fun rollDice() {
        binding.tvIntro.isGone = true
        binding.rootLayout.transitionToEnd()
        binding.rootLayout.transitionToStart()
        binding.rootLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                //do nothing
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                randomNumber = (1 .. 6).shuffled().random()
                val resourceId = when(randomNumber) {
                    1 -> R.drawable.dice_1
                    2 -> R.drawable.dice_2
                    3 -> R.drawable.dice_3
                    4 -> R.drawable.dice_4
                    5 -> R.drawable.dice_5
                    else -> R.drawable.dice_6
                }
                setDice(resourceId)
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                setPlayerScore(randomNumber)
                togglePlayerTurn()
                restRoundsLeft()
                checkWinner()
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                // do nothing.
            }

        })
    }

    private fun checkWinner() {
        if (rounds == 0) {
            if (playerOneScore > playerTwoScore) {
                binding.ivWinnerPlayerOne.isVisible = true
                binding.ivWinnerPlayerOne.winnerAnimation()
                binding.confetti.confettiAnimation()
                binding.tvIntro.text = resources.getString(R.string.player1wins)
                binding.tvIntro.isVisible = true
            }
            if (playerTwoScore > playerOneScore) {
                binding.ivWinnerPlayerTwo.isVisible = true
                binding.ivWinnerPlayerTwo.winnerAnimation()
                binding.confetti.confettiAnimation()
                binding.tvIntro.text = resources.getString(R.string.player2wins)
                binding.tvIntro.isVisible = true
            }
            if (playerOneScore == playerTwoScore) {
                binding.ivWinnerPlayerOne.isVisible = true
                binding.ivWinnerPlayerTwo.isVisible = true
                binding.ivWinnerPlayerOne.winnerAnimation()
                binding.ivWinnerPlayerTwo.winnerAnimation()
                binding.confetti.confettiAnimation()
                binding.tvIntro.text = resources.getString(R.string.draw)
                binding.tvIntro.isVisible = true
            }
            binding.ivDice.isGone = true
            binding.btnRestart.isVisible = true
        }
    }

    private fun restRoundsLeft() {
        if (rounds != 0) {
            rounds -= 1
            binding.tvRoundsLeft.text = resources.getString(R.string.rounds_left, rounds.toString())
        }
    }

    private fun togglePlayerTurn() {
        if (playerTurn == 1) {
            playerTurn = 2
            binding.playerOneContainer.background = AppCompatResources.getDrawable(this, R.drawable.player_background)
            binding.playerTwoContainer.background = AppCompatResources.getDrawable(this, R.drawable.active_turn_background)
            binding.tvPlayer2Turn.isVisible = true
            binding.tvPlayer1Turn.isGone = true
        } else {
            playerTurn = 1
            binding.playerTwoContainer.background = AppCompatResources.getDrawable(this, R.drawable.player_background)
            binding.playerOneContainer.background = AppCompatResources.getDrawable(this, R.drawable.active_turn_background)
            binding.tvPlayer1Turn.isVisible = true
            binding.tvPlayer2Turn.isGone = true
        }
    }

    private fun setDice(resourceId: Int) {
        binding.ivDice.setImageDrawable(AppCompatResources.getDrawable(this, resourceId))
    }

    private fun setPlayerScore(randomNumber: Int) {
        if (playerTurn == 1) {
            playerOneScore += randomNumber
            binding.tvScorePlayerOne.text = resources.getString(R.string.score_placeholder, playerOneScore.toString())
        } else {
            playerTwoScore += randomNumber
            binding.tvScorePlayerTwo.text = resources.getString(R.string.score_placeholder, playerTwoScore.toString())
        }
    }

    private fun setDefaultImage() {
        val drawable = AppCompatResources.getDrawable(this, R.drawable.dice_1)
        binding.ivDice.setImageDrawable(drawable)
    }

    private fun LottieAnimationView.winnerAnimation() {
        setAnimation(R.raw.trophy)
        playAnimation()
        repeatCount = 10
    }

    private fun LottieAnimationView.confettiAnimation() {
        binding.tvPlayer1Turn.isGone = true
        binding.tvPlayer2Turn.isGone = true
        isVisible = true
        bringToFront()
        setAnimation(R.raw.confetti)
        playAnimation()
        repeatCount = 2
    }
}