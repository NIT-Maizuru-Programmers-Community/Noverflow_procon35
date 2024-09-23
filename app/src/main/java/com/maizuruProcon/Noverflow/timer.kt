package com.maizuruProcon.Noverflow

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.RelativeLayout
import android.view.MotionEvent

class timer : Fragment() {
    private lateinit var timerText: TextView
    private var countDownTimer: CountDownTimer? = null
    private val startTimeInMillis: Long = 30 * 60 * 1000 // 30 minutes in milliseconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.timer, container, false)
        timerText = view.findViewById(R.id.timer_text)

        // ドラッグ可能にするためのリスナーを設定
        timerText.setOnTouchListener(DragTouchListener())

        startTimer()
        return view
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(startTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                timerText.text = String.format("残り時間:%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerText.text = "残り時間:00:00"
            }
        }.start()
    }

    private inner class DragTouchListener : View.OnTouchListener {
        private var dX = 0f
        private var dY = 0f

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    view.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
                }
                else -> return false
            }
            return true
        }
    }
}