package br.com.itau.itaunotes.commons.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.itau.itaunotes.notes.data.datasoruce.dao.NotesDao
import br.com.itau.itaunotes.notes.data.model.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getNoteDao(): NotesDao
}