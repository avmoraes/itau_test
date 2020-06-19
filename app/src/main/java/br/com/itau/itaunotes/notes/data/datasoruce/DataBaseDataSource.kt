package br.com.itau.itaunotes.notes.data.datasoruce

import br.com.itau.itaunotes.commons.data.database.AppDataBase
import br.com.itau.itaunotes.notes.domain.model.Note

interface DataBaseDataSourceContract{
    suspend fun getAll(): List<Note>
    suspend fun loadById(id: Int): Note
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun delete()
    suspend fun deleteNote(note: Note)
}

class DataBaseDataSource(dataBase: AppDataBase): DataBaseDataSourceContract {
    private val dao = dataBase.getNoteDao()

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

    override suspend fun delete() {
        dao.deleteAll()
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}