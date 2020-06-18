package br.com.itau.itaunotes.notes.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import br.com.itau.itaunotes.notes.domain.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface NoteDetailViewModelContract{
    fun loading(): LiveData<Boolean>
    fun noteSaved(): LiveData<Boolean>
    fun priorityList(): LiveData<List<Int>>

    fun addNote(note: Note)
    fun loadProperties()
}

class NoteDetailViewModel(
    private val repository: NotesRepositoryContract
): ViewModel(), NoteDetailViewModelContract{

    private val loading = MutableLiveData<Boolean>()
    private val noteSaved = MutableLiveData<Boolean>()
    private val priorities = MutableLiveData<List<Int>>()

    private val priorityList = listOf(1,2,3,4,5)

    override fun loading(): LiveData<Boolean> {
        return loading
    }

    override fun noteSaved(): LiveData<Boolean> {
        return noteSaved
    }

    override fun priorityList(): LiveData<List<Int>> {
        return priorities
    }

    override fun addNote(note: Note) {
        loading.value = true

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(note)

            withContext(Main){
                loading.value = true
                noteSaved.value = true
            }
        }
    }

    override fun loadProperties() {
        priorities.value = priorityList
    }
}