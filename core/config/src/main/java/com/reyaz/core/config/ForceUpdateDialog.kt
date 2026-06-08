package com.reyaz.core.config

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.reyaz.core.common.utils.openUrl

@Composable
fun ForceUpdateDialog(
    message: String,
) {
    val context = LocalContext.current
    AlertDialog(
        title = {
            Text(
                text = "Update Required",
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        },
        text = {
            Text(
                text = message,
                textAlign = TextAlign.Justify,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        },
        containerColor = MaterialTheme.colorScheme.errorContainer,
        dismissButton = {},
        onDismissRequest = {},
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                onClick = { context.openUrl("https://play.google.com/store/apps/details?id=${context.packageName}") }) {
                Text("Update")
            }
        },
    )
}
