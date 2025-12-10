package com.reyaz.feature.portal.presentation


import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.core.ui.components.text_field.CustomCircularTextField
import com.reyaz.core.ui.components.button.MCOutlineButton
import com.reyaz.core.ui.components.text_field.CustomSlimTextField
import com.reyaz.feature.portal.presentation.components.GradientPromoCard
import com.reyaz.feature.portal.presentation.components.LoginFormComposable

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PortalScreen(
    modifier: Modifier = Modifier,
    viewModel: PortalViewModel,
    dismissDialog: () -> Unit,
//    showSnackBar: (String, (() -> Unit)?) -> Job,
) {
    /*val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(uiState.errorMsg) {
        uiState.errorMsg?.let {
            showSnackBar(it){
                viewModel.retry()
            }
        }
    }*/
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.weight(1f))
        GradientPromoCard(
            title = "Got a Minute?",
            bodyText = "Help us shape the future of this app! Take a quick 60-second survey to let us know how we are doing. Your input makes a huge difference.",
            primaryBtnText = "Feedback",
            secondaryText = "Rate Now!",
            onPrimaryBtnClick = {},
            onSecondaryTextClick = {},
            modifier = Modifier.padding(16.dp)
        )

        Spacer(Modifier.weight(1f))

        LoginFormComposable(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
            uiState = uiState,
            viewModel = viewModel
        )
    }
    /*Dialog(onDismissRequest = {dismissDialog() }) {
        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
            ) {
                HorizontalDivider(
                    thickness = 1.5.dp,
                    modifier = Modifier.padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.outline
                )
                CaptivePortalDialogContent(viewModel = viewModel)
            }
            // close icon
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { dismissDialog() }
                    .padding(8.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error),
                imageVector = Icons.Default.Clear, contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onError
            )
            // dialog headline
            Text(
                text = "Wifi Login",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(4.dp),
                fontWeight = FontWeight.SemiBold
            )
            if (viewModel.uiState.collectAsState().value.isLoading)
                TranslucentLoader(modifier = modifier.matchParentSize())
        }
    }*/
}