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

private const val TAG = "MainActivity.kt"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setDefaultImage()
        initListeners()
    }

    private fun initListeners() {
        binding.btnRollDice.setOnClickListener { rollDice() }
    }

    private fun rollDice() {
        binding.motionLayout.transitionToStart()
        binding.motionLayout.transitionToEnd()
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                val resourceId = when(Random.nextInt(1,7)) {
                    1 -> R.drawable.dice_1
                    2 -> R.drawable.dice_2
                    3 -> R.drawable.dice_3
                    4 -> R.drawable.dice_4
                    5 -> R.drawable.dice_5
                    else -> { R.drawable.dice_6 }
                }
                binding.ivDice.setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, resourceId))
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                val resourceId = when(Random.nextInt(1,7)) {
                    1 -> R.drawable.dice_1
                    2 -> R.drawable.dice_2
                    3 -> R.drawable.dice_3
                    4 -> R.drawable.dice_4
                    5 -> R.drawable.dice_5
                    else -> { R.drawable.dice_6 }
                }
                binding.ivDice.setImageDrawable(AppCompatResources.getDrawable(this@MainActivity, resourceId))
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                TODO("Not yet implemented")
            }

        })
        val resourceId = when(Random.nextInt(1,7)) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> { R.drawable.dice_6 }
        }
        binding.ivDice.setImageDrawable(AppCompatResources.getDrawable(this, resourceId))
    }

    private fun setDefaultImage() {
        val drawable = AppCompatResources.getDrawable(this, R.drawable.dice_1)
        binding.ivDice.setImageDrawable(drawable)
    }
}