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

    private lateinit var gestureDetector: GestureDetector
    private lateinit var backgroundMusic: MediaPlayer
    private var playbackParams: PlaybackParams? = null
    private lateinit var winSound: MediaPlayer
    private lateinit var failSound: MediaPlayer

    private lateinit var speedUp: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_game)


        gestureDetector = GestureDetector(this,GestureListener())
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music)
        backgroundMusic.setVolume(0.3f, 0.3f)
        backgroundMusic.isLooping = true

        playbackParams = backgroundMusic.playbackParams
        playbackParams?.setSpeed(1f)
        backgroundMusic.playbackParams = playbackParams!!
        backgroundMusic.start()

        winSound = MediaPlayer.create(this, R.raw.positive_sound)
        failSound = MediaPlayer.create(this, R.raw.negative_sound)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    override fun onPause()
    {
        super.onPause()
        backgroundMusic.pause()
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

    public fun onFailButtonClick(view: View?)
    {
        failSound.start()
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
            if(velocityY < -1000 && velocityX > -1000 && velocityX < 1000)
            {
                showToast("Fling detected!")
                return true
            }

            return false
        }
    }
}