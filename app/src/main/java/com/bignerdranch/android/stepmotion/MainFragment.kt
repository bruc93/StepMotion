package com.bignerdranch.android.stepmotion

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_main.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private lateinit var viewModel:StepMotionViewModel
    private lateinit var textStepCount: TextView
    private lateinit var textKcalBurnt: TextView
    private lateinit var textDistanceWalked: TextView
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        viewModel = ViewModelProvider(requireActivity()).get(StepMotionViewModel::class.java)

        Log.d("fragment", "We CREATE the fragment!!!!")
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_main, container, false)

        textStepCount = view.findViewById(R.id.step_count)
        textKcalBurnt = view.findViewById(R.id.kcal_text)
        textDistanceWalked = view.findViewById(R.id.km_text)

        updateUI()

        textStepCount.setOnClickListener{
            Toast.makeText(this.activity, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }
        textStepCount.setOnLongClickListener{
            resetSteps()
            true
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        textDistanceWalked.text = String.format("%.2f", viewModel.getDistanceWalkedKM()) + " KM"
        textKcalBurnt.text = viewModel.getKcalBurnt().toInt().toString() + " KCAL"
        textStepCount.text = viewModel.getCurrentStep().toInt().toString();
        
        this.view?.progress_circular?.apply {
            setProgressWithAnimation(viewModel.getCurrentStep())
        }
    }

    @SuppressLint("ShowToast")
    override fun onResume() {
        super.onResume()
        Log.d("fragment", "We resumed the fragment!!!!")

        viewModel.setRunning(true)

        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null){
            Toast.makeText(this.activity, "No sensor detected on this device", Toast.LENGTH_LONG)

        }else{
            sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("SetTextI18n")
    public fun resetSteps(){
        viewModel.setPreviousTotalSteps(viewModel.getTotalStep())
        textStepCount.text = 0.toString()
        textKcalBurnt.text = 0.toString() + " KCAL"
        textDistanceWalked.text = 0.toString() + " KM"

        saveData()

        this.view?.progress_circular?.apply {
            setProgressWithAnimation(0f)
        }
    }

    private fun saveData(){
        val sharedPreferences = this.activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        editor?.putFloat("key1", viewModel.getPreviousTotalSteps())
        editor?.apply()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {

        var stepeer = viewModel.getCurrentStep().toInt();
        Log.d("getCurrentStep", "$stepeer")
        if(viewModel.getIfRunning())
        {
            viewModel.setTotalStep(event!!.values[0])
        }

        updateUI()

    }

}