package com.smorzhok.financeapp.data.datastore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.core.content.edit

@Suppress("DEPRECATION")
class PinCodeManager(context: Context) {
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun savePin(pin: String) {
        sharedPreferences.edit { putString("pin_code", pin) }
    }

    fun getPin(): String? = sharedPreferences.getString("pin_code", null)

    fun isPinSet(): Boolean = sharedPreferences.contains("pin_code")

    fun clearPin() {
        sharedPreferences.edit { remove("pin_code") }
    }
}