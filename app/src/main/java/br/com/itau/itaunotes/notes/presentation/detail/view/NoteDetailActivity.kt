package br.com.itau.itaunotes.notes.presentation.detail.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.notes.domain.model.Note
import br.com.itau.itaunotes.notes.presentation.detail.viewmodel.NoteDetailViewModel
import kotlinx.android.synthetic.main.activity_note_detail.*
import org.koin.android.ext.android.inject

const val NOTE = "note"

class NoteDetailActivity : AppCompatActivity(R.layout.activity_note_detail) {

    private val viewModel: NoteDetailViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       //TODO Implement Loading here
        viewModel.loading().observe(this, Observer<Boolean> { _ ->

        })

        viewModel.loadProperties()

        viewModel.priorityList().observe(this, Observer<List<Int>> { list ->
            notePrioritySpinner.apply {
                adapter = ArrayAdapter<Int>(
                    this@NoteDetailActivity,
                    android.R.layout.simple_spinner_item,
                    list
                )
            }
        })

        viewModel.noteSaved().observe(this, Observer<Boolean> { _ ->
            Toast.makeText(this, R.string.notes_detail_save_success, Toast.LENGTH_LONG).show()
            finish()
        })

        intent.getParcelableExtra<Note>(NOTE)?.let { note ->
            viewModel.setNote(note)

            noteTitleText.setText(note.title)
            noteSummaryText.setText(note.summary)
            noteContentText.setText(note.description)
            notePrioritySpinner.setSelection(note.priority - 1)
        }
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