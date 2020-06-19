package br.com.itau.itaunotes.notes.presentation.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.itau.itaunotes.notes.data.repository.NotesRepositoryContract
import br.com.itau.itaunotes.notes.domain.model.Note
import kotlinx.android.synthetic.main.activity_note_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface NoteDetailViewModelContract{
    fun loading(): LiveData<Boolean>
    fun noteSaved(): LiveData<Boolean>
    fun priorityList(): LiveData<List<Int>>

    fun saveNote(title: String,
                  summary: String,
                  description: String,
                  priority: Int)

    fun loadProperties()
    fun setNote(note: Note)
}

class NoteDetailViewModel(
    private val repository: NotesRepositoryContract
): ViewModel(), NoteDetailViewModelContract{

    private val priorityList = listOf(1, 2, 3, 4, 5)

    private val loading = MutableLiveData<Boolean>()
    private val noteSaved = MutableLiveData<Boolean>()
    private val priorities = MutableLiveData<List<Int>>()

    override fun loading() = loading
    override fun noteSaved() = noteSaved
    override fun priorityList() = priorities

    private var noteSelected: Note? = null

    override fun saveNote(title: String,
                          summary: String,
                          description: String,
                          priority: Int) {

        loading.value = true



        CoroutineScope(Dispatchers.IO).launch {

           noteSelected?.let {
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
                    priority = priority + 1)

                repository.insert(noteToSave)
            }

            withContext(Main){
                loading.value = true
                noteSaved.value = true
            }
        }
    }

    override fun loadProperties() {
        priorities.value = priorityList
    }

    override fun setNote(note: Note) {
        noteSelected = note
    }
}