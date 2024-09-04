package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.data.NotesModel
import com.example.notesapp.databinding.ItemNoteBinding

class NotesAdapter(private val onItemClick: (NotesModel) -> Unit) :
    ListAdapter<NotesModel, NotesAdapter.NotesViewHolder>(NotesDiffUtil()) {
    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemNoteBinding.bind(itemView)
        val noteTitle: TextView = binding.noteTitle
        val noteDescription: TextView = binding.noteDescription
        val noteDate: TextView = binding.noteDate
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        getItem(position).let { notesModel ->
            holder.apply {
                noteTitle.text = notesModel.noteTitle
                noteDescription.text = notesModel.noteDescription
                noteDate.text = notesModel.noteDate

                itemView.setOnClickListener { onItemClick(notesModel) }
            }
        }
    }

    class NotesDiffUtil : DiffUtil.ItemCallback<NotesModel>() {
        override fun areItemsTheSame(oldItem: NotesModel, newItem: NotesModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotesModel, newItem: NotesModel): Boolean {
            return oldItem == newItem
        }
    }
}