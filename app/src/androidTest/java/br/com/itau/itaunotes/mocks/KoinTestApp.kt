package br.com.itau.itaunotes.mocks

import android.app.Application
import br.com.itau.itaunotes.login.di.loginModule
import br.com.itau.itaunotes.notes.di.notesDetailModule
import br.com.itau.itaunotes.notes.di.notesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KoinTestApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@KoinTestApp)
            modules(mockedAuth, loginModule , notesModule, notesDetailModule)
        }
    }
}