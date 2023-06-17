package com.d3if3150.catatin.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
//import androidx.work.ExistingWorkPolicy
//import androidx.work.OneTimeWorkRequestBuilder
//import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import com.d3if3150.catatin.datastore.UIModeDataStore
import com.d3if3150.catatin.model.Notes
import com.d3if3150.catatin.repo.NotesRepo
import com.d3if3150.catatin.utils.NotesViewState
import com.d3if3150.catatin.utils.network.UpdateWorker
//import com.d3if3150.catatin.utils.network.UpdateWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject internal constructor(
    application: Application,
    private val notesRepo: NotesRepo,
) :
    AndroidViewModel(application) {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<NotesViewState>(NotesViewState.Loading)

    // The UI collects from this StateFlow to get its state updates
    val uiState = _uiState.asStateFlow()

    // DataStore
    private val uiDataStore = UIModeDataStore(application)

    // get UI mode
    val getUIMode = uiDataStore.uiMode

    // save UI mode
    fun saveToDataStore(isNightMode: Boolean) {
        viewModelScope.launch(IO) {
            uiDataStore.saveToDataStore(isNightMode)
        }
    }

    // save notes
    fun insertNotes(taskName: String, taskDesc: String) = viewModelScope.launch {
        val notes = Notes(
            title = taskName,
            description = taskDesc
        )
        notesRepo.insert(notes)
    }

    // update notes
    fun updateNotes(id: Int, taskName: String, taskDesc: String) = viewModelScope.launch {
        val notes = Notes(
            id = id,
            title = taskName,
            description = taskDesc
        )
        notesRepo.update(notes)
    }

    // get all saved notes by default
    init {
        viewModelScope.launch {
            notesRepo.getSavedNotes().distinctUntilChanged().collect { result ->
                if (result.isNullOrEmpty()) {
                    _uiState.value = NotesViewState.Empty
                } else {
                    _uiState.value = NotesViewState.Success(result)
                }
            }
        }
    }

    // delete note by ID
    fun deleteNoteByID(id: Int) = viewModelScope.launch {
        notesRepo.deleteNote(id)


    }

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(app).enqueueUniqueWork(
            UpdateWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
