package com.example.appdispmoviles

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.media.PlaybackParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlin.math.abs
import kotlin.random.Random

class SoloGame : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector
    private lateinit var backgroundMusic: MediaPlayer
    private var playbackParams: PlaybackParams? = null
    private lateinit var winSound: MediaPlayer
    private lateinit var failSound: MediaPlayer

    private lateinit var speedUp: Handler

    private lateinit var sensorManager: SensorManager

    private val gamePrompts = arrayOf("Shake!", "Touch!", "Fling!")
    public var currentGame = 0
    var gameTime: Long = 5000
    var gameWin = false
    lateinit var gameText: TextView
    var gameHandler: Handler = Handler(Looper.getMainLooper())
    var score: Int = 0

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

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                SensorListener(),
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST)
        }

        gameText = findViewById<TextView>(R.id.accelText)
        currentGame = abs(Random.nextInt() % 2)
        println(currentGame)
        gameText.text = gamePrompts[currentGame]
        gameText.setTextColor(Color.RED)

        val delayMillis = gameTime.toLong()
        gameHandler.postDelayed(nextGameRunnable, delayMillis)

        displayTimerHandler.postDelayed(displayTimerRunnable, displayTimerMillis)
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



    private fun showToast(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {

            println("$currentGame --- $gameWin")
            if(currentGame == 1 && !gameWin)
            {
                winGame()
            }
            return super.onSingleTapConfirmed(e)
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean
        {
            if(velocityY < -500 && velocityX > -500 && velocityX < 500)
            {
                if(currentGame == 2 && !gameWin)
                {
                    winGame()
                }
                return true
            }
            return false
        }
    }

    var gravity = floatArrayOf(0f, 0f, 0f)
    var linearAcceleration = floatArrayOf(0f, 0f, 0f)
    inner class SensorListener: SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {
            // In this example, alpha is calculated as t / (t + dT),
            // where t is the low-pass filter's time-constant and
            // dT is the event delivery rate.

            val alpha: Float = 0.8f

            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

            // Remove the gravity contribution with the high-pass filter.
            linearAcceleration[0] = event.values[0] - gravity[0]
            linearAcceleration[1] = event.values[1] - gravity[1]
            linearAcceleration[2] = event.values[2] - gravity[2]

            val threshold = 1f
            if(linearAcceleration[1] > threshold)
            {
                if(currentGame == 0 && !gameWin)
                {
                    winGame()
                }
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            //
        }
    }

    // Game

    private val nextGameRunnable = Runnable { nextGame() }
    private fun nextGame() {
        if(gameWin)
        {
            gameWin = false
            gameText.setTextColor(Color.RED)
            println("CURRENT : $currentGame")
            gameText.text = gamePrompts[currentGame]
            findViewById<TextView>(R.id.scoreText).text = "Score: $score"
            gameTime -= displayTimerMillis
            displayTime = gameTime.toFloat()/1000
            gameHandler.postDelayed(nextGameRunnable, gameTime)
        }
        else
        {
            loseGame()
        }
    }

    private fun winGame()
    {
        gameText.setTextColor(Color.GREEN)
        gameWin = true
        winSound.start()
        score += 1

        val prevGame = currentGame
        do {
            currentGame = abs(Random.nextInt() % 3)
        } while ((currentGame < 0 || currentGame > 3) && currentGame == prevGame)

        gameHandler.removeCallbacks(nextGameRunnable)
        gameHandler.postDelayed(nextGameRunnable, 100)
    }

    private fun loseGame()
    {
        failSound.start()
        showToast("Try Again! Score: $score")
        val value = 1000
        val delayMillis = value.toLong()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, delayMillis)
    }

    var displayTime: Float = gameTime.toFloat()/1000
    val displayTimerMillis: Long = 100
    val displayTimerHandler: Handler = Handler(Looper.getMainLooper())
    private val displayTimerRunnable = Runnable {
        findViewById<TextView>(R.id.timerText).text = String.format("%.1f", displayTime)
        displayTime -= 0.1f;
        displayTime = Math.max(displayTime, 0f)
        callDisplayTime()
    }

    private fun callDisplayTime()
    {
        displayTimerHandler.postDelayed(displayTimerRunnable, displayTimerMillis)
    }
}