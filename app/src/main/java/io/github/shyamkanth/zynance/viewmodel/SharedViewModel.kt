package io.github.shyamkanth.zynance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _refreshTrigger = MutableLiveData<Boolean>()
    val refreshTrigger: LiveData<Boolean> get() = _refreshTrigger

    fun triggerRefresh() {
        _refreshTrigger.value = true
    }
}