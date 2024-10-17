package com.maizuruProcon.Noverflow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GarbageViewModel : ViewModel() {
    private val _counts = MutableLiveData<Map<String, Int>>(mutableMapOf())
    val counts: LiveData<Map<String, Int>> get() = _counts

    // カウントの更新
    fun updateCounts(key: String, value: Int) {
        val currentCounts = (_counts.value ?: mutableMapOf()).toMutableMap()
        currentCounts[key] = value
        _counts.value = currentCounts

        // ログを出力
        Log.d("GarbageViewModel", "Updated counts - Key: $key, Value: $value")
    }

    // カウントの一括更新
    fun updateCounts(newCounts: Map<String, Int>) {
        _counts.value = newCounts

        // 一括更新後、各キーと値をログに出力
        newCounts.forEach { (key, value) ->
            Log.d("GarbageViewModel", "Updated counts - Key: $key, Value: $value")
        }
    }

    // カウントのリセット
    fun resetCounts() {
        val resetValues = mapOf(
            "burningGarbage" to 0,
            "plasticGarbage" to 0,
            "bottles" to 0,
            "cans" to 0
        )
        _counts.value = resetValues

        // リセット後の各キーと値をログに出力
        resetValues.forEach { (key, value) ->
            Log.d("GarbageViewModel", "Reset counts - Key: $key, Value: $value")
        }
    }

    // ViewModelからデータを取得し、ログに出力する
    fun getCountsFromViewModel(
        viewModel: GarbageViewModel,
        onCountsRetrieved: (Map<String, Int>) -> Unit
    ) {
        viewModel.counts.observeForever { countsMap ->
            countsMap?.let {
                Log.d("GarbageViewModel", "Retrieved counts: $it")
                onCountsRetrieved(it)
            }
        }
    }
}
