package com.group25.timebanking.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val data = MutableLiveData<Int>()

    fun data(item: Int) {
        data.value = item
    }
}