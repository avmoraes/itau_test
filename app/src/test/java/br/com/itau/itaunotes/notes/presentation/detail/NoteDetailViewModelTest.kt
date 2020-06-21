package br.com.itau.itaunotes.notes.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.itau.itaunotes.mocks.createMockNote
import br.com.itau.itaunotes.notes.data.model.Note
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import br.com.itau.itaunotes.notes.presentation.detail.viewmodel.NoteDetailViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NoteDetailViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: NotesRepositoryContract
    @Captor
    lateinit var loadingCaptor: ArgumentCaptor<Boolean>
    @Captor
    lateinit var noteSavedCaptor: ArgumentCaptor<Boolean>
    @Captor
    lateinit var notesCaptor: ArgumentCaptor<Note>

    @Mock
    private lateinit var loadingObserver: Observer<Boolean>
    @Mock
    private lateinit var savedObserver: Observer<Boolean>
    @Mock
    private lateinit var noteObserver: Observer<Note?>

    private lateinit var testDispatcher: TestCoroutineDispatcher
    private lateinit var viewModel: NoteDetailViewModel

    @Before
    fun setUp(){
        testDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testDispatcher)

        viewModel = NoteDetailViewModel(repository, testDispatcher)
        viewModel.note.observeForever(noteObserver)
        viewModel.loading.observeForever(loadingObserver)
        viewModel.noteSaved.observeForever(savedObserver)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test view model properties`(){
        Assert.assertNotNull(viewModel.note)
        Assert.assertNotNull(viewModel.loading)
        Assert.assertNotNull(viewModel.noteSaved)
        Assert.assertNotNull(viewModel.priorities)
   }

    @Test
    fun `test save User`() = testDispatcher.runBlockingTest {
        viewModel.saveNote(
            title = "test1",
            summary = "this is summary",
            description = "imagine this is a huge description",
            priority = 5
        )

        verify(repository).insert(any())

        loadingCaptor.run {
            verify(loadingObserver, times(2)).onChanged(capture())
        }

        noteSavedCaptor.run {
            verify(savedObserver, times(1)).onChanged(capture())
            Assert.assertNotNull(this.value)
            assertTrue(value)
        }
    }

    @Test
    fun `test update User`() = testDispatcher.runBlockingTest {

        val mockNote = createMockNote(1)
        viewModel.loadProperties(note = mockNote)

        viewModel.saveNote(
            title = "test1",
            summary = "this is summary",
            description = "imagine this is a huge description",
            priority = 5
        )

        verify(repository).update(any())

        loadingCaptor.run {
            verify(loadingObserver, times(2)).onChanged(capture())
        }

        noteSavedCaptor.run {
            verify(savedObserver, times(1)).onChanged(capture())
            Assert.assertNotNull(this.value)
            assertTrue(value)
        }
    }

    @Test
    fun `test load Properties`(){
        val mockNote = createMockNote(1)

        viewModel.loadProperties(note = mockNote)

        notesCaptor.run {
            verify(noteObserver, times(1)).onChanged(capture())
            Assert.assertNotNull(this.value)
            TestCase.assertEquals(mockNote.id, this.value.id)
        }
   }
}