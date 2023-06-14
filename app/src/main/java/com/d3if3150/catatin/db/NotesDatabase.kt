

package com.d3if3150.catatin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.d3if3150.catatin.model.Notes

@Database(
    entities = [Notes::class],
    version = 1,
    exportSchema = true
)

abstract class NotesDatabase : RoomDatabase() {
    abstract fun getNotesDao(): NotesDao
}
