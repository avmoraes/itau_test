package br.com.itau.itaunotes.notes.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "summary")  val summary: String,
    @ColumnInfo(name = "description")  val description: String,
    @ColumnInfo(name = "priority") val priority: Int
) : Parcelable