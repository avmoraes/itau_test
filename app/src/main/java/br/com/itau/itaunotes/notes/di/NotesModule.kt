package br.com.itau.itaunotes.notes.di

import androidx.annotation.VisibleForTesting
import androidx.room.Room
import br.com.itau.itaunotes.commons.data.database.AppDataBase
import br.com.itau.itaunotes.notes.data.datasoruce.DataBaseDataSource
import br.com.itau.itaunotes.notes.data.datasoruce.DataBaseDataSourceContract
import br.com.itau.itaunotes.notes.data.repository.NotesRepository
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import br.com.itau.itaunotes.notes.presentation.detail.viewmodel.NoteDetailViewModel
import br.com.itau.itaunotes.notes.presentation.list.viewmodel.NotesListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val dataBaseModule = module {
    single { Room.databaseBuilder(
                androidApplication(),
                AppDataBase::class.java, "database-name"
            ).build()
    }
}

val notesRepoModule = module {
    factory { Dispatchers.IO }
    factory<DataBaseDataSourceContract> { DataBaseDataSource(get()) }
    single<NotesRepositoryContract> { NotesRepository(get()) }
}

val notesModule = module {
    viewModel { NotesListViewModel(get(), get()) }
}

val notesDetailModule = module {
    viewModel { NoteDetailViewModel(get(), get()) }
}

@VisibleForTesting
private val notesDependency by lazy { loadKoinModules(listOf(dataBaseModule, notesRepoModule)) }
internal fun loadDependencies() = notesDependency

