

package com.d3if3150.catatin.utils

import com.d3if3150.catatin.model.Notes

// Represents different states for the LatestNews screen
sealed class NotesViewState {
    object Empty : NotesViewState()
    object Loading : NotesViewState()
    data class Success(val notes: List<Notes>) : NotesViewState()
    data class Error(val exception: Throwable) : NotesViewState()
}
