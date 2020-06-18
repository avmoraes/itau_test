package br.com.itau.itaunotes.notes.presentation.list.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.itau.itaunotes.R
import br.com.itau.itaunotes.notes.domain.model.Note
import kotlinx.android.synthetic.main.note_item_layout.view.*

typealias noteClickListener = (Note) -> Unit

class ListItemAdapter() : RecyclerView.Adapter<NoteViewHolder>(){

    var noteClick: noteClickListener? = null

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item_layout, parent, false)

        return NoteViewHolder(view, noteClick)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

}

class NoteViewHolder(
    itemView: View,
    private val noteClick: noteClickListener?
): RecyclerView.ViewHolder(itemView) {
    fun bind(note: Note) {

        itemView.noteItemTitle.text = note.title
        itemView.noteItemSummary.text = note.summary
        itemView.noteItemPriority.text = note.priority.toString()

        itemView.setOnClickListener {
            noteClick?.invoke(note)
        }
    }
}

