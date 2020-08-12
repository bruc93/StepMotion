package com.bignerdranch.android.stepmotion

import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.round

class StepMotionViewModel:ViewModel() {
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var kcalBurnt = 0f
    private var distance = 0f;

    override fun onCleared() {
        super.onCleared()
        Log.i("StepMotionViewModel", "StepMotionViewModel destroyed!")
    }

    public fun getKcalBurnt():Float{
            kcalBurnt = getCurrentStep() * 0.04f
            return kcalBurnt
    }

    public fun getDistanceWalkedKM():Float{
            distance = getCurrentStep() * 70
            return distance / 100000
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