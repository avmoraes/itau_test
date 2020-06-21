package br.com.itau.itaunotes.notes.presentation.detail.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.notes.data.model.Note
import br.com.itau.itaunotes.notes.di.loadDependencies
import br.com.itau.itaunotes.notes.presentation.detail.viewmodel.NoteDetailViewModel
import kotlinx.android.synthetic.main.activity_note_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val NOTE = "note"

class NoteDetailActivity : AppCompatActivity(R.layout.activity_note_detail) {

    private val viewModel: NoteDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadDependencies()

       //TODO Implement Loading here
        viewModel.loading.observe(this, Observer<Boolean> { _ ->

        })

        viewModel.priorities.observe(this, Observer<List<Int>> { list ->
            notePrioritySpinner.apply {
                adapter = ArrayAdapter<Int>(
                    this@NoteDetailActivity,
                    android.R.layout.simple_spinner_item,
                    list
                )
            }
        })

        viewModel.noteSaved.observe(this, Observer<Boolean> { _ ->
            Toast.makeText(this, R.string.notes_detail_save_success, Toast.LENGTH_LONG).show()
            finish()
        })

        viewModel.note.observe(this, Observer { saved ->
            saved?.let { note ->
                noteTitleText.setText(note.title)
                noteSummaryText.setText(note.summary)
                noteContentText.setText(note.description)
                notePrioritySpinner.setSelection(note.priority - 1)
            }
        })

        viewModel.validTitle.observe(this, Observer {
            Toast.makeText(this, R.string.notes_detail_empty_title ,Toast.LENGTH_LONG).show()
        })

        val savedNote = intent.getParcelableExtra<Note>(NOTE)
        viewModel.loadProperties(savedNote)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.note_detail_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {

                viewModel.saveNote( title = noteTitleText.text.toString(),
                                    summary = noteSummaryText.text.toString(),
                                    description = noteContentText.text.toString(),
                                    priority = notePrioritySpinner.selectedItemPosition)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}