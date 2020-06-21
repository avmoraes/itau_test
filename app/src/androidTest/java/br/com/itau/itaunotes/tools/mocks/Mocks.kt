package br.com.itau.itaunotes.tools.mocks

import br.com.itau.itaunotes.login.data.datasource.auth.AuthContract
import br.com.itau.itaunotes.login.data.datasource.baseCallBack
import br.com.itau.itaunotes.notes.data.datasoruce.dao.NotesDao
import br.com.itau.itaunotes.notes.data.model.Note
import org.koin.dsl.module

internal val mockedAuth = module {
    factory<AuthContract> { MockAuth() }
}

internal val notesDaoMock = module {
    single<NotesDao> { NotesDaoMock() }
}

class MockAuth: AuthContract{
    override fun loginByEmail(
        email: String,
        password: String,
        success: baseCallBack?,
        error: baseCallBack?
    ) {
        when(email){
            "notaspessoais@desafioitau.com" ->success?.invoke()
            else -> error?.invoke()
        }
    }
}

class NotesDaoMock: NotesDao {

    private val notes = mutableListOf<Note>(
        createNote(1),
        createNote(2)
    )

    override suspend fun getAll(): List<Note> {
        return notes
    }

    override suspend fun loadById(id: Int): Note {
        return notes[id]
    }

    override suspend fun insert(note: Note) {
        notes.add(createNote(notes.size + 1))
    }

    override suspend fun deleteAll() {
        notes.clear()
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
    }

    override fun update(note: Note) {
        val index = note.id-1
        notes[index] = note
    }
}

fun createNote(
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

