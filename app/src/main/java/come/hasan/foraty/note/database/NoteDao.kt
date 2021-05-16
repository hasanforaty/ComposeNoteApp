package come.hasan.foraty.note.database

import androidx.lifecycle.LiveData
import androidx.room.*
import come.hasan.foraty.note.model.Note
import java.util.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    fun getNotes():LiveData<List<Note>>
    @Query("SELECT * FROM Note WHERE id=(:id)")
    fun getNote(id:UUID):LiveData<Note>
    @Query("SELECT * FROM Note WHERE content MATCH(:query)")
    fun searchNoteContent(query:String):List<Note>
    @Insert
    fun addNote(note: Note)
    @Update
    fun updateNote(note: Note)
    @Delete
    fun deleteNote(note: Note)

}