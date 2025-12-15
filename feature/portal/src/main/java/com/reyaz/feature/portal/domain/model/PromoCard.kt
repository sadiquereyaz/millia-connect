package com.reyaz.feature.portal.domain.model

import java.util.UUID

data class PromoCard(
    val id: String? = null,
    val title: String? = null,
    val bodyText: String? = null,
    val primaryAction: PromoAction?  = null,
    val secondaryAction: PromoAction? = null
)

data class PromoAction(
    val text: String,
    val action: DynamicUiClickAction? = null
)

val defaultPromoCard = PromoCard(
    id = UUID.randomUUID().toString(),
    title = "Got a Minute?",
    bodyText = "Help us shape the future of this app! Take a quick 60-second survey to let us know how we are doing. Your input makes a huge difference.",
    primaryAction = PromoAction(
        text = "Feedback",
        action = DynamicUiClickAction.ExternalUrl(
            "https://forms.gle/s3a9mjcps5HmHgrt5"
        )
    ),
    secondaryAction = PromoAction(
        text = "Rate Now!",
        action = DynamicUiClickAction.ExternalUrl(
            "https://play.google.com/store/apps/details?id=com.reyaz.milliaconnect1&hl=en_IN"
        )
    )
)