package com.maizuruProcon.Noverflow

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class TimerService : Service() {

    private var countDownTimer: CountDownTimer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startTimer()// タイマーを開始
        return START_STICKY // サービスが終了しても再起動されるようにする
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(5 * 60 * 1000, 1000) { // 5分間のタイマー
            override fun onTick(millisUntilFinished: Long) {
                Log.d("TimerService", "Remaining time: $millisUntilFinished")// 1秒ごとの処理
            }

            override fun onFinish() {

                Log.d("TimerService", "Timer finished")// タイマー終了時の処理

                val broadcastIntent = Intent("TIMER_FINISHED")// タイマー終了をブロードキャストで通知
                sendBroadcast(broadcastIntent)

                startTimer()// タイマーを再スタート
            }
        }.start()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null// バインドは必要ないのでnullを返す
    }

    override fun onDestroy() {
        countDownTimer?.cancel()// サービスが終了されたときにタイマーを停止
        super.onDestroy()
    }
}
