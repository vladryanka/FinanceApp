package com.smorzhok.financeapp.ui.commonitems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smorzhok.financeapp.data.datastore.HapticPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HapticViewModel @Inject constructor(
    private val hapticPref: HapticPreference
) : ViewModel() {

    private val _hapticEnabled = MutableStateFlow(false)
    val hapticEnabled: StateFlow<Boolean> = _hapticEnabled

    private val _hapticEffect = MutableStateFlow("CLICK")
    val hapticEffect: StateFlow<String> = _hapticEffect

    init {
        viewModelScope.launch {
            hapticPref.hapticFlow.collect {
                _hapticEnabled.value = it.first
                it.second?.let { effect -> _hapticEffect.value = effect }
            }
        }
    }

    fun toggleHaptic(enabled: Boolean) {
        viewModelScope.launch { hapticPref.setHapticEnabled(enabled) }
    }

    fun setEffect(effect: String) {
        viewModelScope.launch { hapticPref.setEffect(effect) }
    }
}
