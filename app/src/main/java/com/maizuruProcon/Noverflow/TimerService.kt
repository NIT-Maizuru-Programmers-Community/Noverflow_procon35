package com.maizuruProcon.Noverflow

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class TimerService : Service() {

    private var countDownTimer: CountDownTimer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // タイマーを開始
        startTimer()
        return START_STICKY // サービスが終了しても再起動されるようにする
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(5 * 60 * 1000, 1000) { // 5分間のタイマー
            override fun onTick(millisUntilFinished: Long) {
                // 1秒ごとの処理
                Log.d("TimerService", "Remaining time: $millisUntilFinished")
            }

            override fun onFinish() {
                // タイマー終了時の処理
                Log.d("TimerService", "Timer finished")

                // タイマー終了をブロードキャストで通知
                val broadcastIntent = Intent("TIMER_FINISHED")
                sendBroadcast(broadcastIntent)

                // タイマーを再スタート
                startTimer()
            }
        }.start()
    }

    override fun onBind(intent: Intent?): IBinder? {
        // バインドは必要ないのでnullを返す
        return null
    }

    override fun onDestroy() {
        // サービスが終了されたときにタイマーを停止
        countDownTimer?.cancel()
        super.onDestroy()
    }
}
