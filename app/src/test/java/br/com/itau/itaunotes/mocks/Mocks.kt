package br.com.itau.itaunotes.mocks

import android.content.SharedPreferences
import br.com.itau.itaunotes.commons.data.database.AppDataBase
import br.com.itau.itaunotes.notes.data.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.nhaarman.mockitokotlin2.mock
import org.koin.dsl.module

val prefsMockModule = module {
    single <SharedPreferences> { mock() }
}

val dataBaseMockModule = module {
    single <AppDataBase> { mock() }
}

val firebaseMockModule = module {
    factory<FirebaseAuth> { mock() }
}

fun createMockNote(
    index: Int
):Note{
    return Note(
        index,
        "Note $index",
        "This is the note of index $index",
        "This is the long note of index $index",
        1
    )
}