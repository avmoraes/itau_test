package br.com.itau.itaunotes.notes.data.datasource

import br.com.itau.itaunotes.commons.data.database.AppDataBase
import br.com.itau.itaunotes.mocks.createMockNote
import br.com.itau.itaunotes.notes.data.datasoruce.DataBaseDataSource
import br.com.itau.itaunotes.notes.data.datasoruce.DataBaseDataSourceContract
import br.com.itau.itaunotes.notes.data.datasoruce.dao.NotesDao
import com.nhaarman.mockitokotlin2.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DataBaseDataSourceTest {

    @Mock
    private lateinit var dao: NotesDao

    private lateinit var dataBaseDataSource: DataBaseDataSourceContract

    @Before
    fun setUp(){

        val dataBase = mock<AppDataBase>()

        whenever(dataBase.getNoteDao()).doReturn(dao)

        dataBaseDataSource = DataBaseDataSource(dataBase)
    }

    @Test
    fun `Test get all Notes`() = runBlockingTest{
        whenever(dao.getAll()).thenReturn(
            listOf(
                createMockNote(1),
                createMockNote(2),
                createMockNote(3)
            )
        )

        val notes = dataBaseDataSource.getAll()

        assertNotNull(notes)
        assertTrue(notes.size == 3)
    }

    @Test
    fun `Test load note by Id`() = runBlockingTest{

        val note = createMockNote(3)

        whenever(dao.loadById(any())).doReturn(note)
        val resultNote = dataBaseDataSource.loadById(3)

        assertEquals(note.id, resultNote.id)
    }

    @Test
    fun `Test insert note`() = runBlockingTest{
        val note = createMockNote(3)

        dataBaseDataSource.insertNote(note)

        verify(dao).insert(note)
    }

    @Test
    fun `Test update note`() = runBlockingTest{
        val note = createMockNote(3)

        dataBaseDataSource.updateNote(note)

        verify(dao).update(note)
    }

    @Test
    fun `Test delete all notes`() = runBlockingTest{
        dataBaseDataSource.deleteAll()

        verify(dao).deleteAll()
    }

    @Test
    fun `Test delete single note`() = runBlockingTest{
        val note = createMockNote(3)

        dataBaseDataSource.deleteNote(note)

        verify(dao).deleteNote(note)
    }
}