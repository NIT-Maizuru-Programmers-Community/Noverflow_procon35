package com.maizuruProcon.Noverflow

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroupclass TimerFragment : Fragment() {
    private lateinit var timerText: TextView
    private var countDownTimer: CountDownTimer? = null
    private val startTimeInMillis: Long = 30 * 60 * 1000 // 30分をミリ秒で設定

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.timer, container, false)
        timerText = view.findViewById(R.id.timer_text)
        timerText.text = "QRは利用できません" // 初期状態
        return view
    }

    fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(startTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                timerText.text = String.format("利用可能時間:%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerText.text = "QRは利用できません"
            }
        }.start()
    }
}
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.view.MotionEvent

