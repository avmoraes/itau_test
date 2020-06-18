package br.com.itau.itaunotes.notes.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "summary")  val summary: String,
    @ColumnInfo(name = "description")  val description: String,
    @ColumnInfo(name = "priority") val priority: Int
)