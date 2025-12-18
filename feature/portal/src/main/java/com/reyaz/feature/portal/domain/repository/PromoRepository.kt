package com.reyaz.feature.portal.domain.repository

import com.reyaz.feature.portal.data.remote.model.PromoCardDto
import com.reyaz.feature.portal.domain.model.PromoCard

class PromoRepository(
//    private val firestore: FirebaseFirestore
) {

    suspend fun getPromoCards(): List<PromoCard> {
//        return firestore.collection("promo_cards")
//            .get()
//            .await()
//            .documents
//            .mapNotNull { it.toObject(PromoCardDto::class.java)?.toDomain() }
//            .sortedBy { it.priority }
        return emptyList<PromoCard>()
    }
}
