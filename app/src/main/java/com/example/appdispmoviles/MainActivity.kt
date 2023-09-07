package com.example.appdispmoviles

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickTest(view: View?)
    {
        // What does it do
    }

    fun onAboutButtonClick(view: View?)
    {
        val switchActivityIntent = Intent(this, AboutActivity::class.java)
        startActivity(switchActivityIntent)
    }

}