package br.com.itau.itaunotes.notes.presentation.list.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.commons.viewmodel.ViewModelFactory
import br.com.itau.itaunotes.login.presentation.view.LoginActivity
import br.com.itau.itaunotes.notes.presentation.detail.view.NoteDetailActivity
import br.com.itau.itaunotes.notes.presentation.list.view.adapter.ListItemAdapter
import br.com.itau.itaunotes.notes.presentation.list.viewmodel.NotesListViewModel
import kotlinx.android.synthetic.main.activity_notes_list.*

class NotesListActivity : AppCompatActivity(R.layout.activity_notes_list) {

    private lateinit var viewModel: NotesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(NotesListViewModel::class.java)

        val adapter = ListItemAdapter()
        adapter.noteClick= { note ->
            //TODO call Detail Here
            print("Note Clicked ${note.title}")
        }

        notesList.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@NotesListActivity)
        }

        viewModel.notesList().observe(this, Observer { list ->
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
}