package com.smorzhok.financeapp.ui.screen.setting

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun InfoScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val packageName = context.packageName

    val versionName = try {
        context.packageManager.getPackageInfo(packageName, 0).versionName
    } catch (_: PackageManager.NameNotFoundException) {
        "N/A"
    }

    val lastUpdateTime = try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val updateTime = packageInfo.lastUpdateTime
        val formatter = SimpleDateFormat("HH:mm, dd.MM", Locale.getDefault())
        formatter.format(Date(updateTime))
    } catch (_: Exception) {
        "N/A"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = paddingValues.calculateTopPadding()+8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "О приложении",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        InfoRow(label = "Версия:", value = versionName ?: "0")
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(label = "Последнее обновление:", value = lastUpdateTime)
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}