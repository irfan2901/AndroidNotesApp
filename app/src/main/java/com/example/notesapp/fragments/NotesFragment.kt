package com.example.notesapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.databinding.FragmentNotesBinding
import com.example.notesapp.repository.NotesRepository
import com.example.notesapp.utils.SwipeToDelete
import com.example.notesapp.viewmodel.NotesViewModel
import com.example.notesapp.viewmodel.NotesViewModelFactory
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var navController: NavController
    private lateinit var notesViewModel: NotesViewModel

    private fun initViewModel() {
        val activity = requireActivity() as MainActivity
        val repository = NotesRepository(NotesDatabase.getDatabase(activity))
        val viewModelFactory = NotesViewModelFactory(repository)
        notesViewModel = ViewModelProvider(this, viewModelFactory)[NotesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initViewModel()

        binding.floatingActionButton.setOnClickListener {
            navController.navigate(NotesFragmentDirections.actionNotesFragmentToSaveOrUpdateNoteFragment())
        }

        setUpRecyclerView()
        observeDataChanges()
        swipeToDelete(binding.notesRecyclerview)
    }

    private fun setUpRecyclerView() {
        notesAdapter = NotesAdapter { notesModel ->
            navController.navigate(
                NotesFragmentDirections.actionNotesFragmentToSaveOrUpdateNoteFragment(
                    notesModel
                )
            )
        }
        binding.notesRecyclerview.setHasFixedSize(true)
        binding.notesRecyclerview.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.notesRecyclerview.adapter = notesAdapter
    }

    private fun observeDataChanges() {
        lifecycleScope.launch {
            notesViewModel.notes.collect { notes ->
                notesAdapter.submitList(notes)
            }
        }
    }

    private fun swipeToDelete(notesRecyclerview: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val note = notesAdapter.currentList[position]

                // Delete the note from the ViewModel
                notesViewModel.deleteNote(note)

                // Show a Snackbar with an Undo action
                val snackBar = Snackbar.make(
                    requireView(),
                    "Note Deleted",
                    Snackbar.LENGTH_LONG
                ).addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (event != DISMISS_EVENT_ACTION) {
                            // Note was not undone, so actually delete it from database
                            notesViewModel.deleteNote(note)
                        }
                    }

                    override fun onShown(transientBottomBar: Snackbar?) {
                        super.onShown(transientBottomBar)
                        transientBottomBar?.setAction("Undo") {
                            // Undo the deletion
                            notesViewModel.insertNote(note)
                        }
                    }
                }).apply {
                    animationMode = Snackbar.ANIMATION_MODE_FADE
                    setAnchorView(binding.floatingActionButton)
                    setActionTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                }
                snackBar.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(notesRecyclerview)
    }
}
