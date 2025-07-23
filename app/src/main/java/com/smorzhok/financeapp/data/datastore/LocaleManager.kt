package com.smorzhok.financeapp.data.datastore

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleManager {
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}