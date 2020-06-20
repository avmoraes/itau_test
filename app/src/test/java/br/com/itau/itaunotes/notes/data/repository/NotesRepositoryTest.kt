package br.com.itau.itaunotes.notes.data.repository

import br.com.itau.itaunotes.mocks.createMockNote
import br.com.itau.itaunotes.notes.data.datasoruce.DataBaseDataSourceContract
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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
class NotesRepositoryTest {

    @Mock
    private lateinit var dbDataSource: DataBaseDataSourceContract

    private lateinit var notesRepository: NotesRepositoryContract

    @Before
    fun setUp(){
        notesRepository = NotesRepository(dbDataSource)
    }

    @Test
    fun `Test get all Notes`() = runBlockingTest{
        whenever(dbDataSource.getAll()).thenReturn(
            listOf(
                createMockNote(1),
                createMockNote(2),
                createMockNote(3)
            )
        )

        val notes = notesRepository.getAll()

        assertNotNull(notes)
        assertTrue(notes.size == 3)
    }

    @Test
    fun `Test load note by Id`() = runBlockingTest{
        val note = createMockNote(3)

        whenever(dbDataSource.loadById(any())).doReturn(note)
        val resultNote = notesRepository.loadById(3)

        assertEquals(note.id, resultNote.id)
    }

    @Test
    fun `Test insert note`() = runBlockingTest{
        val note = createMockNote(3)

        notesRepository.insert(note)

        verify(dbDataSource).insertNote(note)
    }

    @Test
    fun `Test update note`() = runBlockingTest{
        val note = createMockNote(3)

        notesRepository.update(note)

        verify(dbDataSource).updateNote(note)
    }

    @Test
    fun `Test delete all notes`() = runBlockingTest{
        notesRepository.deleteAll()

        verify(dbDataSource).deleteAll()
    }

    @Test
    fun `Test delete single note`() = runBlockingTest{
        val note = createMockNote(3)

        notesRepository.deleteNote(note)

        verify(dbDataSource).deleteNote(note)
    }
}