package br.com.itau.itaunotes.notes.di

import br.com.itau.itaunotes.mocks.dataBaseMockModule
import br.com.itau.itaunotes.notes.data.datasoruce.DataBaseDataSourceContract
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

class NotesModuleTest: AutoCloseKoinTest() {
    @Before
    fun setup(){
        startKoin{
            modules(dataBaseMockModule, notesRepoModule)
        }
    }

    @Test
    fun `Assert If DataBaseDataSourceContract Is provided by DI`(){
        val databaseDataSource by inject<DataBaseDataSourceContract>()
        assertNotNull(databaseDataSource)
    }

    @Test
    fun `Assert If NotesRepositoryContract Is provided by DI`(){
        val notesRepo by inject<NotesRepositoryContract>()
        assertNotNull(notesRepo)
    }
}