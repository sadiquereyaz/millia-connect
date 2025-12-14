package com.reyaz.feature.portal.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.core.ui.components.text_field.CustomSlimTextField
import com.reyaz.feature.portal.presentation.PortalUiState
import com.reyaz.feature.portal.presentation.PortalViewModel
import kotlinx.coroutines.launch

@Composable
internal fun LoginFormComposable(
    modifier: Modifier,
    uiState: PortalUiState,
    viewModel: PortalViewModel
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomSlimTextField(
            modifier = Modifier.height(42.dp),
            value = uiState.username,
            onValueChange = { viewModel.updateUsername(it) },
            label = "Username",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
        )
        CustomSlimTextField(
            modifier = Modifier.height(42.dp),
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = "Password",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
//                    scope.launch {
                    viewModel.onLoginClick()
//                    }
                }
            )
        )

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = (Arrangement.End)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = if (uiState.autoConnect) "Your device will automatically connect when session expired." else "Auto Connect",
                textAlign = TextAlign.End,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 14.sp
            )
            Spacer(Modifier.width(16.dp))
            Switch(
                modifier = Modifier,
                checked = uiState.autoConnect,
                onCheckedChange = { viewModel.updateAutoConnect(it) },
                colors = SwitchDefaults.colors()
            )
        }

        // login btn
        Column {
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onLoginClick()
                },
                modifier = Modifier
                    .height(48.dp)
                    .padding(4.dp, 12.dp, 4.dp, 0.dp)
//                .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                enabled = uiState.loginBtnEnabled
            ) {
                if (uiState.isLoading) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            trackColor = MaterialTheme.colorScheme.onSurface
                        )
                        Text("Logging In...", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text("Login")
                }
            }

            //logout btn
            OutlinedButton(
                modifier = Modifier
                    .height(48.dp)
                    .padding(4.dp, 12.dp, 4.dp, 0.dp)
                    .fillMaxWidth(),
                onClick = { viewModel.logout() }) {
                Text("Logout", color = MaterialTheme.colorScheme.onSurface)
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            uiState.supportingText?.let {
                Text(
                    text = it,
                    color = if (uiState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                    fontWeight = if (uiState.isError) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 16.sp,
                    //modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}