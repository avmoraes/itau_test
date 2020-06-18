package br.com.itau.itaunotes.notes.presentation.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import br.com.itau.itaunotes.notes.domain.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ViewModelContract{
    fun notesList():LiveData<List<Note>>
    fun loading():LiveData<Boolean>
    fun deleteAllNote()
    fun fetchNotes()
}

class NotesListViewModel(
    private val repository: NotesRepositoryContract
): ViewModel(), ViewModelContract {

    private val listLiveData = MutableLiveData<List<Note>>()
    private val loadingLiveData = MutableLiveData<Boolean>()

    override fun notesList(): LiveData<List<Note>> {
        return listLiveData
    }

    override fun loading(): LiveData<Boolean> {
        return loadingLiveData
    }

    override fun deleteAllNote() {
        loadingLiveData.value = true

        CoroutineScope(IO).launch {
            repository.deleteAll()

            withContext(Main){
                loadingLiveData.value = false
                listLiveData.value = listOf()
            }
        }
    }

    override fun fetchNotes() {
        loadingLiveData.value = true
        CoroutineScope(IO).launch {
           val list = repository.getAll()

           withContext(Main){
               loadingLiveData.value = false
               listLiveData.value = list
           }
       }
    }
}