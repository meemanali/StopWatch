package com.internsavyInternee.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import com.internsavyInternee.stopwatch.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var seconds = 1

        val handlerThread = HandlerThread("MyBackgroundThread").apply { start() }
        val handler =
            Handler(handlerThread.looper) // we can just simply use val handler = Handler() but that handler will run on main thread but this will run on separate thread

        // we have to declare runnable before using it in handler
        val runnable = object : Runnable {
            override fun run() {

                val s = seconds % 60
                val m = seconds / 60
                val h = m / 60

                // we must update ui in main thread otherwise many problems may occur eg app crashing
                runOnUiThread {
                    binding.textViewCounter.text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d:%02d",
                        h,
                        m,
                        s
                    ) //"%02d:%02d:%02d"
                }

                seconds++
                handler.postDelayed(this, 1000)
            }
        }

        binding.btnStart.setOnClickListener {
            handler.post(runnable)
        }

        binding.btnStop.setOnClickListener {
            handler.removeCallbacks(runnable)

            seconds = 1
            binding.textViewCounter.text = getString(R.string.zeroTime)
        }

        binding.btnHold.setOnClickListener {
            handler.removeCallbacks(runnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        HandlerThread("MyBackgroundThread").quit()
    }
}