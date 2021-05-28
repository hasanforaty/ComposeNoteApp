package come.hasan.foraty.note.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import come.hasan.foraty.note.model.Note
import come.hasan.foraty.note.reposetory.RoomNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

private const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel @Inject constructor(
    private val noteRepository: RoomNoteRepository
):ViewModel() {

    private val noteList:MutableLiveData<List<Note>> = MutableLiveData<List<Note>>()
    val  notes:LiveData<List<Note>> = Transformations.map(noteList){ notes ->
        notes
    }
    private val note:MutableLiveData<Note> = MutableLiveData<Note>()
    val currentNote:LiveData<Note> = Transformations.map(note){
        note->
        note
    }

    val currentNoteTitle:MutableLiveData<String> = Transformations.map(note){
        Log.d(TAG, "current note title : $it")
        it.title
    } as MutableLiveData<String>
    fun changeTitle(title:String){
        currentNote.value?.title = title
        currentNoteTitle.value = title
    }
    val currentNoteContent:MutableLiveData<String> = Transformations.map(note){
        it.content
    } as MutableLiveData<String>
    fun changeContent(content:String){
        currentNote.value?.content  = content
        currentNoteContent.value = content
    }


    fun getAllNotes(){
        CoroutineScope(IO).launch {
            val allNotes=noteRepository.getAllNotes()
            Log.d(TAG, "getAllNotes: notes : $allNotes")
            allNotes.sortedBy { note ->
                note.date
            }
            withContext(Main){
                noteList.value = allNotes
            }
        }
    }
    fun getNoteById(id:UUID){
        CoroutineScope(IO).launch {
            val receivedNote = noteRepository.getNoteById(id)
            withContext(Main){
                note.value = receivedNote
            }
        }
    }
    fun getNote(receivedNote:Note){
        CoroutineScope(IO).launch {
            withContext(Main){
                note.value = receivedNote
            }
        }
    }
    fun queryContent(query:String){
        CoroutineScope(IO).launch {
            val wantedNotes = noteRepository.searchNoteContent(query)
            noteList.value = wantedNotes
        }
    }

    fun addNote(note:Note){
        CoroutineScope(IO).launch {
            noteRepository.addNewNote(note)
        }
    }

    fun deleteNote(note: Note){
        CoroutineScope(IO).launch {
            noteRepository.deleteNote(note)
        }
    }


    companion object{
        fun notesMock():List<Note> = listOf(
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
            Note.mock(),
        )
    }

}