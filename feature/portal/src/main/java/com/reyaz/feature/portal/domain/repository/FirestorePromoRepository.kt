package com.reyaz.feature.portal.domain.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.reyaz.feature.portal.data.remote.model.PromoCardDto
import com.reyaz.feature.portal.data.remote.model.toDomain
import com.reyaz.feature.portal.domain.model.PromoCard
import kotlinx.coroutines.tasks.await

class FirestorePromoRepository(
    private val firestore: FirebaseFirestore
) {
    suspend fun getPromoCards(): List<PromoCard> {
        return firestore.collection("promo_cards")
            .whereEqualTo("enabled", true)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(PromoCardDto::class.java)?.toDomain() }
            .sortedBy { it.priority }
    }
}
