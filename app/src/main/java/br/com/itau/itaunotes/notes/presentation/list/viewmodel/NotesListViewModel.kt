package br.com.itau.itaunotes.notes.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import br.com.itau.itaunotes.notes.data.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesListViewModel(
    private val repository: NotesRepositoryContract
): ViewModel() {

    private val _list by lazy { MutableLiveData<List<Note>>() }
    private val _loading by lazy { MutableLiveData<Boolean>() }

    val list: LiveData<List<Note>>
        get() = _list

    val loading: LiveData<Boolean>
        get() = _loading

    fun deleteAllNote() {
        _loading.value = true

        CoroutineScope(IO).launch {
            repository.deleteAll()

            withContext(Main){
                _loading.value = false
                _list.value = listOf()
            }
        }
    }

    fun fetchNotes() {
        _loading.value = true
        CoroutineScope(IO).launch {
           val list = repository.getAll()

           withContext(Main){
               _loading.value = false
               _list.value = list
           }
       }
    }
}