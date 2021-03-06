package br.com.itau.itaunotes.notes.data.datasoruce

import br.com.itau.itaunotes.notes.data.datasoruce.dao.NotesDao
import br.com.itau.itaunotes.notes.data.model.Note

interface DataBaseDataSourceContract{
    suspend fun getAll(): List<Note>
    suspend fun loadById(id: Int): Note
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteAll()
    suspend fun deleteNote(note: Note)
}

class DataBaseDataSource(
    private val dao: NotesDao
): DataBaseDataSourceContract {

    override suspend fun getAll(): List<Note> {
        return dao.getAll()
    }

    override suspend fun loadById(id: Int): Note {
        return dao.loadById(id)
    }

    override suspend fun insertNote(note: Note) {
        return dao.insert(note)
    }

    override suspend fun updateNote(note: Note) {
        dao.update(note)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}