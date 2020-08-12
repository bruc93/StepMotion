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


class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null

    private lateinit var viewModel:StepMotionViewModel

    private lateinit var textStepCount:TextView
    private lateinit var textKcalBurnt:TextView
    private lateinit var textDistanceWalked:TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(StepMotionViewModel::class.java)

        textStepCount = findViewById(R.id.step_count)
        textKcalBurnt = findViewById(R.id.kcal_text)
        textDistanceWalked = findViewById(R.id.km_text)

        //textDistanceWalked.text = viewModel.getDistanceWalkedKM().toString() + " KM"
        textDistanceWalked.text = String.format("%.2f", viewModel.getDistanceWalkedKM()) + " KM"
        textKcalBurnt.text = viewModel.getKcalBurnt().toInt().toString() + " KCAL"
        textStepCount.text = viewModel.getTotalStep().toInt().toString();

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

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        if(viewModel.getIfRunning())
        {
            viewModel.setTotalStep(event.values[0])

            textStepCount.text =  viewModel.getCurrentStep().toInt().toString()
            textKcalBurnt.text = viewModel.getKcalBurnt().toInt().toString() + " KCAL"
            textDistanceWalked.text = String.format("%.2f", viewModel.getDistanceWalkedKM()) + " KM"
            progress_circular.apply {
                setProgressWithAnimation(viewModel.getCurrentStep())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun resetSteps(){
        textStepCount.setOnClickListener{
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }
        textStepCount.setOnLongClickListener{
            viewModel.setPreviousTotalSteps(viewModel.getTotalStep())
            textStepCount.text = 0.toString()
            textKcalBurnt.text = 0.toString() + " KCAL"
            textDistanceWalked.text = 0.toString() + " KM"
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