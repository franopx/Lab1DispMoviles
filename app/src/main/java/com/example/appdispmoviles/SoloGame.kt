package com.example.appdispmoviles

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class SoloGame : AppCompatActivity() {

    private lateinit var backgroundMusic: MediaPlayer
    private lateinit var winSound: MediaPlayer
    private lateinit var failSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_game)

        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)
    }

    public fun onClearButtonClick(view: View?)
    {

    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean
        {
            return false
        }
    }
}