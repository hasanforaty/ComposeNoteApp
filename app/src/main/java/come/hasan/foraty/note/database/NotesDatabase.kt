package come.hasan.foraty.note.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import come.hasan.foraty.note.model.Note
const val NOTES_DATABASE = "NoteDatabase"
@Database(entities = [Note::class],version = 1)
@TypeConverters(value = [NoteTypeConvertor::class])
abstract class NotesDatabase:RoomDatabase() {
    abstract fun noteDao():NoteDao
}