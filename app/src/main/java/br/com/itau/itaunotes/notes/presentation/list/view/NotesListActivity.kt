package br.com.itau.itaunotes.notes.presentation.list.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.login.presentation.view.LoginActivity
import br.com.itau.itaunotes.notes.data.model.Note
import br.com.itau.itaunotes.notes.di.loadDependencies
import br.com.itau.itaunotes.notes.presentation.detail.view.NOTE
import br.com.itau.itaunotes.notes.presentation.detail.view.NoteDetailActivity
import br.com.itau.itaunotes.notes.presentation.list.view.adapter.ListItemAdapter
import br.com.itau.itaunotes.notes.presentation.list.viewmodel.NotesListViewModel
import kotlinx.android.synthetic.main.activity_notes_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotesListActivity : AppCompatActivity(R.layout.activity_notes_list) {

    private val viewModel: NotesListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadDependencies()

        val adapter = ListItemAdapter().apply {
            noteClick = { note ->
                goToEdit(note)
            }
        }

        notesList.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@NotesListActivity)
            this.addItemDecoration(DividerItemDecoration(this@NotesListActivity,  LinearLayoutManager.VERTICAL))
        }

        viewModel.list.observe(this, Observer { list ->
            adapter.notes = list
        })

        addNoteButton.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.note_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exclude_all -> {
                viewModel.deleteAllNote()
                true
            }
            R.id.logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToEdit(note: Note){
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra(NOTE, note)
        }
        startActivity(intent)
    }
}