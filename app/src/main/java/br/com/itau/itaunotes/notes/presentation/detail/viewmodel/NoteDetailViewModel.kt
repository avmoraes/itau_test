package br.com.itau.itaunotes.notes.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.itau.itaunotes.notes.data.model.Note
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteDetailViewModel(
    private val repository: NotesRepositoryContract,
    private val dispatcher: CoroutineDispatcher
): ViewModel(){

    private val _noteSaved by lazy { MutableLiveData<Boolean>() }
    private val _priorities by lazy { MutableLiveData(listOf(1, 2, 3, 4, 5)) }
    private val _note by lazy { MutableLiveData<Note?>() }
    private val _validTitle by lazy { MutableLiveData<Boolean>() }

    val noteSaved: LiveData<Boolean>
        get() = _noteSaved

    val priorities: LiveData<List<Int>>
        get() = _priorities

    val note: LiveData<Note?>
        get() = _note

    val validTitle: LiveData<Boolean>
        get() = _validTitle

    fun saveNote(title: String,
                          summary: String,
                          description: String,
                          priority: Int) {

        when {
            title.isEmpty()-> _validTitle.value = false
            else -> {
                viewModelScope.launch(dispatcher) {
                    _note.value?.let {
                        it.apply {
                            this.title = title
                            this.summary = summary
                            this.description = description
                            this.priority = priority + 1
                        }

                        repository.update(it)
                    } ?: run {
                        val noteToSave = Note(
                            title = title,
                            summary = summary,
                            description = description,
                            priority = priority + 1
                        )

                        repository.insert(noteToSave)
                    }

                    withContext(Main){
                        _noteSaved.value = true
                    }
                }
            }
        }
    }

    fun loadProperties(note: Note?) {
        _note.value = note
    }
}