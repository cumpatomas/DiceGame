package com.cumpatomas.rolldice.ui

import android.os.Bundle
import android.util.Log
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
import com.cumpatomas.rolldice.ui.viewmodel.RoundsEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainActivity.kt"
class MainActivity : AppCompatActivity() {

    private val model: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding


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
        logging("on create")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "On start")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "on resume")
    }

    override fun onPause() {
        super.onPause()
        logging("on pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "on stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "on destroy")
    }

    private fun logging(message: String) {
        Log.d(TAG, message)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            model.startPlayerTurn.collectLatest { turn ->
                togglePlayerTurn(turn)
            }

        }
    }

    private fun setInitialRounds() {
        binding.tvRoundsLeft.text = resources.getString(R.string.rounds_left, model.rounds.toString())
    }

    private fun setInitialScores() {
        binding.tvScorePlayerOne.text = resources.getString(R.string.score_placeholder, model.playerOneScore.toString())
        binding.tvScorePlayerTwo.text = resources.getString(R.string.score_placeholder, model.playerTwoScore.toString())
    }

    private fun togglePlayerTurn(turn: Int) {
        when(turn) {
            1 -> {
                binding.playerOneContainer.background = AppCompatResources.getDrawable(this, R.drawable.active_turn_background)
                binding.playerTwoContainer.background = AppCompatResources.getDrawable(this, R.drawable.player_background)
                binding.tvPlayer1Turn.isVisible = true
                binding.tvPlayer2Turn.isGone = true

            }
            2 -> {
                binding.playerTwoContainer.background = AppCompatResources.getDrawable(this, R.drawable.active_turn_background)
                binding.playerOneContainer.background = AppCompatResources.getDrawable(this, R.drawable.player_background)
                binding.tvPlayer2Turn.isVisible = true
                binding.tvPlayer1Turn.isGone = true
            }
        }
        model.setPlayerTurn(turn)
    }

    private fun initListeners() {
        binding.ivDice.setOnClickListener { rollDice() }
        binding.btnRestart.setOnClickListener { restartGame() }
    }

    private fun restartGame() {
        model.getRandomTurn()
        model.setPlayerOneScore(score = 0)
        model.setPlayerTwoScore(score = 0)
        setInitialScores()
        model.setRounds(rounds = 6)
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
                setDice(model.getRandomNumber())
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                setPlayerScore(model.randomNumber)
                togglePlayerTurn(model.playerTurn)
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
        with(model) {
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
    }

    private fun restRoundsLeft() {
        if (model.rounds != 0) {
            model.setRounds(RoundsEvent.DecreaseRound)
            binding.tvRoundsLeft.text = resources.getString(R.string.rounds_left, model.rounds.toString())
        }
    }

    /*private fun togglePlayerTurn() {
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
    }*/

    private fun setDice(resourceId: Int) {
        binding.ivDice.setImageDrawable(AppCompatResources.getDrawable(this, resourceId))
    }

    private fun setPlayerScore(randomNumber: Int) {
        if (model.playerTurn == 1) {
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