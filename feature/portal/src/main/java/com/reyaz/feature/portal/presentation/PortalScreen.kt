package com.reyaz.feature.portal.presentation


import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.reyaz.core.ui.components.ConfettiEffect
import com.reyaz.feature.portal.domain.model.defaultPromoCard
import com.reyaz.feature.portal.presentation.components.LoginFormComposable
import com.reyaz.feature.portal.presentation.components.PromoCarousel

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
    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isConnected) {
            ConfettiEffect()
        }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.weight(1f))

            PromoCarousel(
                promoCards = uiState.promoCard + defaultPromoCard,
                navController = navController
            )

            Spacer(Modifier.weight(1f))

            LoginFormComposable(
                modifier = Modifier.padding(16.dp),
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}