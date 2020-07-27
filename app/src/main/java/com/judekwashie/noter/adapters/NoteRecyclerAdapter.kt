package com.judekwashie.noter.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.judekwashie.noter.R
import com.judekwashie.noter.entity.Note
import com.judekwashie.noter.fragments.NoteListFragmentDirections
import kotlinx.android.synthetic.main.list_note_item.view.*

class NoteRecyclerAdapter(

    private var navController: NavController

) : ListAdapter<Note, NoteRecyclerAdapter.NoteViewHolder>(NoteItemsDiffUtilCallback()) {


    inner class NoteViewHolder(itemView: View, navController: NavController) :
        RecyclerView.ViewHolder(itemView) {
        var note: Note? = null
        var noteTitle: TextView? = itemView.note_title
        var noteText: TextView? = itemView.note_text


        init {
            itemView.setOnClickListener {
                val note = getItem(layoutPosition)
                val action =
                    NoteListFragmentDirections.actionNoteListFragmentToNoteEditReadFragment(note.noteId)
                navController.navigate(action)
            }
        }

        fun setNoteProperties(note: Note) {
            this.note = note
            noteTitle?.text = note.noteTitle
            noteText?.text = note.noteText
        }


    }

    class NoteItemsDiffUtilCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {

           return oldItem.noteTitle == newItem.noteTitle && oldItem.noteText == newItem.noteText &&
                    oldItem.noteDate == newItem.noteDate

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_note_item, parent, false)
        return NoteViewHolder(itemView, navController)
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.setNoteProperties(getItem(position))
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }


}