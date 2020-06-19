package br.com.itau.itaunotes.notes.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.itau.itaunotes.notes.data.model.Note
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteDetailViewModel(
    private val repository: NotesRepositoryContract
): ViewModel(){

    private val _loading by lazy { MutableLiveData<Boolean>() }
    private val _noteSaved by lazy { MutableLiveData<Boolean>() }
    private val _priorities by lazy { MutableLiveData(listOf(1, 2, 3, 4, 5)) }
    private val _note by lazy { MutableLiveData<Note?>() }

    val loading: LiveData<Boolean>
        get() = _loading

    val noteSaved: LiveData<Boolean>
        get() = _noteSaved

    val priorities: LiveData<List<Int>>
        get() = _priorities

    val note: LiveData<Note?>
        get() = _note

    fun saveNote(title: String,
                          summary: String,
                          description: String,
                          priority: Int) {

        _loading.value = true

        CoroutineScope(Dispatchers.IO).launch {
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
                _loading.value = true
                _noteSaved.value = true
            }
        }
    }

    fun loadProperties(note: Note?) {
        _note.value = note
    }
}