package com.example.cdc.ui.dataProcessing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataProcessingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is DataProcessing Fragment"
    }
    val text: LiveData<String> = _text
}