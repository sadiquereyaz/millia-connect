package com.reyaz.feature.portal.data.remote.model

import com.reyaz.feature.portal.domain.model.DynamicUiClickAction
import com.reyaz.feature.portal.domain.model.PromoAction
import com.reyaz.feature.portal.domain.model.PromoCard
import java.util.UUID

/**
{
    "id": "feedback_card",
    "enabled": true,
    "title": "Got a Minute?",
    "bodyText": "Help us shape the future of this app!",
    "priority": 1,
    "primary": {
    "text": "Feedback",
    "type": "external_url",
    "value": "https://play.google.com/store/apps/details?id=com.reyaz.milliaconnect1"
},
    "secondary": {
        "text": "Rate Now",
        "type": "deeplink",
        "value": "milliaconnect://feedback"
    }
}
*/
data class PromoCardDto(
    val id: String? = null,
    val enabled: Boolean? = null,
    val title: String? = null,
    val bodyText: String? = null,
    val priority: Int? = null,
    val primary: PromoActionDto? = null,
    val secondary: PromoActionDto? = null
)

data class PromoActionDto(
    val text: String? = null,
    val type: String? = null,
    val value: String? = null
)

fun PromoCardDto.toDomain(): PromoCard? {
    if (enabled != true) return null
    return PromoCard(
        id = id ?: UUID.randomUUID().toString(),
        title = title,
        priority = priority ?: Int.MAX_VALUE,
        bodyText = bodyText,
        primaryAction = primary?.toDomainAction(),
        secondaryAction = secondary?.toDomainAction()
    )
}

fun PromoActionDto?.toDomainAction(): PromoAction? {
    if (this == null) return null

    val text = text ?: return null
    val value = value ?: return null

    val action = when (type) {
        "external_url" -> DynamicUiClickAction.ExternalUrl(value)
        "deeplink" -> DynamicUiClickAction.DeepLink(value)
        else -> DynamicUiClickAction.None
    }

    return PromoAction(text, action)
}