package com.d3if3150.catatin.ui.notes

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.d3if3150.catatin.R
import com.d3if3150.catatin.adapter.NotesAdapter
import com.d3if3150.catatin.app.MainActivity
import com.d3if3150.catatin.databinding.NotesFragmentBinding
import com.d3if3150.catatin.model.Notes
import com.d3if3150.catatin.utils.NotesViewState
import com.d3if3150.catatin.utils.hide
import com.d3if3150.catatin.utils.show
import com.d3if3150.catatin.utils.toast

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.notes_fragment) {

    private val viewModel: NotesViewModel by activityViewModels()
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var _binding: NotesFragmentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = NotesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.scheduleUpdater(requireActivity().application)
        setHasOptionsMenu(true)
        setUpRV()
        initViews()
        observeNotes()
        initSwipeToDeleteNote()
        onClickNotes()
    }

    private fun onClickNotes() {
        // onclick navigate to add notes
        notesAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("notes", it)
            }
            findNavController().navigate(
                R.id.action_notesFragment_to_notesDetailsFragment,
                bundle
            )
        }
    }

    private fun observeNotes() {

        // This will block will run on started state & will suspend when view moves to stop state
        lifecycleScope.launchWhenStarted {
            // Triggers the flow and starts listening for values
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is NotesViewState.Loading -> binding.progress.show()
                    is NotesViewState.Success -> {
                        binding.progress.hide()
                        onNotesLoaded(uiState.notes)
                    }
                    is NotesViewState.Error -> {
                        binding.progress.hide()
                        requireActivity().toast("Error...")
                    }
                    is NotesViewState.Empty -> {
                        binding.progress.hide()
                        showEmptyState()
                    }
                }
            }
        }
    }

    private fun initSwipeToDeleteNote() {
        // init item touch callback for swipe action
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // get item position & delete notes
                val position = viewHolder.adapterPosition
                val notes = notesAdapter.differ.currentList[position]
                viewModel.deleteNoteByID(
                    notes.id
                )
                Snackbar.make(
                    binding.root,
                    getString(R.string.note_deleted_msg),
                    Snackbar.LENGTH_LONG
                )
                    .apply {
                        setAction(getString(R.string.undo)) {
                            viewModel.insertNotes(
                                notes.title,
                                notes.description
                            )
                        }
                        show()
                    }
            }
        }

        // attach swipe callback to rv
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.notesRv)
        }
    }

    private fun initViews() {
        // onclick navigate to add notes
        binding.btnAddNotes.setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_addNotesFragment)
        }
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.show()
        notesAdapter.differ.submitList(emptyList())
    }

    private fun onNotesLoaded(notes: List<Notes>) {
        binding.emptyStateLayout.hide()
        notesAdapter.differ.submitList(notes)
    }

    private fun setUpRV() {
        notesAdapter = NotesAdapter()
        binding.notesRv.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ui_menu, menu)

        // Set the item state
        lifecycleScope.launch {
            val isChecked = viewModel.getUIMode.first()
            val item = menu.findItem(R.id.action_night_mode)
            item.isChecked = isChecked
            setUIMode(item, isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        return when (item.itemId) {
            R.id.action_night_mode -> {
                item.isChecked = !item.isChecked
                setUIMode(item, item.isChecked)
                true
            }

            R.id.action_about -> {
                findNavController().navigate(R.id.action_notesFragment_to_aboutFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUIMode(item: MenuItem, isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            viewModel.saveToDataStore(true)
            item.setIcon(R.drawable.ic_night)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            viewModel.saveToDataStore(false)
            item.setIcon(R.drawable.ic_day)
        }
    }

}
