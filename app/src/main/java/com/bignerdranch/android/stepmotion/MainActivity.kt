package com.bignerdranch.android.stepmotion

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*


class MainActivity : AppCompatActivity() {
    //private val viewModel2 by viewModels<StepMotionViewModel>()
    private lateinit var viewModel:StepMotionViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(StepMotionViewModel::class.java)

        loadData()

        val mainFragment = MainFragment()
        val meFragment = MeFragment()
        val statsFragment = StatsFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, mainFragment)
            commit()
        }

        buttonMe.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, meFragment)
                commit()
            }
        }

        buttonHealth.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, mainFragment)
                commit()
            }
        }

        buttonStats.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, statsFragment)
                commit()
            }
        }


    }

    private fun loadData()
    {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences?.getFloat("key1", 0f)
        Log.d("MainActivity", "$savedNumber")
        if (savedNumber != null) {
            viewModel.setPreviousTotalSteps(savedNumber)
        }
    }
}