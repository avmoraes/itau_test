package br.com.itau.itaunotes.login.di

import android.content.Context
import android.content.SharedPreferences
import br.com.itau.itaunotes.login.data.datasource.*
import br.com.itau.itaunotes.login.data.repository.LoginRepository
import br.com.itau.itaunotes.login.data.repository.LoginRepositoryContract
import br.com.itau.itaunotes.login.presentation.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single <SharedPreferences> { androidContext().getSharedPreferences(USER_KEY, Context.MODE_PRIVATE) }

    factory <LoginDataSourceContract> { FirebaseDataSource()  }
    factory <CacheDataSourceContract> { CacheDataSource(get()) }
    factory <LoginRepositoryContract> { LoginRepository(get(), get()) }

    viewModel{ LoginViewModel(get()) }
}
