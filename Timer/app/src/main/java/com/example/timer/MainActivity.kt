package com.example.timer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.timer.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    companion object {
        private const val STOPPED: String = "stopped"
        private const val ACTIVATED: String = "activated"
        private const val PAUSED: String = "paused"
    }

    private lateinit var bind: ActivityMainBinding
    private var state: String = STOPPED
    private var counter = 40
    private var counterMemory = counter
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.seekBarTimer.setOnSeekBarChangeListener(this)
        updateCounter()
        updateButtons()
        loadOrientationDependencies()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putInt("counter", counter)
            putString("state", getState())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        counter = savedInstanceState.getInt("counter", counter)
        setState(savedInstanceState.getString("state", getState()))
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        loadOrientationDependencies()
    }

    private suspend fun timer() {
        repeat(counter) {
            while (getState() == ACTIVATED) {
                delay(1_000L)
                counter--
                updateCounter()
                setState()
            }
        }
    }

    private fun runTimer() {
        scope.launch {
            timer()
        }
    }

    private fun stopTimer() {
        scope.coroutineContext.cancelChildren()
        updateCounter(counterMemory)
    }

    private fun pauseTimer() {
        scope.coroutineContext.cancelChildren()
        updateCounter()
        updateButtons()
    }

    private fun updateCounter() {
        bind.textCounter.text = counter.toString()
        bind.progressBarTimer.progress = counter
        bind.seekBarTimer.progress = counter
    }

    private fun updateCounter(setCounter: Int) {
        bind.textCounter.text = setCounter.toString()
        bind.progressBarTimer.progress = setCounter
        counter = setCounter
        bind.seekBarTimer.progress = setCounter
    }

    private fun updateButtons() {
        when (getState()) {
            ACTIVATED -> activatedTimerWidgetsSet()
            STOPPED -> stoppedTimerWidgetsSet()
            PAUSED -> pausedTimerWidgetsSet()
        }
    }

    private fun getState() = state

    private fun setState(state: String) {
        this.state = state
        if (counter == 0) this.state = STOPPED
        updateButtons()
        Log.d("STATE", "State changed: ${getState()}")
    }

    private fun setState() {
        if (counter == 0) this.state = STOPPED
        updateButtons()
        Log.d("STATE", "State changed: ${getState()}")
    }

    private fun activatedTimerWidgetsSet() {
        bind.seekBarTimer.isEnabled = false
        bind.buttonStart.visibility = View.INVISIBLE
        bind.buttonStop.visibility = View.VISIBLE
        bind.buttonPause.visibility = View.VISIBLE
        bind.buttonStop.setOnClickListener {
            setState(STOPPED)
            stopTimer()
            updateButtons()
        }
        bind.buttonPause.setOnClickListener {
            setState(PAUSED)
            pauseTimer()
        }
    }

    private fun stoppedTimerWidgetsSet() {
        bind.buttonStart.text = getString(R.string.start)
        bind.seekBarTimer.isEnabled = true
        bind.buttonStart.visibility = View.VISIBLE
        bind.buttonPause.visibility = View.GONE
        bind.buttonStop.visibility = View.GONE
        bind.buttonStart.setOnClickListener {
            setState(ACTIVATED)
            runTimer()
            updateButtons()
            counterMemory = counter
        }
    }

    private fun pausedTimerWidgetsSet() {
        bind.buttonStart.text = getString(R.string.resume)
        bind.seekBarTimer.isEnabled = false
        bind.buttonStart.visibility = View.VISIBLE
        bind.buttonPause.visibility = View.GONE
        bind.buttonStop.visibility = View.VISIBLE

        bind.buttonStart.setOnClickListener {
            setState(ACTIVATED)
            runTimer()
            updateButtons()
        }
        bind.buttonStop.setOnClickListener {
            setState(STOPPED)
            stopTimer()
            updateButtons()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        counter = bind.seekBarTimer.progress
        updateCounter()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    private fun loadOrientationDependencies() {
        if (this.resources.configuration.orientation == 2) {
            bind.progressBarTimer.visibility = View.INVISIBLE
        } else if (this.resources.configuration.orientation == 1) {
            bind.progressBarTimer.visibility = View.VISIBLE
        }
    }
}


