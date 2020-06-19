package br.com.itau.itaunotes.notes.data.datasoruce.dao

import androidx.room.*
import br.com.itau.itaunotes.notes.domain.model.Note

@Dao
interface NotesDao {
    @Query("SELECT * FROM note")
    suspend fun getAll(): List<Note>

    @Query("SELECT * FROM note WHERE id == (:id)")
    suspend fun loadById(id: Int): Note

    @Insert
    suspend fun insert(note: Note)

    @Query("DELETE FROM note")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteNote(note: Note)

    @Update()
    fun update(note: Note)
}