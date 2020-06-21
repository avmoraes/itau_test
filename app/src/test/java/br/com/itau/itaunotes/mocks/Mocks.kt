package br.com.itau.itaunotes.mocks

import android.content.SharedPreferences
import br.com.itau.itaunotes.commons.data.database.AppDataBase
import br.com.itau.itaunotes.login.data.datasource.auth.AuthContract
import br.com.itau.itaunotes.notes.data.datasoruce.dao.NotesDao
import br.com.itau.itaunotes.notes.data.model.Note
import com.nhaarman.mockitokotlin2.mock
import org.koin.dsl.module

val prefsMockModule = module {
    single <SharedPreferences> { mock() }
}

val dataBaseMockModule = module {
    single <NotesDao> { mock() }
}

val firebaseMockModule = module {
    factory<AuthContract> { mock() }
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