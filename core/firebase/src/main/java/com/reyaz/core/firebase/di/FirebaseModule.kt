package com.reyaz.core.firebase.di

import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseFirestore.getInstance() }
}