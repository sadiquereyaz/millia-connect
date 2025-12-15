package com.reyaz.feature.portal.presentation


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.reyaz.feature.portal.domain.model.PromoCard
import com.reyaz.feature.portal.domain.model.defaultPromoCard
import com.reyaz.feature.portal.domain.model.handlePromoAction
import com.reyaz.feature.portal.presentation.components.GradientPromoCard
import com.reyaz.feature.portal.presentation.components.LoginFormComposable

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PortalScreen(
    modifier: Modifier = Modifier,
    viewModel: PortalViewModel,
    navController: NavController,
    dismissDialog: () -> Unit,
//    showSnackBar: (String, (() -> Unit)?) -> Job,
) {
    /*LaunchedEffect(uiState.errorMsg) {
        uiState.errorMsg?.let {
            showSnackBar(it){
                viewModel.retry()
            }
        }
    }*/
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.weight(1f))

        GradientPromoCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            promoCard = uiState.promoCard ?: defaultPromoCard,
            onActionClick = { action ->
                handlePromoAction(
                    action = action,
                    navController = navController,
                    context = context
                )
            }
        )

        Spacer(Modifier.weight(1f))

        LoginFormComposable(
            modifier = Modifier.padding(16.dp),
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