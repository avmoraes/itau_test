package br.com.itau.itaunotes.notes.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import br.com.itau.itaunotes.notes.data.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesListViewModel(
    private val repository: NotesRepositoryContract,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _list by lazy { MutableLiveData<List<Note>>() }
    private val _loading by lazy { MutableLiveData<Boolean>() }

    val list: LiveData<List<Note>>
        get() = _list

    val loading: LiveData<Boolean>
        get() = _loading

    fun deleteAllNote() {
        _loading.value = true

        viewModelScope.launch(dispatcher) {
            repository.deleteAll()

            withContext(Main){
                _loading.value = false
                _list.value = listOf()
            }
        }
    }

    fun fetchNotes() {
        _loading.value = true
        viewModelScope.launch(dispatcher) {
           val list = repository.getAll()

           withContext(Main){
               _loading.value = false
               _list.value = list
           }
       }
    }
}