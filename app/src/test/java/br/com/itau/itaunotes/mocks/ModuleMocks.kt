package br.com.itau.itaunotes.mocks

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.nhaarman.mockitokotlin2.mock
import org.koin.dsl.module

val prefsMockModule = module {
    single <SharedPreferences> { mock() }
}

val firebaseMockModule = module {
    factory<FirebaseAuth> { mock() }
}