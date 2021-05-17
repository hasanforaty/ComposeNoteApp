package come.hasan.foraty.note.database

import androidx.room.*
import come.hasan.foraty.note.model.Note
import java.util.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getNotes():List<Note>
    @Query("SELECT * FROM Note WHERE id=(:id)")
    fun getNote(id:UUID):Note
    @Query("SELECT * FROM Note WHERE content MATCH(:query)")
    fun searchNoteContent(query:String):List<Note>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: Note)
    @Update
    fun updateNote(note: Note)
    @Delete
    fun deleteNote(note: Note)

}