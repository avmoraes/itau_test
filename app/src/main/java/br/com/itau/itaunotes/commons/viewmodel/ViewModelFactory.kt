package br.com.itau.itaunotes.commons.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import br.com.itau.itaunotes.commons.data.database.AppDataBase
import br.com.itau.itaunotes.login.data.datasource.CacheDataSource
import br.com.itau.itaunotes.login.data.datasource.FirebaseDataSource
import br.com.itau.itaunotes.login.data.datasource.USER_KEY
import br.com.itau.itaunotes.login.data.repository.LoginRepository
import br.com.itau.itaunotes.login.presentation.viewmodel.LoginViewModel
import br.com.itau.itaunotes.notes.data.datasoruce.DataBaseDataSource
import br.com.itau.itaunotes.notes.data.repository.NotesRepository
import br.com.itau.itaunotes.notes.presentation.detail.viewmodel.NoteDetailViewModel
import br.com.itau.itaunotes.notes.presentation.list.viewmodel.NotesListViewModel

class ViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) -> {

                    val sharedPrefs = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)
                    val fireBaseDataSource = FirebaseDataSource()
                    val cacheDataSource = CacheDataSource(sharedPrefs)

                    val repository = LoginRepository(fireBaseDataSource, cacheDataSource)
                    LoginViewModel(repository)
                }
                isAssignableFrom(NotesListViewModel::class.java) -> {
                    val db = Room.databaseBuilder(
                       context,
                       AppDataBase::class.java, "database-name"
                   ).build()

                    val repository = NotesRepository(DataBaseDataSource(db))
                    NotesListViewModel(repository)
               }
                isAssignableFrom(NoteDetailViewModel::class.java) -> {
                    val db = Room.databaseBuilder(
                        context,
                        AppDataBase::class.java, "database-name"
                    ).build()

                    val repository = NotesRepository(DataBaseDataSource(db))
                    NoteDetailViewModel(repository)
                }
                else ->
                    throw IllegalArgumentException("Classe desconhecida.")
            }
        } as T
    }
}