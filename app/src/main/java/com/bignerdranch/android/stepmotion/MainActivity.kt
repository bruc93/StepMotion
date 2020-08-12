package com.bignerdranch.android.stepmotion

import android.content.Context
import android.content.SharedPreferences
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


class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null

    private lateinit var viewModel:StepMotionViewModel

    private lateinit var tv_stepsTaken:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(StepMotionViewModel::class.java)

        tv_stepsTaken = findViewById(R.id.step_count)
        tv_stepsTaken.text = viewModel.getTotalStep().toString();

        loadData()

        resetSteps()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onResume() {
        super.onResume()
        viewModel.setRunning(true)
        
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null){
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_LONG)

        }else{
            sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI)
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(viewModel.getIfRunning())
        {
            viewModel.setTotalStep(event.values[0])

            tv_stepsTaken.text =  viewModel.getCurrentStep().toString()

            progress_circular.apply {
                setProgressWithAnimation(viewModel.getCurrentStep())
            }
        }
    }

    fun resetSteps(){
        tv_stepsTaken.setOnClickListener{
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }
        tv_stepsTaken.setOnLongClickListener{
            viewModel.setPreviousTotalSteps(viewModel.getTotalStep())
            tv_stepsTaken.text = 0.toString()

            saveData()

            progress_circular.apply {
                setProgressWithAnimation(0f)
            }

            true
        }
    }

    private fun saveData(){
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putFloat("key1", viewModel.getPreviousTotalSteps())
        editor.apply()
    }
    private fun loadData(){
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        Log.d("MainActivity", "$savedNumber")
        viewModel.setPreviousTotalSteps(savedNumber)
    }
}