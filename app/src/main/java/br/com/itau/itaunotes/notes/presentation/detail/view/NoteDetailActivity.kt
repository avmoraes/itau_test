package br.com.itau.itaunotes.notes.presentation.detail.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.commons.viewmodel.ViewModelFactory
import br.com.itau.itaunotes.notes.domain.model.Note
import br.com.itau.itaunotes.notes.presentation.detail.viewmodel.NoteDetailViewModel
import br.com.itau.itaunotes.notes.presentation.list.viewmodel.NotesListViewModel
import kotlinx.android.synthetic.main.activity_note_detail.*

class NoteDetailActivity : AppCompatActivity(R.layout.activity_note_detail) {

    private lateinit var viewModel: NoteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(NoteDetailViewModel::class.java)

        viewModel.loading().observe(this, Observer<Boolean> { _ ->

        })
        viewModel.noteSaved().observe(this, Observer<Boolean> { _ ->
            Toast.makeText(this, R.string.notes_detail_save_success, Toast.LENGTH_LONG).show()
            finish()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.note_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {

                val note = Note(
                    id = -1,
                    title = noteTitleText.text.toString(),
                    summary = noteSummaryText.text.toString(),
                    description = noteContentText.text.toString(),
                    priority = 1
                )

                viewModel.addNote(note)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}