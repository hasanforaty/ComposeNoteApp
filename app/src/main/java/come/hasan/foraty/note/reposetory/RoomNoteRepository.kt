package come.hasan.foraty.note.reposetory

import come.hasan.foraty.note.database.NoteDao
import come.hasan.foraty.note.model.Note
import java.util.*
import javax.inject.Inject

@Suppress("RedundantSuspendModifier")
class RoomNoteRepository @Inject constructor(
    private val roomDao: NoteDao
) {
    suspend fun getAllNotes()=roomDao.getNotes()
    suspend fun getNoteById(id:UUID) = roomDao.getNote(id)
    suspend fun searchNoteContent(query:String) = roomDao.searchNoteContent(query)
    suspend fun addNewNote(note:Note) = roomDao.addNote(note)
    suspend fun updateNote(note: Note)=roomDao.updateNote(note)
    suspend fun deleteNote(note: Note) = roomDao.deleteNote(note)

}