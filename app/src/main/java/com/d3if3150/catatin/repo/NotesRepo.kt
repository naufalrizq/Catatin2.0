
package com.d3if3150.catatin.repo

import com.d3if3150.catatin.db.NotesDatabase
import com.d3if3150.catatin.model.Notes
import javax.inject.Inject

class NotesRepo @Inject constructor(private val db: NotesDatabase) {

    // insert notes
    suspend fun insert(notes: Notes) = db.getNotesDao().insertNotes(notes)

    // update notes
    suspend fun update(notes: Notes) = db.getNotesDao().updateNotes(notes)

    // get saved notes
    fun getSavedNotes() = db.getNotesDao().getNotes()

    // delete note by ID
    suspend fun deleteNote(id: Int) = db.getNotesDao().deleteNote(id)
}
