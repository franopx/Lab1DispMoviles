package com.example.appdispmoviles

import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class SoloGame : AppCompatActivity() {

    private lateinit var backgroundMusic: MediaPlayer
    private var playbackParams: PlaybackParams? = null
    private lateinit var winSound: MediaPlayer
    private lateinit var failSound: MediaPlayer

    private lateinit var speedUp: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_game)

        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)
        backgroundMusic.setVolume(0.3f, 0.3f)
        backgroundMusic.isLooping = true

        playbackParams = backgroundMusic.playbackParams
        playbackParams?.setSpeed(1f)
        backgroundMusic.playbackParams = playbackParams!!
        backgroundMusic.start()

        winSound = MediaPlayer.create(this, R.raw.positive_sound)
    }

    override fun onPause()
    {
        super.onPause()
        backgroundMusic.stop()
    }

    override fun onResume()
    {
        super.onResume()
        backgroundMusic.start()
    }

    public fun onClearButtonClick(view: View?)
    {
        winSound.start()
    }

    private fun showToast(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean
        {
            if(velocityY > velocityX*2)
            {
                showToast("Fling Detected")
                return true
            }
            showToast("atata")
            return false
        }
    }
}