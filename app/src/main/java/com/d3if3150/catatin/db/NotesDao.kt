
package com.d3if3150.catatin.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.d3if3150.catatin.model.Notes

@Dao
interface NotesDao {

    // insert notes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: Notes)

    // update notes
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotes(notes: Notes)

    // get all notes from db
    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Notes>>

    // delete notes by id
    @Query("DELETE FROM notes where id=:id")
    suspend fun deleteNote(id: Int)
}
