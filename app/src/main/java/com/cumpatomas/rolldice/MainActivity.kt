package com.cumpatomas.rolldice

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.motion.widget.MotionLayout
import com.cumpatomas.rolldice.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.random.nextInt

private const val TAG = "MainActivity.kt"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var playerTurn = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setDefaultImage()
        initListeners()
        assignFirstPlayerTurn()
    }

    private fun assignFirstPlayerTurn() {
        val r = (1 until 3).shuffled().last()
        Log.d(TAG, r.toString())
        when(r) {
            1 -> {
                playerTurn = 1
                binding.playerOneContainer.background = AppCompatResources.getDrawable(this, R.drawable.active_turn_background)
            }
            2 -> {
                playerTurn = 2
                binding.playerTwoContainer.background = AppCompatResources.getDrawable(this, R.drawable.active_turn_background)
            }
        }
    }

    private fun initListeners() {
        binding.ivDice.setOnClickListener { rollDice() }
    }

    private fun rollDice() {

    }

    private fun setDefaultImage() {
        val drawable = AppCompatResources.getDrawable(this, R.drawable.dice_1)
        binding.ivDice.setImageDrawable(drawable)
    }
}