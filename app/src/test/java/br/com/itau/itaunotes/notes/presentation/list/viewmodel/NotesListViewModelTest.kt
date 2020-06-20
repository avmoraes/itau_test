package br.com.itau.itaunotes.notes.presentation.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.itau.itaunotes.mocks.createMockNote
import br.com.itau.itaunotes.notes.data.model.Note
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NotesListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Captor
    lateinit var loadingCaptor: ArgumentCaptor<Boolean>
    @Captor
    lateinit var notesCaptor: ArgumentCaptor<List<Note>>

    @Mock
    private lateinit var repository: NotesRepositoryContract
    @Mock
    private lateinit var loadingObserver: Observer<Boolean>
    @Mock
    private lateinit var notesObserver: Observer<List<Note>>

    private lateinit var viewModel: NotesListViewModel

    private lateinit var testDispatcher: TestCoroutineDispatcher

    @Before
    fun setUp(){
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)

        viewModel = NotesListViewModel(repository, testDispatcher)
        viewModel.list.observeForever(notesObserver)
        viewModel.loading.observeForever(loadingObserver)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test view model properties`(){
        assertNotNull(viewModel.list)
        assertNotNull(viewModel.loading)
    }

    @Test
    fun `Test Load All Notes`() = testDispatcher.runBlockingTest {
      val mockList = listOf(
            createMockNote(1),
            createMockNote(2),
            createMockNote(3)
        )

        whenever(repository.getAll()).thenReturn(mockList)

        viewModel.fetchNotes()

        loadingCaptor.run {
            verify(loadingObserver, times(2)).onChanged(capture())
        }

        notesCaptor.run {
            verify(notesObserver, times(1)).onChanged(capture())
            assertNotNull(this.value)
            assertEquals(mockList.size, this.value.size)
        }
    }

    @Test
    fun `Test Delete All Notes`() = testDispatcher.runBlockingTest {
        viewModel.deleteAllNote()

        loadingCaptor.run {
            verify(loadingObserver, times(2)).onChanged(capture())
        }

        notesCaptor.run {
            verify(notesObserver, times(1)).onChanged(capture())
            assertNotNull(this.value)
            assertEquals(0, this.value.size)
        }
    }
}