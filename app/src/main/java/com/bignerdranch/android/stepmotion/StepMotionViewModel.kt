package com.bignerdranch.android.stepmotion

import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.activity_main.*

class StepMotionViewModel:ViewModel() {
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f



    override fun onCleared() {
        super.onCleared()
        Log.i("StepMotionViewModel", "StepMotionViewModel destroyed!")
    }




    public fun getTotalStep():Float{
        return totalSteps
    }

    public fun setTotalStep(_total:Float){
        totalSteps = _total
    }

    public fun getCurrentStep():Float
    {
        return totalSteps - previousTotalSteps
    }

    public fun setRunning(_running:Boolean){
        running = _running
    }

    public fun getIfRunning():Boolean{
        return running
    }

    public fun setPreviousTotalSteps(_steps:Float){
            previousTotalSteps = _steps
    }

    public fun getPreviousTotalSteps():Float{
        return previousTotalSteps
    }

}