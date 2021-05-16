package come.hasan.foraty.note.reposetory

import androidx.annotation.WorkerThread
import come.hasan.foraty.note.database.NoteDao
import come.hasan.foraty.note.model.Note
import java.util.*
import javax.inject.Inject

class RoomNoteReposetory @Inject constructor(
    private val roomDao: NoteDao
) {
    fun getAllNotes()=roomDao.getNotes()
    fun getNoteById(id:UUID) = roomDao.getNote(id)
    fun searchNoteContent(query:String) = roomDao.searchNoteContent(query)
    fun addNewNote(note:Note) = roomDao.addNote(note)
    fun updateNote(note: Note)=roomDao.updateNote(note)
    fun deleteNote(note: Note) = roomDao.deleteNote(note)

}