package com.cumpatomas.dicegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import com.cumpatomas.dicegame.databinding.ActivityMainBinding

private const val TAG = "MainActivity.kt"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var playerTurn = 0
    private var playerOneScore = 0
    private var playerTwoScore = 0
    private var rounds = 5
    private var randomNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setDefaultImage()
    }

    private fun setDefaultImage() {
        val drawable = AppCompatResources.getDrawable(this, R.drawable.dice_1)
        binding.ivDice.setImageDrawable(drawable)

    }
}