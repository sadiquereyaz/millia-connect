package com.reyaz.feature.portal.domain.model

import android.content.Context
import androidx.navigation.NavController
import com.reyaz.core.common.utils.openUrl

sealed interface DynamicUiClickAction {
    data class DeepLink(val route: String) : DynamicUiClickAction
    data class ExternalUrl(val url: String) : DynamicUiClickAction
    object None : DynamicUiClickAction
}

fun handlePromoAction(
    action: DynamicUiClickAction,
    navController: NavController,
    context: Context
) {
    when (action) {
        is DynamicUiClickAction.DeepLink ->
            navController.navigate(action.route)

        is DynamicUiClickAction.ExternalUrl ->
            context.openUrl(action.url)

        DynamicUiClickAction.None -> Unit
    }
}
