package com.breaktime.lab3.di

import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
}