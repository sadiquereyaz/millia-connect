package com.reyaz.feature.portal.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.reyaz.feature.portal.domain.model.PromoCard
import com.reyaz.feature.portal.domain.model.handlePromoAction

@Composable
 fun PromoCarousel(
    promoCards: List<PromoCard>,
    navController: NavController
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState { promoCards.size }

    Column {
        HorizontalPager(
            modifier = Modifier
                .heightIn(min = 250.dp),
            state = pagerState,
            contentPadding = PaddingValues(0.dp),
            pageSpacing = 0.dp
        ) { page ->
            GradientPromoCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                promoCard = promoCards[page],
                onActionClick = { action ->
                    handlePromoAction(
                        action = action,
                        navController = navController,
                        context = context
                    )
                }
            )
        }

        if(promoCards.size > 1) {
            Spacer(Modifier.padding(4.dp))
            PagerDots(
                totalDots = promoCards.size,
                selectedIndex = pagerState.currentPage
            )
        }
    }
}
