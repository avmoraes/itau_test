package br.com.itau.itaunotes.notes.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "summary")  var summary: String,
    @ColumnInfo(name = "description")  var description: String,
    @ColumnInfo(name = "priority") var priority: Int
) : Parcelable