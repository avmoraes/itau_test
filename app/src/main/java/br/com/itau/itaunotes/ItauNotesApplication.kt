package br.com.itau.itaunotes

import android.app.Application
import br.com.itau.itaunotes.login.loginModule
import br.com.itau.itaunotes.notes.notesDetailModule
import br.com.itau.itaunotes.notes.notesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ItauNotesApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ItauNotesApplication)
            modules(loginModule, notesModule, notesDetailModule)
        }
    }
}