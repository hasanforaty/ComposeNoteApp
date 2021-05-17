package come.hasan.foraty.note.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import come.hasan.foraty.note.model.Note
import come.hasan.foraty.note.reposetory.RoomNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

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
    fun getAllNotes(){
        CoroutineScope(IO).launch {
            val allNotes=noteRepository.getAllNotes()
            noteList.value = allNotes
        }
    }
    fun getNoteById(id:UUID){
        CoroutineScope(IO).launch {
            note.value = noteRepository.getNoteById(id)
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

}