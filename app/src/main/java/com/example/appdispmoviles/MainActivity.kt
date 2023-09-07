package com.example.appdispmoviles

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.security.AccessController.getContext


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
    }

}