package br.com.itau.itaunotes.login.di

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import br.com.itau.itaunotes.login.data.datasource.*
import br.com.itau.itaunotes.login.data.datasource.auth.AuthContract
import br.com.itau.itaunotes.login.data.datasource.auth.FirebaseEmailAuth
import br.com.itau.itaunotes.login.data.repository.LoginRepository
import br.com.itau.itaunotes.login.data.repository.LoginRepositoryContract
import br.com.itau.itaunotes.login.presentation.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val authModule = module {
    factory<AuthContract> { FirebaseEmailAuth(FirebaseAuth.getInstance()) }
}

val prefsModule = module {
    single <SharedPreferences> { androidContext().getSharedPreferences(USER_KEY, Context.MODE_PRIVATE) }
}

val loginDependenciesModule = module {
    factory <LoginDataSourceContract> { FirebaseDataSource(get())  }
    factory <CacheDataSourceContract> { CacheDataSource(get()) }
    factory <LoginRepositoryContract> { LoginRepository(get(), get()) }
}

val loginModule = module {
    viewModel{ LoginViewModel(get()) }
}

@VisibleForTesting
private val loginDependencies by lazy { loadKoinModules(listOf(prefsModule, authModule, loginDependenciesModule)) }
internal fun loadDependencies() = loginDependencies

