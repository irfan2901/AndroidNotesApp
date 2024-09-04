package com.example.notesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.notesapp.data.NotesModel
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.databinding.FragmentSaveOrUpdateNoteBinding
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.viewmodel.NotesViewModel
import com.example.notesapp.viewmodel.NotesViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SaveOrUpdateNoteFragment : Fragment() {
    private lateinit var binding: FragmentSaveOrUpdateNoteBinding
    private lateinit var noteViewModel: NotesViewModel
    private lateinit var navController: NavController
    private val args: SaveOrUpdateNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveOrUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val repository = NotesRepository(NotesDatabase.getDatabase(requireContext()))
        val viewModelFactory = NotesViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, viewModelFactory)[NotesViewModel::class.java]

        binding.backArrow.setOnClickListener {
            navController.popBackStack()
        }

        binding.addNoteButton.setOnClickListener {
            addNote()
        }

        showNote()
    }

    private fun showNote() {
        val note = args.Note
        if (note != null) {
            binding.etNoteTitle.setText(note.noteTitle)
            binding.etNoteDescription.setText(note.noteDescription)
        }
    }

    private fun addNote() {
        val note = args.Note
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        when (note) {
            null -> {
                val noteModel = NotesModel(
                    0,
                    binding.etNoteTitle.text.toString(),
                    binding.etNoteDescription.text.toString(),
                    date
                )
                noteViewModel.insertNote(noteModel)
                showToast("Note added successfully...")
                navigateToNotesFragment()
            }
            else -> updateNote()
        }
    }

    private fun updateNote() {
        val note = args.Note
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        if (note != null) {
            val notesModel = NotesModel(
                note.id,
                binding.etNoteTitle.text.toString(),
                binding.etNoteDescription.text.toString(),
                date
            )
            noteViewModel.updateNote(notesModel)
            showToast("Note updated successfully...")
            navigateToNotesFragment()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToNotesFragment() {
        navController.navigate(SaveOrUpdateNoteFragmentDirections.actionSaveOrUpdateNoteFragmentToNotesFragment())
    }
}
