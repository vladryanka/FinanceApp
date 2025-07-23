package com.smorzhok.financeapp.ui.screen.setting

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smorzhok.financeapp.MainActivity
import com.smorzhok.financeapp.R
import com.smorzhok.financeapp.data.datastore.LocaleManager
import com.smorzhok.financeapp.data.datastore.LocalePreference
import kotlinx.coroutines.launch

enum class AppLanguage(val code: String, val displayName: String) {
    RUSSIAN("ru", "Русский"),
    ENGLISH("en", "English")
}

@Composable
fun LanguageScreen(
    paddingValues: PaddingValues,
    currentLanguage: String,
    localePreference: LocalePreference
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = paddingValues.calculateTopPadding()+8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.language),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AppLanguage.entries.forEach { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        if (selectedLanguage != language.code) {
                            selectedLanguage = language.code

                            coroutineScope.launch {
                                localePreference.saveLanguage(language.code)
                                LocaleManager.setLocale(context, language.code)

                                val intent = Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                context.startActivity(intent)
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedLanguage == language.code,
                    onClick = {
                        if (selectedLanguage != language.code) {
                            selectedLanguage = language.code

                            coroutineScope.launch {
                                localePreference.saveLanguage(language.code)
                                LocaleManager.setLocale(context, language.code)

                                val intent = Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                context.startActivity(intent)
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = language.displayName)
            }
        }
    }
}